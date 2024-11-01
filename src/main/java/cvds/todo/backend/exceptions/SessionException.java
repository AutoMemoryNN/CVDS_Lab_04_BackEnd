package cvds.todo.backend.exceptions;

/**
 * SessionException es una clase de excepción personalizada que extiende AppException.
 * Sirve como clase base para todas las excepciones relacionadas con el estado de las sesiones,
 * permitiendo el manejo específico de errores en diferentes situaciones de sesión.
 */
public class SessionException extends AppException {

    /**
     * Constructor de SessionException.
     *
     * @param message    Mensaje de error descriptivo.
     * @param statusCode Código de estado HTTP asociado con el error.
     */
    public SessionException(String message, Integer statusCode) {
        super(message, statusCode);
    }

    /**
     * InvalidSessionException se lanza cuando una sesión es inválida o no está autorizada.
     */
    public static class InvalidSessionException extends SessionException {

        /**
         * Constructor de InvalidSessionException.
         *
         * @param sessionId El ID de la sesión inválida.
         */
        public InvalidSessionException(String sessionId) {
            super("Session with ID: " + sessionId + " is invalid or unauthorized.", 401);
        }
    }

    /**
     * ExpiredSessionException se lanza cuando una sesión ha expirado.
     */
    public static class ExpiredSessionException extends SessionException {

        /**
         * Constructor de ExpiredSessionException.
         *
         * @param sessionId El ID de la sesión expirada.
         */
        public ExpiredSessionException(String sessionId) {
            super("Session with ID: " + sessionId + " has expired.", 440);
        }
    }

    /**
     * SessionNotFoundException se lanza cuando no se encuentra una sesión en el sistema.
     */
    public static class SessionNotFoundException extends SessionException {

        /**
         * Constructor de SessionNotFoundException.
         *
         * @param sessionId El ID de la sesión que no se encontró.
         */
        public SessionNotFoundException(String sessionId) {
            super("Session with ID: " + sessionId + " not found.", 404);
        }
    }

    /**
     * SessionConflictException se lanza cuando hay un conflicto de sesión,
     * como cuando ya existe una sesión activa para un usuario.
     */
    public static class SessionConflictException extends SessionException {

        /**
         * Constructor de SessionConflictException.
         *
         * @param userId El ID del usuario que causó el conflicto.
         */
        public SessionConflictException(String userId) {
            super("A session for user ID: " + userId + " is already active.", 409);
        }
    }
}
