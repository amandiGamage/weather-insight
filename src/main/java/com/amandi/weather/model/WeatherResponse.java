package com.amandi.weather.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Represents the structure of the response received from OpenWeatherMap API.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {

    @JsonProperty("list")
    private List<ForecastData> forecastList;

    @JsonProperty("city")
    private City city;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ForecastData {
        @JsonProperty("dt_txt")
        private String dateTime;

        @JsonProperty("main")
        private Main main;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Main {
        @JsonProperty("temp")
        private double temperature;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class City {
        @JsonProperty("name")
        private String name;
    }
}
