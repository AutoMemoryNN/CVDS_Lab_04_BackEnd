package cvds.todo.backend.exceptions;

/**
 * UserException es una clase de excepción personalizada que extiende AppException.
 * Sirve como clase base para todas las excepciones relacionadas con usuarios, permitiendo
 * un manejo específico de errores en situaciones como "no encontrado", valores inválidos o conflictos.
 */
public class UserException extends AppException {

    /**
     * Constructor de UserException.
     *
     * @param message    El mensaje de error.
     * @param statusCode El código de estado HTTP asociado al error.
     */
    public UserException(String message, Integer statusCode) {
        super(message, statusCode);
    }

    /**
     * UserNotFoundException se lanza cuando un usuario no se encuentra en la base de datos.
     */
    public static class UserNotFoundException extends UserException {

        /**
         * Constructor de UserNotFoundException.
         *
         * @param username El nombre de usuario del usuario que no se encontró.
         */
        public UserNotFoundException(String username) {
            super("User with ID: " + username + ", not found in the database.", 404);
        }
    }

    /**
     * UserInvalidValueException se lanza cuando se encuentra un valor inválido en los datos del usuario.
     */
    public static class UserInvalidValueException extends UserException {

        /**
         * Constructor de UserInvalidValueException.
         *
         * @param value El valor inválido encontrado.
         */
        public UserInvalidValueException(String value) {
            super("Invalid value: " + value, 400);
        }
    }

    /**
     * UserConflictException se lanza cuando ocurre un conflicto, como un usuario duplicado.
     */
    public static class UserConflictException extends UserException {

        /**
         * Constructor de UserConflictException.
         *
         * @param username El nombre de usuario que causó el conflicto.
         */
        public UserConflictException(String username) {
            super("User with username: " + username + ", already exists in the database.", 409);
        }
    }
}
