package com.trade_app.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
// class responsible for creating topics
public class KafkaTopicConfig {
    @Bean
    public NewTopic tradingTopic() {
        return TopicBuilder.name("trading-topic").build();
    }
}
