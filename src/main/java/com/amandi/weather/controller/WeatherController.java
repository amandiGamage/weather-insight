package com.amandi.weather.controller;

import com.amandi.weather.exception.CityNotFoundException;
import com.amandi.weather.exception.ExternalApiException;
import com.amandi.weather.model.ErrorResponse;
import com.amandi.weather.model.WeatherSummary;
import com.amandi.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * REST controller for weather-related endpoints.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    /**
     * Fetches the weather summary for a given city.
     *
     * @param city the name of the city
     * @return WeatherSummary wrapped in ResponseEntity with appropriate HTTP status
     */
    @GetMapping("/weather")
    public ResponseEntity<Object> getWeatherSummary(
            @RequestParam @NotNull @NotEmpty String city) {
        try {
            log.info("Request to fetch weather summary for city: {}", city);
            WeatherSummary weatherSummary = weatherService.getWeatherSummary(city).join();
            log.info("Successfully fetched weather summary for city: {}", city);
            return ResponseEntity.ok(weatherSummary); // Return WeatherSummary in success case
        } catch (CityNotFoundException e) {
            log.error("City not found: {}", city, e);
            return new ResponseEntity<>(new ErrorResponse("City not found", e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (ExternalApiException e) {
            log.error("External API error while fetching weather data for city: {}", city, e);
            return new ResponseEntity<>(new ErrorResponse("External API error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error("Unexpected error while fetching weather data for city: {}", city, e);
            return new ResponseEntity<>(new ErrorResponse("Unexpected error", "An unexpected error occurred."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
