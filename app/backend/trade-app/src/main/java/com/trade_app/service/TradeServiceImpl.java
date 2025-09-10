package com.trade_app.service;

import com.trade_app.common.exception.BadRequestError;
import com.trade_app.common.exception.NotFoundError;
import com.trade_app.common.shared.PageResponse;
import com.trade_app.domain.entity.Account;
import com.trade_app.domain.entity.Instrument;
import com.trade_app.domain.entity.Trade;
import com.trade_app.domain.enums.Direction;
import com.trade_app.domain.enums.Status;
import com.trade_app.dto.request.ExerciseRequest;
import com.trade_app.dto.request.TradeFilterRequest;
import com.trade_app.dto.request.TradeRequest;
import com.trade_app.dto.response.TradeResponse;
import com.trade_app.kafka.producers.TradeEventProducer;
import com.trade_app.mapper.TradeMapper;
import com.trade_app.repository.AccountRepository;
import com.trade_app.repository.InstrumentRepository;
import com.trade_app.repository.TradeRepository;
import com.trade_app.service.interfaces.TradeService;
import com.trade_app.specifications.TradeSpecification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TradeServiceImpl implements TradeService {
    private final TradeRepository tradeRepository;
    private final TradeMapper tradeMapper;
    private final InstrumentRepository instrumentRepository;
    private final AccountRepository accountRepository;
    private static final Logger logger = LoggerFactory.getLogger(TradeServiceImpl.class);
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public PageResponse<TradeResponse> getAll(TradeFilterRequest filterRequest, Pageable pageable) {
        Specification<Trade> spec = TradeSpecification.filter(filterRequest);

        Page<Trade> page = tradeRepository.findAll(spec, pageable);

        List<TradeResponse> trades = page
                .stream()
                .map(tradeMapper::toResponse)
                .toList();

        return new PageResponse<TradeResponse>(
                trades,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }



    @Override
    public TradeResponse getTrade(Long id){
        Trade trade = tradeRepository.findById(id).orElseThrow(()-> new NotFoundError("Trade not found"));
        return tradeMapper.toResponse(trade);
    }



    @Override
    @Transactional
    public TradeResponse createTrade(TradeRequest tradeRequest){
        Instrument instrument = instrumentRepository.findById(tradeRequest.getInstrumentId())
                .orElseThrow(() -> new NotFoundError("Instrument not found"));

        Account account = accountRepository.findById(tradeRequest.getAccountId())
                .orElseThrow(() -> new NotFoundError("Account not found"));
        if(tradeRequest.getQuantity() < 1){throw  new BadRequestError("Quantity must be greater than 0");}

        Trade trade = new Trade();
        trade.setInstrument(instrument);
        trade.setAccount(account);
        trade.setQuantity(tradeRequest.getQuantity());
        trade.setPrice(tradeRequest.getPrice());
        trade.setDirection(tradeRequest.getDirection());
        trade.setUnit(tradeRequest.getUnit());
        trade.setDeliveryType(tradeRequest.getDeliveryType());
        trade.setStatus(Status.OPEN);

        TradeResponse savedTrade = matchingTrade(trade);
        if(savedTrade != null){
            messagingTemplate.convertAndSend("/topic/trade", savedTrade);
        }
        return savedTrade;
    }

    @Override
    @Transactional
    public TradeResponse matchingTrade(Trade trade) {
        List<Trade> matchedTrade = tradeRepository.findFirstMatchingTrade(trade.getPrice(), trade.getQuantity(), trade.getInstrument().getId(), trade.getDirection(), trade.getDeliveryType(), trade.getUnit(), trade.getAccount().getId());
        if(!matchedTrade.isEmpty() && matchedTrade.getFirst() != null){
            Trade toMatch = matchedTrade.getFirst();
            toMatch.setStatus(Status.MATCHED);
            trade.setStatus(Status.MATCHED);
            List<Trade> saved = tradeRepository.saveAll(Arrays.asList(trade, toMatch));
            return tradeMapper.toResponse(saved.getFirst());
        }
        trade.setStatus(Status.OPEN);
        return tradeMapper.toResponse(tradeRepository.save(trade));

    }

    @Override
    @Transactional
    public TradeResponse updateTrade(Long id, TradeRequest tradeRequest){
        Trade trade = tradeRepository.findById(id).orElseThrow(()-> new NotFoundError("Trade not found"));
        if(trade.getStatus()!= Status.OPEN && trade.getStatus() != Status.MATCHED){throw  new BadRequestError("Trade can only be modified when it is opened or matched");}

        if(tradeRequest.getQuantity() < 1){throw  new BadRequestError("Quantity must be greater than 0");}

        if(trade.getStatus() == Status.MATCHED){
            matchingAlgorithmByTrade(trade);
            trade.setStatus(Status.OPEN);
        }

        trade.setDeliveryType(tradeRequest.getDeliveryType() == null ? trade.getDeliveryType() : tradeRequest.getDeliveryType());
        trade.setPrice(tradeRequest.getPrice() == null ? trade.getPrice() : tradeRequest.getPrice());
        trade.setQuantity(tradeRequest.getQuantity() == null ? trade.getQuantity() : tradeRequest.getQuantity());
        trade.setUnit(tradeRequest.getUnit() == null ? trade.getUnit() : tradeRequest.getUnit());

        TradeResponse savedTrade = matchingTrade(trade);
        if(savedTrade != null){
            messagingTemplate.convertAndSend("/topic/trade", savedTrade);
        }
        return savedTrade;

    }

    @Override
    @Transactional
    public void deleteTrade(Long id){
        Trade trade = tradeRepository.findById(id).orElseThrow(()-> new NotFoundError("Trade not found"));
        if(trade.getStatus() == Status.MATCHED){
            matchingAlgorithmByTrade(trade);
        }
        tradeRepository.delete(trade);
        messagingTemplate.convertAndSend("/topic/trade", trade);
    }

    @Override
    public Trade callExercise(Trade trade){
        if(trade.getDirection()!= Direction.BUY){throw new BadRequestError("Direction must be BUY");}
        return  trade;
    }

    @Override
    public Trade putExercise(Trade trade){
        if(trade.getDirection()!= Direction.SELL){throw new BadRequestError("Direction must be SELL");}

        return trade;
    }

    @Override
    public TradeResponse closeTrade(Long id){
        Trade trade = tradeRepository.findById(id).orElseThrow(()-> new NotFoundError("Trade not found"));
        if(trade.getStatus()!= Status.CLOSED){throw new BadRequestError("Trade is already closed");}
        if(LocalDateTime.now().isAfter(trade.getInstrument().getMaturityDate())){
            throw new BadRequestError("Date must be before maturity date");
        }
        trade.setStatus(Status.CLOSED);
        return tradeMapper.toResponse(tradeRepository.save(trade));
    }


    @Transactional
    @Override
    public List<Trade> fetchOpenTrades(){
        int closedTrades = tradeRepository.closeExpiredTrades();
        logger.info("Number of closed trades: {}", closedTrades);
        return tradeRepository.findAllByStatus(Status.MATCHED);
    }

    @Override
    public TradeResponse exerciseTrade(Long id, ExerciseRequest request) {
        Trade trade = tradeRepository.findById(id).orElseThrow(()-> new NotFoundError("Trade not found"));
        if(trade.getStatus()!=Status.MATCHED){throw  new BadRequestError("Trade can only be exercised when it is matched");}
        Instrument instrument = instrumentRepository.findById(request.getInstrumentId()).orElseThrow(()-> new NotFoundError("Instrument not found"));
        if(!Objects.equals(trade.getInstrument().getId(), instrument.getId())){throw new BadRequestError("Instruments were nor match");}
        if(LocalDateTime.now().isAfter(instrument.getMaturityDate())){
            throw new BadRequestError("Date must be before maturity date");
        }
        if(trade.getDirection() == Direction.BUY){
            trade = callExercise(trade);
        }
        else if(trade.getDirection() == Direction.SELL){
            trade = putExercise(trade);
        }else{
            throw new BadRequestError("Error with exercise trade");
        }
        trade.setStatus(Status.EXERCISED);
        List<Trade> matchedTrade = tradeRepository.findMatchedTrade(trade.getPrice(), trade.getQuantity(), trade.getInstrument().getId(), trade.getDirection(), trade.getDeliveryType(), trade.getUnit(), trade.getAccount().getId());
        if(matchedTrade.isEmpty()){
            throw new BadRequestError("Error with matched trades");
        }
        Trade toExerciseMatchedTrade = matchedTrade.getFirst();
        toExerciseMatchedTrade.setStatus(Status.EXERCISED);
        List<Trade> trades = tradeRepository.saveAll(Arrays.asList(trade, toExerciseMatchedTrade));
        return tradeMapper.toResponse(trades.getFirst());
    }

    @Override
    @Transactional
    public void matchingAlgorithmByTrade(Trade trade){
        List<Trade> matchedTrade = tradeRepository.findMatchedTrade(trade.getPrice(), trade.getQuantity(), trade.getInstrument().getId(), trade.getDirection(), trade.getDeliveryType(), trade.getUnit(), trade.getAccount().getId());
        if(matchedTrade.isEmpty()){
            throw new BadRequestError("Error with matched trades");
        }
        matchingTrade(matchedTrade.getFirst());

    }
}
