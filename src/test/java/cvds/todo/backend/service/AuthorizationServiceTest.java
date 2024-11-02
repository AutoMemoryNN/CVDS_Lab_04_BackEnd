package cvds.todo.backend.service;

import cvds.todo.backend.enums.Role;
import cvds.todo.backend.exceptions.SessionException;
import cvds.todo.backend.model.UserModel;
import cvds.todo.backend.services.AuthorizationService;
import cvds.todo.backend.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class AuthorizationServiceTest {

    @Mock
    private SessionService sessionService;

    @InjectMocks
    private AuthorizationService authorizationService;

    private UserModel adminUser;
    private UserModel nonAdminUser;
    private final String validToken = "validToken";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configura un usuario administrador
        adminUser = new UserModel();
        adminUser.setUsername("admin");
        adminUser.setRole(Role.ROLE_ADMIN.name());

        // Configura un usuario no administrador
        nonAdminUser = new UserModel();
        nonAdminUser.setUsername("user");
        nonAdminUser.setRole(Role.ROLE_USER.name());
    }

    @Test
    void testAdminResource_AccessGrantedForAdmin() throws SessionException {
        when(sessionService.getUserFromSession(validToken)).thenReturn(adminUser);

        assertDoesNotThrow(() -> authorizationService.adminResource(validToken));
    }

    @Test
    void testAdminResource_AccessDeniedForNonAdmin() throws SessionException {
        when(sessionService.getUserFromSession(validToken)).thenReturn(nonAdminUser);

        assertThrows(SessionException.InvalidSessionException.class, () -> authorizationService.adminResource(validToken));
    }

    @Test
    void testAdminResource_SessionNotFound() throws SessionException {
        String invalidToken = "invalidToken";
        when(sessionService.getUserFromSession(invalidToken)).thenThrow(new SessionException.SessionNotFoundException(invalidToken));

        assertThrows(SessionException.SessionNotFoundException.class, () -> authorizationService.adminResource(invalidToken));
    }

    @Test
    void testAdminResource_ExpiredSession() throws SessionException {
        when(sessionService.getUserFromSession(validToken)).thenThrow(new SessionException.ExpiredSessionException(validToken));

        assertThrows(SessionException.ExpiredSessionException.class, () -> authorizationService.adminResource(validToken));
    }
}
