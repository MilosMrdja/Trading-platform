package com.trade_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TradeAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradeAppApplication.class, args);
	}

}
