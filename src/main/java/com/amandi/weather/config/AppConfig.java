package com.amandi.weather.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration class for beans like WebClient.
 */
@Configuration
public class AppConfig {

    /**
     * WebClient bean for making HTTP requests.
     */
    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}
