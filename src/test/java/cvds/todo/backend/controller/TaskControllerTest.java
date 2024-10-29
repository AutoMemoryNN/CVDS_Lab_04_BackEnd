package cvds.todo.backend.controller;

import cvds.todo.backend.TodoBackendApplication;
import cvds.todo.backend.model.TaskModel;
import cvds.todo.backend.model.UserModel;
import cvds.todo.backend.services.AuthorizationService;
import cvds.todo.backend.services.SessionService;
import cvds.todo.backend.services.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {TodoBackendApplication.class})
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    @MockBean
    private SessionService sessionService;

    @MockBean
    private AuthorizationService authorizationService;

    private TaskModel task;
    private UserModel user;
    private String sessionToken;

    @BeforeEach
    void setUp() {
        // Initialize test data
        sessionToken = "valid-session-token";

        user = new UserModel();
        user.setId(UUID.randomUUID().toString());
        user.setUsername("testUser");

        task = new TaskModel();
        task.setId(UUID.randomUUID().toString());
        task.setName("Test Task");
        task.setDescription("This is a test task.");
    }

    @Test
    void getAllTasks_WithValidSession_ShouldReturnListOfTasks() throws Exception {
        // Arrange
        when(sessionService.isSessionActive(sessionToken)).thenReturn(true);
        when(sessionService.getUserFromSession(sessionToken)).thenReturn(user);
        when(taskService.getAllTasks(user)).thenReturn(Collections.singletonList(task));

        // Act & Assert
        mockMvc.perform(get("/tasks")
                        .header("Authorization", sessionToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(task.getId()))
                .andExpect(jsonPath("$[0].name").value(task.getName()));
    }

    @Test
    void createTask_WithValidSession_ShouldCreateAndReturnTask() throws Exception {
        // Arrange
        when(sessionService.isSessionActive(sessionToken)).thenReturn(true);
        when(sessionService.getUserFromSession(sessionToken)).thenReturn(user);
        when(taskService.createTask(any(TaskModel.class), eq(user))).thenReturn(task);

        // Act & Assert
        mockMvc.perform(post("/tasks")
                        .header("Authorization", sessionToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(task.getId()))
                .andExpect(jsonPath("$.name").value(task.getName()));
    }

    @Test
    void getTaskById_WithValidSession_ShouldReturnTask() throws Exception {
        // Arrange
        when(sessionService.isSessionActive(sessionToken)).thenReturn(true);
        when(sessionService.getUserFromSession(sessionToken)).thenReturn(user);
        when(taskService.getTaskById(task.getId(), user)).thenReturn(task);

        // Act & Assert
        mockMvc.perform(get("/tasks/{id}", task.getId())
                        .header("Authorization", sessionToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(task.getId()));
    }

    @Test
    void deleteTask_WithValidSession_ShouldDeleteAndReturnTask() throws Exception {
        // Arrange
        when(sessionService.isSessionActive(sessionToken)).thenReturn(true);
        when(sessionService.getUserFromSession(sessionToken)).thenReturn(user);
        when(taskService.deleteTask(task.getId(), user)).thenReturn(task);

        // Act & Assert
        mockMvc.perform(delete("/tasks/{id}", task.getId())
                        .header("Authorization", sessionToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(task.getId()));
    }

    @Test
    void updateTask_WithValidSession_ShouldUpdateAndReturnTask() throws Exception {
        // Arrange
        TaskModel updatedTask = new TaskModel();
        updatedTask.setId(task.getId());
        updatedTask.setName("Updated Task");

        when(sessionService.isSessionActive(sessionToken)).thenReturn(true);
        when(sessionService.getUserFromSession(sessionToken)).thenReturn(user);
        when(taskService.updateTask(eq(task.getId()), any(TaskModel.class), eq(user)))
                .thenReturn(updatedTask);

        // Act & Assert
        mockMvc.perform(patch("/tasks/{id}", task.getId())
                        .header("Authorization", sessionToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedTask.getId()))
                .andExpect(jsonPath("$.name").value(updatedTask.getName()));
    }

    @Test
    void deleteAllTasks_WithValidSession_ShouldDeleteAllAndReturnCount() throws Exception {
        // Arrange
        when(sessionService.isSessionActive(sessionToken)).thenReturn(true);
        when(sessionService.getUserFromSession(sessionToken)).thenReturn(user);
        when(taskService.deleteAllTasks(user)).thenReturn(Collections.singletonList(task));

        // Act & Assert
        mockMvc.perform(delete("/tasks/all")
                        .header("Authorization", sessionToken))
                .andExpect(status().isOk())
                .andExpect(content().string("1 Tasks were deleted successfully"));
    }

    @Test
    void generateTasks_WithValidAdminSession_ShouldGenerateTasks() throws Exception {
        // Arrange
        when(sessionService.isSessionActive(sessionToken)).thenReturn(true);
        when(sessionService.getUserFromSession(sessionToken)).thenReturn(user);
        doNothing().when(authorizationService).adminResource(sessionToken);
        when(taskService.generateExamples(user)).thenReturn(Collections.singletonList(task));

        // Act & Assert
        mockMvc.perform(post("/tasks/gen")
                        .header("Authorization", sessionToken))
                .andExpect(status().isCreated())
                .andExpect(content().string("1 Tasks were generated"));
    }

    @Test
    void getAllTasks_WithInvalidSession_ShouldReturnUnauthorized() throws Exception {
        // Arrange
        String sessionToken = "valid-session-token";
        when(sessionService.isSessionActive(sessionToken)).thenReturn(true);
    }

    @Test
    void checkHealth_ShouldReturnServiceUp() throws Exception {
        mockMvc.perform(get("/tasks/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.message").value("The server is up"));
    }
}