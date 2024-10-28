package cvds.todo.backend.exceptions;

import org.springframework.http.ResponseEntity;

import java.util.Collections;

/**
 * Abstract class representing a custom application exception.
 * This class extends the standard Java Exception class and provides
 * additional functionality for handling HTTP response status codes.
 */
public abstract class AppException extends Exception {

    private final Integer statusCode;

    public AppException(String message, Integer statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    /**
     * Generates a ResponseEntity object with the HTTP status code and message.
     *
     * @return ResponseEntity containing the status code and exception message.
     */
    public ResponseEntity<?> getResponse() {
        return ResponseEntity.status(statusCode).body(Collections.singletonMap("Error", this.getMessage()));
    }
}
