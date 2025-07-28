# Weather Insight - Spring Boot Application

## Project Overview
`Weather Insight` is a Spring Boot application that fetches weather data from an external API (e.g., OpenWeatherMap) and provides weather summaries for different cities. The application processes and analyzes the weather data to give insights such as the average temperature, hottest day, and coldest day for the past week.

This project demonstrates key Spring Boot concepts such as asynchronous processing, caching, and error handling.

## Features
- Fetches weather data for a given city.
- Caches the weather data for quick retrieval.
- Asynchronous processing to enhance performance.
- Handles error scenarios (e.g., city not found, external API errors).
- Returns a weather summary including average temperature, hottest day, and coldest day for the past week.

## Technologies Used
- Java 17
- Spring Boot 3.2.x
- Spring Web
- Spring Cache
- Spring Async
- JUnit 5
- Mockito
- Maven
- WireMock (for mocking external API)

## Setup and Installation

### Prerequisites
1. JDK 17 or higher
2. Maven 3.6 or higher
3. IntelliJ IDEA or any other IDE

### Clone the Repository
```bash
git clone https://github.com/amandiGamage/weather-insight.git
cd weather-insight
```

### Configure the Application
1. Ensure you have an API key from OpenWeatherMap. [Sign up here](https://openweathermap.org/appid) to get your API key.
2. Add the following API key to your `application.properties` or `application.yml`:
    ```properties
    weather.api.key={YOUR_API_KEY}
    ```
3. Optionally, configure the test base URL in `application-test.yml` for testing purposes.

### Build the Project
Run the following Maven command to build the project:
```bash
mvn clean install
```

### Running the Application
To run the application locally:
```bash
mvn spring-boot:run
```

To run the application with the test profile:
```bash
mvn spring-boot:run -Dspring.profiles.active=test
```

### Accessing the API
Once the application is running, you can access the weather summary API at:
```
GET http://localhost:8080/weather/summary?city={CITY_NAME}
```
Replace `{CITY_NAME}` with the name of the city you want to query (e.g., London, New York).

### Running Tests
Run the tests with the following Maven command:
```bash
mvn test
```

## Example Test Output

When testing the weather summary for a city (e.g., London), the expected result would be:
```json
{
  "city": "London",
  "averageTemperature": 14.75,
  "hottestDay": "2025-07-28",
  "coldestDay": "2025-07-25"
}
```

## Mocking the External API for Testing
In order to run tests locally without hitting the actual OpenWeatherMap API, you can mock the API calls using **WireMock** or any other mocking library. This can be achieved by configuring the base URL to use a mock server in `application-test.yml`.

Example test setup:
```yaml
weather:
  api:
    baseUrl: http://localhost:8081/mock-weather-api
```

Make sure to configure WireMock or another tool to simulate the OpenWeatherMap API responses.

## Contributions
Feel free to fork this project and submit pull requests. Please make sure your changes are well-tested and well-documented.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
