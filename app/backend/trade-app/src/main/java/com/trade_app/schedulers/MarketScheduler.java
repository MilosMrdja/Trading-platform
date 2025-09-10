package com.trade_app.schedulers;

import com.trade_app.kafka.producers.TradeEventProducer;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MarketScheduler {

    private final TradeEventProducer tradeEventProducer;
    private static final Logger logger = LoggerFactory.getLogger(MarketScheduler.class);

    @Scheduled(cron = "${scheduler.cron.account-sync: 0 15 12 * * *}")
    public void updateAccountStatuses(){
        logger.info("Updating account statuses");
        tradeEventProducer.sendTrade();
    }
}
