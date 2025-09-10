package com.trade_app.service;

import com.trade_app.common.exception.BadRequestError;
import com.trade_app.common.exception.NotFoundError;
import com.trade_app.common.shared.PageResponse;
import com.trade_app.domain.entity.Instrument;
import com.trade_app.dto.request.InstrumentRequest;
import com.trade_app.dto.response.InstrumentResponse;
import com.trade_app.mapper.InstrumentMapper;
import com.trade_app.repository.InstrumentRepository;
import com.trade_app.repository.TradeRepository;
import com.trade_app.service.interfaces.InstrumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstrumentServiceImpl implements InstrumentService {
    private final InstrumentRepository instrumentRepository;
    private final InstrumentMapper instrumentMapper;
    private final TradeRepository tradeRepository;

    @Override
    public PageResponse<InstrumentResponse> getAll(Pageable pageable) {
        Page<Instrument> page = instrumentRepository.findAll(pageable);

        List<InstrumentResponse> content = page.getContent()
                .stream()
                .map(instrumentMapper::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @Override
    public InstrumentResponse getById(Long id){
        return instrumentRepository.findById(id).map(instrumentMapper::toResponse).orElseThrow(()-> new NotFoundError("Instrument not found"));
    }

    @Override
    public InstrumentResponse create(InstrumentRequest request){
        if(request.getCode() == null){throw new BadRequestError("Instrument code is required");}
        if(request.getMaturityDate() == null){throw new BadRequestError("Instrument maturity date is required");}
        if(request.getMaturityDate().isBefore(LocalDateTime.now())){throw new BadRequestError("Instrument maturity date must be in future");}
        if(instrumentRepository.existsInstrumentByCode(request.getCode())){throw new BadRequestError("Instrument code already exists");}
        Instrument instrument = instrumentMapper.toEntity(request);

        return instrumentMapper.toResponse(instrumentRepository.save(instrument));
    }

    @Override
    public InstrumentResponse update(Long id, InstrumentRequest request){
        if(request.getCode()!= null && instrumentRepository.existsInstrumentByCode(request.getCode())){throw new BadRequestError("Instrument code already exists");}
        if(request.getMaturityDate().isBefore(LocalDateTime.now())){throw new BadRequestError("Instrument maturity date must be in future");}

        Instrument instrument = instrumentRepository.findById(id).orElseThrow(()-> new NotFoundError("Instrument not found"));

        instrument.setCode(request.getCode() == null ? instrument.getCode() : request.getCode());
        instrument.setMaturityDate(request.getMaturityDate() == null ? instrument.getMaturityDate() : request.getMaturityDate());
        return instrumentMapper.toResponse(instrumentRepository.save(instrument));
    }

    @Override
    public void delete(Long id){
        Instrument instrument = instrumentRepository.findById(id).orElseThrow(()-> new NotFoundError("Instrument not found"));
        if(tradeRepository.existsByInstrument(instrument)){throw new BadRequestError("Can not delete the instrument because it is existed in some trade");}
        instrumentRepository.delete(instrument);
    }
}
