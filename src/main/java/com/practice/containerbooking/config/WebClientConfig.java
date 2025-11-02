package com.practice.containerbooking.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${maersk.api.check-available-url}")
    private String checkAvailableUrl;

    @Bean
    public WebClient maerskWebClient() {
        return WebClient.builder()
            .baseUrl(checkAvailableUrl)
            .build();
    }
}
