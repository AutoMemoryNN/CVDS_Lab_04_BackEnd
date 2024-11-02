package cvds.todo.backend.service;

import cvds.todo.backend.exceptions.SessionException;
import cvds.todo.backend.model.UserModel;
import cvds.todo.backend.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SessionServiceTest {
    private SessionService sessionService;
    private UserModel user;

    @BeforeEach
    void setUp() {
        sessionService = new SessionService();
        user = new UserModel();
        user.setUsername("testUser");
    }

    @Test
    void testCreateSessionCookie() throws SessionException {
        String sessionId = sessionService.createSessionCookie(user);
        assertNotNull(sessionId);
        assertDoesNotThrow(() -> sessionService.getUserFromSession(sessionId));
    }

    @Test
    void testGetUserFromSession_ValidSession() throws SessionException {
        String sessionId = sessionService.createSessionCookie(user);
        UserModel retrievedUser = sessionService.getUserFromSession(sessionId);
        assertEquals(user, retrievedUser);
    }

    @Test
    void testGetUserFromSession_SessionNotFound() {
        String sessionId = UUID.randomUUID().toString();
        assertThrows(SessionException.SessionNotFoundException.class, () -> sessionService.getUserFromSession(sessionId));
    }

    @Test
    void testRenewSession_ValidSession() throws SessionException {
        String sessionId = sessionService.createSessionCookie(user);
        assertTrue(sessionService.renewSession(sessionId));
    }

    @Test
    void testIsSessionActive_ValidSession() throws SessionException {
        String sessionId = sessionService.createSessionCookie(user);
        assertTrue(sessionService.isSessionActive(sessionId));
    }

    @Test
    void testIsSessionActive_SessionNotFound() {
        String sessionId = UUID.randomUUID().toString();
        assertThrows(SessionException.SessionNotFoundException.class, () -> sessionService.isSessionActive(sessionId));
    }

    @Test
    void testInvalidateSession_ValidSession() throws SessionException {
        String sessionId = sessionService.createSessionCookie(user);
        sessionService.invalidateSession(sessionId);
        assertThrows(SessionException.SessionNotFoundException.class, () -> sessionService.getUserFromSession(sessionId));
    }

    @Test
    void testInvalidateSession_SessionNotFound() {
        String sessionId = UUID.randomUUID().toString();
        assertThrows(SessionException.SessionNotFoundException.class, () -> sessionService.invalidateSession(sessionId));
    }

}