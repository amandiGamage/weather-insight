package com.amandi.weather.service;

import com.amandi.weather.client.WeatherApiClient;
import com.amandi.weather.exception.CityNotFoundException;
import com.amandi.weather.exception.ExternalApiException;
import com.amandi.weather.model.WeatherSummary;
import com.amandi.weather.model.WeatherResponse;
import com.amandi.weather.service.impl.WeatherServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.cache.CacheManager;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WeatherServiceTest {

    @Mock
    private WeatherApiClient weatherApiClient;

    @Mock
    private CacheManager cacheManager;

    @InjectMocks
    private WeatherServiceImpl weatherService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    public void testGetWeatherSummary_Success() throws Exception {
        // Create a mock response
        WeatherResponse mockResponse = new WeatherResponse();
        WeatherResponse.ForecastData forecastData = new WeatherResponse.ForecastData();

        // Initialize the 'main' object to avoid NullPointerException
        WeatherResponse.Main main = new WeatherResponse.Main();
        main.setTemperature(288.15); // 15.0°C
        forecastData.setMain(main);

        forecastData.setDateTime("2024-11-18 14:00:00");

        // Add forecast data to the response
        mockResponse.setForecastList(Collections.singletonList(forecastData));

        // Mock the API client to return the mock response
        when(weatherApiClient.getForecast("London")).thenReturn(mockResponse);

        // Call the service method and get the future result
        CompletableFuture<WeatherSummary> weatherSummaryFuture = weatherService.getWeatherSummary("London");
        WeatherSummary weatherSummary = weatherSummaryFuture.join(); // Blocking to get result

        // Assertions
        assertNotNull(weatherSummary);
        assertEquals("London", weatherSummary.getCity());
        assertEquals(15.0, weatherSummary.getAverageTemperature(), 0.1); // Assert that the average temp is 15°C
    }

//    @Test
//    public void testGetWeatherSummary_CityNotFound() {
//        // Simulate an error when fetching weather data
//        when(weatherApiClient.getForecast("UnknownCity")).thenReturn(new WeatherResponse());  // Return an empty response
//
//        // Catch the RuntimeException thrown by the service
//        Exception exception = assertThrows(CityNotFoundException.class, () -> {
//            weatherService.getWeatherSummary("UnknownCity").join();  // This should trigger the exception
//        });
//
//        // Assert that the exception message contains the expected part
//        assertTrue(exception.getMessage().contains("No weather data available for city"));
//    }


    @Test
    public void testGetWeatherSummary_ExternalApiError() {
        // Simulate an error when fetching weather data from external API
        when(weatherApiClient.getForecast("London")).thenThrow(new ExternalApiException("External API error"));

        // Catch the ExternalApiException thrown by the service
        Exception exception = assertThrows(ExternalApiException.class, () -> {
            weatherService.getWeatherSummary("London").join();  // This should trigger the exception
        });

        // Check that the exception message matches what you expect
        assertTrue(exception.getMessage().contains("Failed to fetch weather data"));
    }
}
