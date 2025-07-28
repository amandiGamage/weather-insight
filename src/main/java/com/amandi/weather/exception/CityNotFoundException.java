package com.amandi.weather.exception;

/**
 * Custom exception thrown when a city is not found.
 * This is a runtime exception, so it doesn't require explicit handling in the code.
 */
public class CityNotFoundException extends RuntimeException {

  // Default constructor
  public CityNotFoundException() {
    super("City not found");
  }

  // Constructor with a custom message
  public CityNotFoundException(String message) {
    super(message);
  }

  // Constructor that accepts a cause
  public CityNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  // Constructor that accepts a cause and provides a default message
  public CityNotFoundException(Throwable cause) {
    super("City not found", cause);
  }
}
