package com.amandi.weather.service.impl;

import com.amandi.weather.model.WeatherSummary;
import com.amandi.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Service implementation that fetches and analyzes weather data.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    @Override
    public CompletableFuture<WeatherSummary> getWeatherSummary(String city) {
        return null;
    }
}
