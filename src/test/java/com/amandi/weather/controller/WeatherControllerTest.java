package com.amandi.weather.controller;

import com.amandi.weather.exception.CityNotFoundException;
import com.amandi.weather.exception.ExternalApiException;
import com.amandi.weather.model.WeatherSummary;
import com.amandi.weather.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit test for WeatherController.
 */
public class WeatherControllerTest {

    private MockMvc mockMvc;

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private WeatherController weatherController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(weatherController).build();
    }

    @Test
    public void testGetWeatherSummary_Success() throws Exception {
        WeatherSummary weatherSummary = WeatherSummary.builder()
                .city("London")
                .averageTemperature(15.5)
                .hottestDay("2024-11-20")
                .coldestDay("2024-11-18")
                .build();

        when(weatherService.getWeatherSummary("London")).thenReturn(java.util.concurrent.CompletableFuture.completedFuture(weatherSummary));

        mockMvc.perform(get("/weather?city=London"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("London"))
                .andExpect(jsonPath("$.averageTemperature").value(15.5))
                .andExpect(jsonPath("$.hottestDay").value("2024-11-20"))
                .andExpect(jsonPath("$.coldestDay").value("2024-11-18"));

        verify(weatherService, times(1)).getWeatherSummary("London");
    }

    @Test
    public void testGetWeatherSummary_CityNotFound() throws Exception {
        when(weatherService.getWeatherSummary("UnknownCity")).thenThrow(new CityNotFoundException("City not found"));

        mockMvc.perform(get("/weather?city=UnknownCity"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("City not found"));

        verify(weatherService, times(1)).getWeatherSummary("UnknownCity");
    }

    @Test
    public void testGetWeatherSummary_ExternalApiError() throws Exception {
        when(weatherService.getWeatherSummary("London")).thenThrow(new ExternalApiException("External API error"));

        mockMvc.perform(get("/weather?city=London"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("External API error"));

        verify(weatherService, times(1)).getWeatherSummary("London");
    }
}
