package cvds.todo.backend.controller;

import cvds.todo.backend.model.UserModel;
import cvds.todo.backend.services.AuthorizationService;
import cvds.todo.backend.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthorizationService authorizationService;

    private UserModel user;
    private String token;

    @BeforeEach
    void setUp() {
        token = "valid-session-token";
        user = new UserModel();
        user.setId(UUID.randomUUID().toString());
        user.setUsername("testUser");
    }

    @Test
    void createUserAsUser_ShouldCreateUserSuccessfully() throws Exception {
        when(userService.createUserAsUser(Mockito.any(UserModel.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("The user testUser was created successfully."));
    }

    @Test
    void getUserById_WithValidAdminToken_ShouldReturnUser() throws Exception {
        when(userService.getUserById(user.getId())).thenReturn(user);
        doNothing().when(authorizationService).adminResource(token);

        mockMvc.perform(get("/users/{id}", user.getId())
                        .header("authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.username").value(user.getUsername()));
    }

    @Test
    void getAllUsers_WithValidAdminToken_ShouldReturnUsersList() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(user));
        doNothing().when(authorizationService).adminResource(token);

        mockMvc.perform(get("/users")
                        .header("authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(user.getId()))
                .andExpect(jsonPath("$[0].username").value(user.getUsername()));
    }

    @Test
    void updateUser_WithValidAdminToken_ShouldUpdateUser() throws Exception {
        when(userService.updateUser(eq(user.getId()), Mockito.any(UserModel.class))).thenReturn(user);
        doNothing().when(authorizationService).adminResource(token);

        mockMvc.perform(put("/users/{id}", user.getId())
                        .header("authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.username").value(user.getUsername()));
    }


    private static class UserWithRoles {
        private UserModel user;
        private String roles;

        public UserWithRoles(UserModel user, String roles) {
            this.user = user;
            this.roles = roles;
        }

        public UserModel getUser() { return user; }
        public void setUser(UserModel user) { this.user = user; }
        public String getRoles() { return roles; }
        public void setRoles(String roles) { this.roles = roles; }
    }
}