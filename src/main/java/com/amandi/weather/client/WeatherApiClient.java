package com.amandi.weather.client;

import com.amandi.weather.exception.CityNotFoundException;
import com.amandi.weather.exception.ExternalApiException;
import com.amandi.weather.model.WeatherResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Utility class that interacts with the OpenWeatherMap API.
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
                        log.warn("City not found: {}", city);
                        return response.bodyToMono(String.class)
                                .map(body -> new CityNotFoundException("City not found: " + city));
                    })
                    .onStatus(status -> status.is5xxServerError(), response -> {
                        log.error("Server error from OpenWeatherMap API");
                        return response.bodyToMono(String.class)
                                .map(body -> new ExternalApiException("External API server error"));
                    })
                    .bodyToMono(WeatherResponse.class)
                    .block(); // Blocking here is okay since the method is called inside an async method

        } catch (WebClientResponseException e) {
            throw new ExternalApiException("Failed to retrieve data from OpenWeatherMap API", e);
        } catch (Exception e) {
            log.error("Unexpected error occurred while calling weather API", e);
            throw new ExternalApiException("Unexpected error occurred", e);
        }
    }
}
