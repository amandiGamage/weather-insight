package com.amandi.weather.integration;

import com.amandi.weather.client.WeatherApiClient;
import com.amandi.weather.model.WeatherResponse;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Files;
import java.nio.file.Paths;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WeatherApiIntegrationTest {

    private WireMockServer wireMockServer;

    @Autowired
    private WeatherApiClient weatherApiClient;

    @BeforeAll
    void setup() {
        wireMockServer = new WireMockServer(9561);
        wireMockServer.start();
        configureFor("localhost", 9561);
    }

    @AfterAll
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void testMockedWeatherApiResponse() throws Exception {
        String json = Files.readString(Paths.get("src/test/resources/mock/openweather-response.json"));

        stubFor(get(urlMatching("/data/2.5/forecast.*"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(json)));

        // Ensure your WeatherApiClient uses http://localhost:9561 instead of real API
        WeatherResponse response = weatherApiClient.getForecast("London");

        Assertions.assertEquals(2, response.getForecastList().size());
    }
}
