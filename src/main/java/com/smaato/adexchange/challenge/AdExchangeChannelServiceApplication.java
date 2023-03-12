package com.smaato.adexchange.challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AdExchangeChannelServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdExchangeChannelServiceApplication.class, args);
	}

}
