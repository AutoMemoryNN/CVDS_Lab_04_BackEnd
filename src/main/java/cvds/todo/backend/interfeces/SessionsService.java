package cvds.todo.backend.interfeces;

import cvds.todo.backend.exceptions.SessionException;
import cvds.todo.backend.model.UserModel;

/**
 * Interfaz para gestionar las sesiones de usuario en el sistema.
 * Define métodos para crear, renovar, validar y limpiar sesiones de usuario.
 */
public interface SessionsService {

    /**
     * Recupera el usuario asociado a un ID de sesión específico.
     *
     * @param sessionId El identificador único de la sesión.
     * @return UserModel El usuario asociado a la sesión, o null si la sesión no es válida.
     * @throws SessionException Si hay un problema con la sesión.
     */
    UserModel getUserFromSession(String sessionId) throws SessionException;

    /**
     * Crea una sesión y devuelve un nuevo ID de sesión para el usuario especificado.
     *
     * @param user El usuario para el que se crea la sesión.
     * @return String Un ID de sesión único.
     * @throws SessionException Si ocurre un problema durante la creación de la sesión.
     */
    String createSessionCookie(UserModel user) throws SessionException;

    /**
     * Renueva la sesión para extender su tiempo de vida.
     *
     * @param sessionId El identificador único de la sesión a renovar.
     * @return boolean True si la sesión se renovó correctamente; false si la sesión no es válida.
     * @throws SessionException Si ocurre un problema durante la renovación de la sesión.
     */
    boolean renewSession(String sessionId) throws SessionException;

    /**
     * Verifica si una sesión está actualmente activa.
     *
     * @param sessionId El identificador único de la sesión a verificar.
     * @return boolean True si la sesión está activa; false en caso contrario.
     * @throws SessionException Si ocurre un problema al verificar la sesión.
     */
    boolean isSessionActive(String sessionId) throws SessionException;

    /**
     * Elimina todas las sesiones expiradas del sistema.
     *
     * @throws SessionException Si ocurre un problema al limpiar las sesiones expiradas.
     */
    void cleanExpiredSessions() throws SessionException;
}
