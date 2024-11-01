package cvds.todo.backend.services;

import cvds.todo.backend.enums.Role;
import cvds.todo.backend.exceptions.SessionException;
import cvds.todo.backend.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio para la gestión de la autorización de recursos según el rol del usuario.
 * Proporciona métodos para validar el acceso a recursos específicos en función del rol de usuario.
 */
@Service
public class AuthorizationService {

    @Autowired
    SessionService sessionService;

    /**
     * Verifica si el usuario autenticado tiene el rol de administrador antes de acceder a un recurso.
     * Lanza una excepción si el usuario no tiene los permisos necesarios.
     *
     * @param token Token de sesión del usuario.
     * @throws SessionException Si el usuario no tiene permisos de administrador o si la sesión es inválida.
     */
    public void adminResource(String token) throws SessionException {
        UserModel user = sessionService.getUserFromSession(token);
        if (!user.getRoles().equals(Role.ROLE_ADMIN.name())) {
            throw new SessionException.InvalidSessionException("No access");
        }
    }
}
