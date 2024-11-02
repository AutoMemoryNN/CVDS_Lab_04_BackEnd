package cvds.todo.backend.service;

import cvds.todo.backend.enums.Role;
import cvds.todo.backend.exceptions.UserException;
import cvds.todo.backend.model.UserModel;
import cvds.todo.backend.repository.UserRepository;
import cvds.todo.backend.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUserAsUser_Success() throws UserException {
        UserModel user = new UserModel();
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setRole(Role.ROLE_USER.name());

        when(userRepository.findByUsername(any())).thenReturn(null);
        when(userRepository.findByEmail(any())).thenReturn(null);
        when(userRepository.save(any(UserModel.class))).thenReturn(user);

        UserModel createdUser = userService.createUserAsUser(user);

        assertNotNull(createdUser.getId());
        assertEquals("testUser", createdUser.getUsername());
        assertNotNull(createdUser.getPassword());
        assertTrue(new BCryptPasswordEncoder().matches("password123", createdUser.getPassword()));
    }

    @Test
    void testCreateUserAsUser_UserAlreadyExists() {
        UserModel user = new UserModel();
        user.setUsername("existingUser");
        user.setEmail("existing@example.com");
        user.setPassword("password123");
        user.setRole(Role.ROLE_USER.name());

        when(userRepository.findByUsername("existingUser")).thenReturn(user);

        UserException.UserConflictException exception = assertThrows(UserException.UserConflictException.class, () -> {
            userService.createUserAsUser(user);
        });

        assertEquals("User with username: existingUser, already exists in the database.", exception.getMessage());
    }

    @Test
    void testUpdateUser_Success() throws UserException {
        UserModel existingUser = new UserModel();
        existingUser.setId("1234");
        existingUser.setUsername("oldUsername");
        existingUser.setEmail("old@example.com");
        existingUser.setPassword(new BCryptPasswordEncoder().encode("oldPassword"));
        existingUser.setRole(Role.ROLE_USER.name());

        UserModel updatedUser = new UserModel();
        updatedUser.setUsername("newUsername");
        updatedUser.setEmail("new@example.com");
        updatedUser.setPassword("newPassword");

        when(userRepository.findById("1234")).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(UserModel.class))).thenReturn(existingUser);

        UserModel result = userService.updateUser("1234", updatedUser);

        assertEquals("newUsername", result.getUsername());
        assertEquals("new@example.com", result.getEmail());
        assertTrue(new BCryptPasswordEncoder().matches("newPassword", result.getPassword()));
    }

    @Test
    void testUpdateUser_NotFound() {
        UserModel user = new UserModel();
        user.setUsername("newUsername");

        when(userRepository.findById("1234")).thenReturn(Optional.empty());

        UserException.UserNotFoundException exception = assertThrows(UserException.UserNotFoundException.class, () -> {
            userService.updateUser("1234", user);
        });

        assertEquals("User with ID: newUsername, not found in the database.", exception.getMessage());
    }

    @Test
    void testLoginUser_Success() throws UserException {
        UserModel user = new UserModel();
        user.setUsername("testUser");
        user.setPassword(new BCryptPasswordEncoder().encode("password123"));

        when(userRepository.findByUsername("testUser")).thenReturn(user);

        UserModel loggedUser = userService.loginUser("testUser", "password123");

        assertNotNull(loggedUser);
        assertEquals("testUser", loggedUser.getUsername());
    }

    @Test
    void testLoginUser_InvalidPassword() {
        UserModel user = new UserModel();
        user.setUsername("testUser");
        user.setPassword(new BCryptPasswordEncoder().encode("password123"));

        when(userRepository.findByUsername("testUser")).thenReturn(user);

        UserException.UserInvalidValueException exception = assertThrows(UserException.UserInvalidValueException.class, () -> {
            userService.loginUser("testUser", "wrongPassword");
        });

        assertEquals("Invalid value: Invalid credentials", exception.getMessage());
    }

    @Test
    void testLoginUser_UserNotFound() {
        when(userRepository.findByUsername("nonexistentUser")).thenReturn(null);

        UserException.UserNotFoundException exception = assertThrows(UserException.UserNotFoundException.class, () -> {
            userService.loginUser("nonexistentUser", "password123");
        });

        assertEquals("User with ID: nonexistentUser, not found in the database.", exception.getMessage());
    }
}
