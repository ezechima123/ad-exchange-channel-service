package com.smaato.adexchange.challenge.config;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class ApplicationConfig {

    @Value("${connection.timeout}")
    int connectionTimeOut;

    @Value("${read.timeout}")
    int readTimeOut;

    @Value("${extension.1.fireType}")
    String extension1fireType;

    @Value("${redis.url}")
    String redisUrl;

    @Bean
    public ConcurrentHashMap<Integer, Integer> requestMap() {
        return new ConcurrentHashMap<Integer, Integer>();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofSeconds(connectionTimeOut))
                .setReadTimeout(Duration.ofSeconds(readTimeOut))
                .build();
    }

    @Bean
    public String getFireType() {
        return extension1fireType;
    }

    @Bean

    public OpenAPI customOpenAPI(@Value("${application-description}") String appDesciption,
            @Value("${application-version}") String appVersion) {

        return new OpenAPI()
                .info(new Info()
                        .title("sample application API")
                        .version(appVersion)
                        .description(appDesciption)
                        .termsOfService("http://swagger.io/terms/")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));

    }

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        // config.useSingleServer().setAddress(redisUrl);
        config.useClusterServers().addNodeAddress(redisUrl);
        return Redisson.create(config);
    }

}
