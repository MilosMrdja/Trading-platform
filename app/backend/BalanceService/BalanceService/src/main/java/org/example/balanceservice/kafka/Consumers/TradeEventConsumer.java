package org.example.balanceservice.kafka.Consumers;

import com.google.protobuf.InvalidProtocolBufferException;
import com.trade_app.kafka.proto.OteProto;
import lombok.RequiredArgsConstructor;
import org.example.balanceservice.kafka.Producers.OteEventProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TradeEventConsumer {

    private final OteEventProducer producer;
    private static final Logger logger = LoggerFactory.getLogger(TradeEventConsumer.class);

    @KafkaListener(topics = "trading-topic", groupId = "event-group")
    public void listener(byte[] tradeListBytes) {
        try {
            logger.info("Received trade events");
            OteProto.TradeList tradeList = OteProto.TradeList.parseFrom(tradeListBytes);
            Map<Long, Double> oteRes = new HashMap<>(); // accountId, ote
            tradeList.getTradesList().forEach(trade -> {
//                System.out.println("Trade ID: " + trade.getTradeId());
//                System.out.println("Instrument ID: " + trade.getInstrumentId());
//                System.out.println("Account ID: " + trade.getAccountId());
//                System.out.println("Price: " + trade.getPrice());
//                System.out.println("Quantity: " + trade.getQuantity());
//                System.out.println("Direction: " + trade.getDirection());
//                System.out.println(getTodayValueForInstrument(trade.getInstrumentId()));

                Double marketPrice = getTodayValueForInstrument(trade.getInstrumentId());
                if (marketPrice == null) {throw new IllegalArgumentException("Market price does not exist");}
                Double tradePrice = trade.getPrice();
                int quantity = trade.getQuantity();
                int directionFactor = trade.getDirection().equals("BUY") ? 1 : -1;

                Double ote = (marketPrice - tradePrice) * quantity * directionFactor;
                if(oteRes.containsKey(trade.getAccountId())){
                    oteRes.put(trade.getAccountId(), oteRes.get(trade.getAccountId()) + ote);
                }else{
                    oteRes.put(trade.getAccountId(), ote);
                }

            });
            producer.sendOte(oteRes);

        }catch (Exception e){
            logger.error("Problem with receiving trade events: ", e);
        }

    }

    public static Double getTodayValueForInstrument(Long instrumentId) {
        LocalDate today = LocalDate.now();

        ClassLoader classLoader = TradeEventConsumer.class.getClassLoader();
        try (InputStream is = classLoader.getResourceAsStream("static/market.csv");
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0].trim());
                LocalDate date = LocalDate.parse(parts[1].trim());
                if (id == instrumentId && date.equals(today)) {
                    return Double.parseDouble(parts[2].trim());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
