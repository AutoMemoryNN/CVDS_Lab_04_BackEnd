package cvds.todo.backend.services;

import cvds.todo.backend.exceptions.SessionException;
import cvds.todo.backend.interfeces.SessionsService;
import cvds.todo.backend.model.UserModel;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servicio para la gestión de sesiones de usuario.
 * Proporciona métodos para crear, renovar, validar e invalidar sesiones, así como para limpiar sesiones expiradas.
 */
@Service
public class SessionService implements SessionsService {

    private static final int COOKIE_MAX_AGE_MIN = 1800; // Duración de la sesión en minutos
    private final Map<String, SessionInfo> activeSessions = new ConcurrentHashMap<>(); // Mapa de sesiones activas

    /**
     * Obtiene el usuario asociado a una sesión específica.
     *
     * @param sessionId El ID de la sesión.
     * @return UserModel El usuario asociado a la sesión.
     * @throws SessionException Si la sesión no existe o ha expirado.
     */
    public UserModel getUserFromSession(String sessionId) throws SessionException {
        SessionInfo sessionInfo = activeSessions.get(sessionId);

        if (sessionInfo == null) {
            throw new SessionException.SessionNotFoundException(sessionId);
        }
        if (System.currentTimeMillis() > sessionInfo.expirationTime) {
            activeSessions.remove(sessionId);
            throw new SessionException.ExpiredSessionException(sessionId);
        }

        return sessionInfo.user;
    }

    /**
     * Crea una nueva sesión para un usuario especificado y devuelve su ID.
     *
     * @param user El usuario para quien se crea la sesión.
     * @return String El ID de la sesión creada.
     * @throws SessionException En caso de error durante la creación de la sesión.
     */
    public String createSessionCookie(UserModel user) throws SessionException {
        String sessionId = UUID.randomUUID().toString();
        long expirationTime = System.currentTimeMillis() + (COOKIE_MAX_AGE_MIN * 60000);

        activeSessions.put(sessionId, new SessionInfo(user, expirationTime));

        return sessionId;
    }

    /**
     * Renueva una sesión extendiendo su tiempo de expiración.
     *
     * @param sessionId El ID de la sesión a renovar.
     * @return boolean Verdadero si la sesión se renovó con éxito, falso si no es válida.
     * @throws SessionException Si la sesión no existe o ha expirado.
     */
    public boolean renewSession(String sessionId) throws SessionException {
        SessionInfo sessionInfo = activeSessions.get(sessionId);

        if (sessionInfo == null || System.currentTimeMillis() > sessionInfo.expirationTime) {
            throw new SessionException.ExpiredSessionException(sessionId);
        }

        sessionInfo.expirationTime = System.currentTimeMillis() + (COOKIE_MAX_AGE_MIN * 60000);
        return true;
    }

    /**
     * Verifica si una sesión está activa.
     *
     * @param sessionId El ID de la sesión a verificar.
     * @return boolean Verdadero si la sesión está activa, falso en caso contrario.
     * @throws SessionException Si la sesión no existe o ha expirado.
     */
    public boolean isSessionActive(String sessionId) throws SessionException {
        UserModel user = getUserFromSession(sessionId);
        if (user == null) {
            throw new SessionException.SessionNotFoundException(sessionId);
        }
        return activeSessions.containsKey(sessionId);
    }

    /**
     * Invalida una sesión eliminándola del sistema.
     *
     * @param sessionId El ID de la sesión a invalidar.
     * @throws SessionException Si la sesión no existe.
     */
    public void invalidateSession(String sessionId) throws SessionException {
        if (activeSessions.remove(sessionId) == null) {
            throw new SessionException.SessionNotFoundException(sessionId);
        }
    }

    /**
     * Limpia las sesiones expiradas del sistema.
     */
    public void cleanExpiredSessions() {
        long currentTime = System.currentTimeMillis();
        activeSessions.entrySet().removeIf(entry ->
                entry.getValue().expirationTime < currentTime);
    }


//        // Create cookie in the Backend
//        return ResponseCookie.from("SESSION_ID", sessionId)
//                .maxAge(COOKIE_MAX_AGE)
//                .path("/")
//                .httpOnly(true)
//                .build();

    /**
     * Clase interna que representa la información de una sesión.
     * Contiene el usuario asociado y el tiempo de expiración de la sesión.
     */
    private static class SessionInfo {
        UserModel user;
        long expirationTime;

        SessionInfo(UserModel user, long expirationTime) {
            this.user = user;
            this.expirationTime = expirationTime;
        }
    }
}
