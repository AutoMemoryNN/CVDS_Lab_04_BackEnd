package cvds.todo.backend.services;

import cvds.todo.backend.interfeces.SessionsService;
import cvds.todo.backend.model.UserModel;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionService implements SessionsService {
    private final Map<String, SessionInfo> activeSessions = new ConcurrentHashMap<>();
    private static final int COOKIE_MAX_AGE = 1800;

    private static class SessionInfo {
        UserModel user;
        long expirationTime;

        SessionInfo(UserModel user, long expirationTime) {
            this.user = user;
            this.expirationTime = expirationTime;
        }
    }

    public UserModel getUserFromSession(String sessionId) {
        SessionInfo sessionInfo = activeSessions.get(sessionId);

        if (sessionInfo == null) {
            return null;
        }

        if (System.currentTimeMillis() > sessionInfo.expirationTime) {
            activeSessions.remove(sessionId);
            return null;
        }

        return sessionInfo.user;
    }

    public String createSessionCookie(UserModel user) {
        String sessionId = UUID.randomUUID().toString();
        long expirationTime = System.currentTimeMillis() + (COOKIE_MAX_AGE * 1000);

        activeSessions.put(sessionId, new SessionInfo(user, expirationTime));

        return sessionId;

//        // Create cookie in the Backend
//        return ResponseCookie.from("SESSION_ID", sessionId)
//                .maxAge(COOKIE_MAX_AGE)
//                .path("/")
//                .httpOnly(true)
//                .build();
    }

    public boolean renewSession(String sessionId) {
        SessionInfo sessionInfo = activeSessions.get(sessionId);

        if (sessionInfo == null || System.currentTimeMillis() > sessionInfo.expirationTime) {
            return false;
        }

        sessionInfo.expirationTime = System.currentTimeMillis() + (COOKIE_MAX_AGE * 1000);
        return true;
    }

    public boolean isSessionActive(String sessionId) {
        return activeSessions.containsKey(sessionId);
    }

    public void invalidateSession(String sessionId) {
        activeSessions.remove(sessionId);
    }

    public void cleanExpiredSessions() {
        long currentTime = System.currentTimeMillis();
        activeSessions.entrySet().removeIf(entry ->
                entry.getValue().expirationTime < currentTime);
    }
}
