package com.amandi.weather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main class for the Weather Insight Spring Boot application.
 * Enables caching and asynchronous processing.
 */
@SpringBootApplication
@EnableCaching
@EnableAsync
public class WeatherInsightApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherInsightApplication.class, args);
    }
}
