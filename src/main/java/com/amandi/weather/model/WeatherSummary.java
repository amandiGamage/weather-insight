package com.amandi.weather.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the summarized weather information for a given city.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherSummary {

    /**
     * City name provided as input.
     */
    private String city;

    /**
     * Average temperature (in Celsius) for the past 7 days.
     */
    private double averageTemperature;

    /**
     * Hottest day (date in yyyy-MM-dd format).
     */
    private String hottestDay;

    /**
     * Coldest day (date in yyyy-MM-dd format).
     */
    private String coldestDay;
}

