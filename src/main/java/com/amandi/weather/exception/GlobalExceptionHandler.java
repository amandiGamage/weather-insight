package com.amandi.weather.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Global exception handler to handle all exceptions centrally.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles CityNotFoundException thrown by the application.
     *
     * @param ex the exception to handle
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(CityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleCityNotFoundException(CityNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Handles ExternalApiException for errors related to external APIs.
     *
     * @param ex the exception to handle
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(ExternalApiException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleExternalApiException(ExternalApiException ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    /**
     * Handles any generic exception.
     *
     * @param ex the exception to handle
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    }

    /**
     * Builds a consistent error response format.
     *
     * @param status the HTTP status
     * @param message the error message
     * @return ResponseEntity with error details
     */
    private ResponseEntity<Object> buildErrorResponse(HttpStatus status, String message) {
        ErrorResponse errorResponse = new ErrorResponse(status.value(), message);
        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * Error response format used for consistency.
     */
    public static class ErrorResponse {
        private int status;
        private String message;

        public ErrorResponse(int status, String message) {
            this.status = status;
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }
}
