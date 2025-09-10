package org.example.balanceservice.kafka.Producers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trade_app.kafka.proto.OteProto;
import lombok.RequiredArgsConstructor;
import org.example.balanceservice.kafka.Consumers.TradeEventConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OteEventProducer {
    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(OteEventProducer.class);

    public void sendOte(Map<Long, Double> list){
        try {
            logger.info("Sending trade event ote...");
            OteProto.OtePerAccount.Builder builder = OteProto.OtePerAccount.newBuilder();

            list.forEach(builder::putOteMap);
            OteProto.OtePerAccount oteMessage = builder.build();

            kafkaTemplate.send("balance-events-ack", oteMessage.toByteArray());
        } catch (Exception e) {
            logger.error("Problem with sending trade event ote: ", e);
        }
    }
}
