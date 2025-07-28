package com.amandi.weather.service;

import com.amandi.weather.model.WeatherSummary;

import java.util.concurrent.CompletableFuture;

/**
 * Service interface for fetching and analyzing weather data.
 */
public interface WeatherService {

    /**
     * Retrieves the weather summary for the specified city.
     *
     * @param city the name of the city
     * @return a CompletableFuture containing the weather summary
     */
    CompletableFuture<WeatherSummary> getWeatherSummary(String city);
}
