package com.amandi.weather.client;

import com.amandi.weather.model.WeatherResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Basic version of WeatherApiClient that calls OpenWeatherMap API
 * without custom exception handling.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class WeatherApiClient {

    private final WebClient webClient;

    @Value("${weather.api.base-url}")
    private String baseUrl;

    @Value("${weather.api.key}")
    private String apiKey;

    /**
     * Fetches forecast data for a given city using OpenWeatherMap API.
     *
     * @param city the city name
     * @return WeatherResponse containing forecast details
     */
    public WeatherResponse getForecast(String city) {
        try {
            log.info("Calling OpenWeatherMap API for city: {}", city);

            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("https")
                            .host("api.openweathermap.org")
                            .path("/data/2.5/forecast")
                            .queryParam("q", city)
                            .queryParam("appid", apiKey)
                            .build()
                    )
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError(), response -> {
                        log.warn("Invalid city name or request: {}", city);
                        return response.createException().flatMap(error -> {
                            throw new RuntimeException("City not found or invalid: " + city);
                        });
                    })
                    .onStatus(status -> status.is5xxServerError(), response -> {
                        log.error("Server error from OpenWeatherMap API");
                        return response.createException().flatMap(error -> {
                            throw new RuntimeException("Weather API server error");
                        });
                    })
                    .bodyToMono(WeatherResponse.class)
                    .block();


        } catch (WebClientResponseException e) {
            log.error("Error response from OpenWeatherMap: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve data from weather API: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error occurred while calling weather API", e);
            throw new RuntimeException("Unexpected error occurred", e);
        }
    }
}
