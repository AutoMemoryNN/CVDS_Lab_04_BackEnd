package cvds.todo.backend.interfeces;

import cvds.todo.backend.exceptions.SessionException;
import cvds.todo.backend.model.UserModel;

/**
 * Interface to manage user sessions in the system.
 */
public interface SessionsService {

    /**
     * Retrieves the user associated with a given session ID.
     *
     * @param sessionId The unique identifier for the session.
     * @return UserModel The user associated with the session, or null if the session is invalid.
     */
    UserModel getUserFromSession(String sessionId) throws SessionException;

    /**
     * Creates a session and returns a new session ID for the specified user.
     *
     * @param user The user for whom the session is created.
     * @return String A unique session ID.
     */
    String createSessionCookie(UserModel user) throws SessionException;

    /**
     * Renews the session to extend its lifetime.
     *
     * @param sessionId The unique identifier for the session to renew.
     * @return boolean True if the session was successfully renewed, false if the session is invalid.
     */
    boolean renewSession(String sessionId) throws SessionException;

    /**
     * Checks if a session is currently active.
     *
     * @param sessionId The unique identifier for the session to check.
     * @return boolean True if the session is active, false otherwise.
     */
    boolean isSessionActive(String sessionId) throws SessionException;

    /**
     * Removes all expired sessions from the system.
     */
    void cleanExpiredSessions() throws SessionException;
}

