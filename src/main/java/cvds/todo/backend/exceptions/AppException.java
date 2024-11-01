package cvds.todo.backend.exceptions;

import org.springframework.http.ResponseEntity;
import java.util.Collections;

/**
 * Clase abstracta que representa una excepción personalizada en la aplicación.
 * Esta clase extiende la clase estándar Java Exception y proporciona
 * funcionalidad adicional para manejar códigos de estado HTTP en las respuestas.
 */
public abstract class AppException extends Exception {

    private final Integer statusCode; // Código de estado HTTP asociado con la excepción

    /**
     * Constructor de AppException.
     *
     * @param message    Mensaje de error que describe la excepción.
     * @param statusCode Código de estado HTTP que se usará en la respuesta.
     */
    public AppException(String message, Integer statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    /**
     * Genera un objeto ResponseEntity con el código de estado HTTP y el mensaje de error.
     *
     * @return ResponseEntity que contiene el código de estado y el mensaje de la excepción.
     */
    public ResponseEntity<?> getResponse() {
        return ResponseEntity.status(statusCode).body(Collections.singletonMap("error", this.getMessage()));
    }
}
