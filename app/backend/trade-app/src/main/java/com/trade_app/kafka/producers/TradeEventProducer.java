package com.trade_app.kafka.producers;

import com.trade_app.domain.entity.Trade;
import com.trade_app.kafka.consumers.OteEventConsumer;
import com.trade_app.kafka.proto.TradeProto;
import com.trade_app.service.TradeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TradeEventProducer {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final TradeServiceImpl tradeService;
    private static final Logger logger = LoggerFactory.getLogger(TradeEventProducer.class);

    public Boolean sendTrade(){
        try{
            logger.info("Sending trade event...");
            List<Trade> trades = tradeService.fetchOpenTrades();

            TradeProto.TradeList.Builder listBuilder = TradeProto.TradeList.newBuilder();
            for (Trade trade : trades) {
                TradeProto.Trade protoTrade = TradeProto.Trade.newBuilder()
                        .setTradeId(trade.getId())
                        .setAccountId(trade.getAccount().getId())
                        .setInstrumentId(trade.getInstrument().getId())
                        .setPrice(trade.getPrice())
                        .setQuantity(trade.getQuantity())
                        .setDirection(trade.getDirection().name())
                        .build();

                listBuilder.addTrades(protoTrade);
            }
            TradeProto.TradeList tradeList = listBuilder.build();

            kafkaTemplate.send("trading-topic", tradeList.toByteArray());
            return true;
        }catch (Exception e){
            logger.error("Problem with sending trade events: ", e);
            return false;
        }

    }
}
