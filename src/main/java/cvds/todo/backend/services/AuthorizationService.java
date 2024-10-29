package cvds.todo.backend.services;

import cvds.todo.backend.enums.Role;
import cvds.todo.backend.exceptions.SessionException;
import cvds.todo.backend.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {
    @Autowired
    SessionService sessionService;

    public void adminResource(String token) throws SessionException {
        UserModel user = sessionService.getUserFromSession(token);
        if (!user.getRoles().contains(Role.ROLE_ADMIN.name())) {
            throw new SessionException.InvalidSessionException("No access");
        }
    }
}
