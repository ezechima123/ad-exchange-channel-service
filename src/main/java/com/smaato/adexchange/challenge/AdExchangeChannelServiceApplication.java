package com.smaato.adexchange.challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class AdExchangeChannelServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdExchangeChannelServiceApplication.class, args);
	}

}
