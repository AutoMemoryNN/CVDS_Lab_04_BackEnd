package cvds.todo.backend.interfeces;

import cvds.todo.backend.model.UserModel;

public interface SessionsService {
    UserModel getUserFromSession(String sessionId);
    String createSessionCookie(UserModel user);
    boolean renewSession(String sessionId);
    boolean isSessionActive(String sessionId);
    void cleanExpiredSessions();
}
