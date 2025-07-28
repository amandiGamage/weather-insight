package com.amandi.weather.exception;

/**
 * Custom exception thrown when an external API fails or encounters an issue.
 * This is a runtime exception, so it doesn't require explicit handling in the code.
 */
public class ExternalApiException extends RuntimeException {

  // Default constructor
  public ExternalApiException() {
    super("External API error occurred");
  }

  // Constructor with a custom message
  public ExternalApiException(String message) {
    super(message);
  }

  // Constructor that accepts a cause (another exception)
  public ExternalApiException(String message, Throwable cause) {
    super(message, cause);
  }

  // Constructor that accepts a cause and provides a default message
  public ExternalApiException(Throwable cause) {
    super("External API error occurred", cause);
  }
}
