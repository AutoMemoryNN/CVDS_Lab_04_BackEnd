package cvds.todo.backend.exceptions;

/**
 * SessionException is a custom exception class that extends AppException.
 * This serves as the base class for all session-related exceptions,
 * allowing specific error handling for different session states.
 */
public class SessionException extends AppException {

    /**
     * Constructor for SessionException.
     *
     * @param message    The error message.
     * @param statusCode The HTTP status code associated with the error.
     */
    public SessionException(String message, Integer statusCode) {
        super(message, statusCode);
    }

    /**
     * InvalidSessionException is thrown when a session is invalid or unauthorized.
     */
    public static class InvalidSessionException extends SessionException {

        /**
         * Constructor for InvalidSessionException.
         *
         * @param sessionId The ID of the invalid session.
         */
        public InvalidSessionException(String sessionId) {
            super("Session with ID: " + sessionId + " is invalid or unauthorized.", 401);
        }
    }

    /**
     * ExpiredSessionException is thrown when a session has expired.
     */
    public static class ExpiredSessionException extends SessionException {

        /**
         * Constructor for ExpiredSessionException.
         *
         * @param sessionId The ID of the expired session.
         */
        public ExpiredSessionException(String sessionId) {
            super("Session with ID: " + sessionId + " has expired.", 440);
        }
    }

    /**
     * SessionNotFoundException is thrown when a session cannot be found.
     */
    public static class SessionNotFoundException extends SessionException {

        /**
         * Constructor for SessionNotFoundException.
         *
         * @param sessionId The ID of the session that was not found.
         */
        public SessionNotFoundException(String sessionId) {
            super("Session with ID: " + sessionId + " not found.", 404);
        }
    }

    /**
     * SessionConflictException is thrown when there is a conflict, such as a session already active for a user.
     */
    public static class SessionConflictException extends SessionException {

        /**
         * Constructor for SessionConflictException.
         *
         * @param userId The ID of the user that caused the conflict.
         */
        public SessionConflictException(String userId) {
            super("A session for user ID: " + userId + " is already active.", 409);
        }
    }
}