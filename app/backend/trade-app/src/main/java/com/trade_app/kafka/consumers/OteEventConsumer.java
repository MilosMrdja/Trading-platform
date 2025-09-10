package com.trade_app.kafka.consumers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trade_app.kafka.proto.TradeProto;
import com.trade_app.service.AccountStatusServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class OteEventConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AccountStatusServiceImpl accountStatusService;
    private static final Logger logger = LoggerFactory.getLogger(OteEventConsumer.class);

    @KafkaListener(topics = "balance-events-ack", groupId = "event-group")
    // param: map<key = accountId, value = calculated OTE>
    // param type: byte[]
    public void listener(byte[] oteMapBytes){
        try {
            logger.info("Received balance event ote");
            TradeProto.OtePerAccount otePerAccount = TradeProto.OtePerAccount.parseFrom(oteMapBytes);

            Map<Long, Double> receivedMap = otePerAccount.getOteMapMap();

//            receivedMap.forEach((accountId, value) ->
//                   System.out.println(accountId + ": " + value)
//            );
            accountStatusService.synchronizeAccountStatuses(receivedMap);

        } catch (Exception e) {
            logger.error("Problem with receiving trade event ote: ", e);
        }
    }
}
