package com.amandi.weather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class for the Weather Insight Spring Boot application.
 * Enables caching and asynchronous processing.
 */
@SpringBootApplication
public class WeatherInsightApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherInsightApplication.class, args);
    }
}
