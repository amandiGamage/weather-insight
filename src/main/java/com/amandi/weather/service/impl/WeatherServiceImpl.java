package com.amandi.weather.service.impl;

import com.amandi.weather.model.WeatherResponse;
import com.amandi.weather.model.WeatherSummary;
import com.amandi.weather.client.WeatherApiClient;
import com.amandi.weather.exception.CityNotFoundException;
import com.amandi.weather.exception.ExternalApiException;
import com.amandi.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Service implementation that fetches and analyzes weather data.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final WeatherApiClient weatherApiClient;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Asynchronously fetches and processes weather data for the given city.
     * Results are cached for 30 minutes per city.
     */
    @Override
    @Async
    @Cacheable(value = "weather", key = "#city.toLowerCase()")
    public CompletableFuture<WeatherSummary> getWeatherSummary(String city) {
        log.info("Fetching weather summary for city: {}", city);

        WeatherResponse response;

        try {
            response = weatherApiClient.getForecast(city);
        } catch (ExternalApiException e) {
            log.error("External API error occurred while fetching data for city: {}", city, e);
            throw new ExternalApiException("Failed to fetch weather data for city: " + city, e);
        } catch (Exception e) {
            log.error("Unexpected error occurred while fetching weather data for city: {}", city, e);
            throw new ExternalApiException("An error occurred while retrieving weather data for city: " + city, e);
        }

        // Group temperature readings by date
        Map<String, List<Double>> dailyTemperatures = new HashMap<>();
        for (WeatherResponse.ForecastData data : response.getForecastList()) {
            String date = data.getDateTime().split(" ")[0]; // Extract date part
            dailyTemperatures
                    .computeIfAbsent(date, k -> new ArrayList<>())
                    .add(kelvinToCelsius(data.getMain().getTemperature()));
        }

        // Filter to last 7 days
        Map<String, Double> averagePerDay = dailyTemperatures.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .limit(7)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0),
                        (a, b) -> a,
                        LinkedHashMap::new
                ));

        if (averagePerDay.isEmpty()) {
            log.error("No weather data available for city: {}", city);
            throw new CityNotFoundException("No weather data available for city: " + city);
        }

        // Determine hottest and coldest days
        String hottestDay = Collections.max(averagePerDay.entrySet(), Map.Entry.comparingByValue()).getKey();
        String coldestDay = Collections.min(averagePerDay.entrySet(), Map.Entry.comparingByValue()).getKey();
        double avgTemp = averagePerDay.values().stream().mapToDouble(Double::doubleValue).average().orElse(0);

        WeatherSummary summary = WeatherSummary.builder()
                .city(city)
                .averageTemperature(roundToTwoDecimals(avgTemp))
                .hottestDay(hottestDay)
                .coldestDay(coldestDay)
                .build();

        return CompletableFuture.completedFuture(summary);
    }

    /**
     * Converts temperature from Kelvin to Celsius.
     */
    private double kelvinToCelsius(double kelvin) {
        return kelvin - 273.15;
    }

    /**
     * Rounds a double to two decimal places.
     */
    private double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
