package com.smaato.adexchange.challenge.config;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationConfig {

    @Value("${connection.timeout}")
    int connectionTimeOut;

    @Value("${read.timeout}")
    int readTimeOut;

    @Bean
    public ConcurrentHashMap<Integer, String> requestMap() {
        return new ConcurrentHashMap<Integer, String>();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofSeconds(connectionTimeOut))
                .setReadTimeout(Duration.ofSeconds(readTimeOut))
                .build();
    }
}
