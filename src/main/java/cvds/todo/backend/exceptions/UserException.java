package cvds.todo.backend.exceptions;

/**
 * UserException is a custom exception class that extends AppException.
 * This serves as the base class for all user-related exceptions, allowing
 * specific error handling for different situations like not found, invalid values, or conflicts.
 */
public class UserException extends AppException {

    /**
     * Constructor for UserException.
     *
     * @param message    The error message.
     * @param statusCode The HTTP status code associated with the error.
     */
    public UserException(String message, Integer statusCode) {
        super(message, statusCode);
    }

    /**
     * UserNotFoundException is thrown when a user cannot be found in the database.
     */
    public static class UserNotFoundException extends UserException {

        /**
         * Constructor for UserNotFoundException.
         *
         * @param username The username of the user that was not found.
         */
        public UserNotFoundException(String username) {
            super("User with ID: " + username + ", not found in the database.", 404);
        }
    }

    /**
     * UserInvalidValueException is thrown when an invalid value is encountered.
     */
    public static class UserInvalidValueException extends UserException {

        /**
         * Constructor for UserInvalidValueException.
         *
         * @param value The invalid value encountered.
         */
        public UserInvalidValueException(String value) {
            super("Invalid value: " + value, 400);
        }
    }

    /**
     * UserConflictException is thrown when there is a conflict, such as a duplicate user.
     */
    public static class UserConflictException extends UserException {

        /**
         * Constructor for UserConflictException.
         *
         * @param username The username that caused the conflict.
         */
        public UserConflictException(String username) {
            super("User with username: " + username + ", already exists in the database.", 409);
        }
    }
}