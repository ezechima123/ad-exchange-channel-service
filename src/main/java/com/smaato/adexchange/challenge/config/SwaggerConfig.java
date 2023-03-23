package com.smaato.adexchange.challenge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI(@Value("${application-description}") String appDesciption,
            @Value("${application-version}") String appVersion) {

        Contact contact = new Contact();
        contact.setEmail("chimaemmanuel.ezeamama@gmail.com");
        contact.setName("Chima  Ezeamama");
        contact.url("https://github.com/ezechima123/ad-exchange-channel-service");

        return new OpenAPI()
                .info(new Info()
                        .title("Smaato TechChallenge API")
                        .contact(contact)
                        .version(appVersion)
                        .description(appDesciption)
                        .termsOfService("http://smaato.io/terms/")
                        .license(new License().name("Apache 2.0").url("http://smaato.org")));

    }

}
