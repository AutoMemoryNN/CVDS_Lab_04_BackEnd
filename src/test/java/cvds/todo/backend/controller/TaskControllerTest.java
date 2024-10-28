//package cvds.todo.backend.controller;
//
//import cvds.todo.backend.exceptions.TaskException;
//import cvds.todo.backend.model.TaskModel;
//import cvds.todo.backend.services.TaskService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Collections;
//import java.util.UUID;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
///**
// * Integration test for {@link TaskController}.
// * This class contains unit tests for controller methods
// * related to task management, ensuring expected behaviors
// * when interacting with the task service.
// */
//@WebMvcTest(TaskController.class)
//class TaskControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private TaskService taskService;
//
//    private TaskModel task;
//
//    /**
//     * Sets up a TaskModel object before each test.
//     * A unique ID is generated, and the name and description fields are initialized.
//     */
//    @BeforeEach
//    void setUp() {
//        task = new TaskModel();
//        task.setId(UUID.randomUUID().toString());
//        task.setName("Test Task");
//        task.setDescription("This is a test task.");
//    }
//
//    /**
//     * Tests retrieval of a list of tasks.
//     * Mocks the task service response and verifies
//     * that the HTTP response is correct and contains expected data.
//     *
//     * @throws Exception if an error occurs during test execution.
//     */
//    @Test
//    void getAllTasks_ShouldReturnListOfTasks() throws Exception {
//        when(taskService.getAllTasks()).thenReturn(Collections.singletonList(task));
//
//        mockMvc.perform(get("/tasks"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$[0].id").value(task.getId()))
//                .andExpect(jsonPath("$[0].name").value(task.getName()))
//                .andExpect(jsonPath("$[0].description").value(task.getDescription()));
//
//        verify(taskService).getAllTasks();
//    }
//
//    /**
//     * Tests creation of a new task.
//     * Mocks the task service response and verifies
//     * that the HTTP response is correct and contains the created task data.
//     *
//     * @throws Exception if an error occurs during test execution.
//     */
//    @Test
//    void createTask_ShouldCreateAndReturnTask() throws Exception {
//        when(taskService.createTask(any(TaskModel.class))).thenReturn(task);
//
//        mockMvc.perform(post("/tasks")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(task)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(task.getId()))
//                .andExpect(jsonPath("$.name").value(task.getName()))
//                .andExpect(jsonPath("$.description").value(task.getDescription()));
//
//        verify(taskService).createTask(any(TaskModel.class));
//    }
//
//    /**
//     * Tests retrieval of a task by its ID.
//     * Mocks the task service response and verifies
//     * that the HTTP response is correct and contains the requested task data.
//     *
//     * @throws Exception if an error occurs during test execution.
//     */
//    @Test
//    void getTaskById_ShouldReturnTask() throws Exception {
//        when(taskService.getTaskById(task.getId())).thenReturn(task);
//
//        mockMvc.perform(get("/tasks/{id}", task.getId()))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id").value(task.getId()))
//                .andExpect(jsonPath("$.name").value(task.getName()))
//                .andExpect(jsonPath("$.description").value(task.getDescription()));
//
//        verify(taskService).getTaskById(task.getId());
//    }
//
//    /**
//     * Tests deletion of a task.
//     * Mocks the task service response and verifies
//     * that the HTTP response is correct and contains the ID of the deleted task.
//     *
//     * @throws Exception if an error occurs during test execution.
//     */
//    @Test
//    void deleteTask_ShouldDeleteAndReturnTask() throws Exception {
//        when(taskService.deleteTask(task.getId())).thenReturn(task);
//
//        mockMvc.perform(delete("/tasks/{id}", task.getId()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(task.getId()));
//
//        verify(taskService).deleteTask(task.getId());
//    }
//
//    /**
//     * Tests updating a task.
//     * Mocks the task service response and verifies
//     * that the HTTP response is correct and contains the updated task data.
//     *
//     * @throws Exception if an error occurs during test execution.
//     */
//    @Test
//    void updateTask_ShouldUpdateAndReturnTask() throws Exception {
//        TaskModel updatedTask = new TaskModel();
//        updatedTask.setId(task.getId());
//        updatedTask.setName("Updated Task");
//        updatedTask.setDescription("Updated description.");
//
//        when(taskService.updateTask(eq(task.getId()), any(TaskModel.class))).thenReturn(updatedTask);
//
//        mockMvc.perform(patch("/tasks/{id}", task.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updatedTask)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(updatedTask.getId()))
//                .andExpect(jsonPath("$.name").value(updatedTask.getName()))
//                .andExpect(jsonPath("$.description").value(updatedTask.getDescription()));
//
//        verify(taskService).updateTask(eq(task.getId()), any(TaskModel.class));
//    }
//
//    /**
//     * Tests retrieval of all tasks when the service throws an exception.
//     * Verifies that the response returns an internal server error status.
//     *
//     * @throws Exception if an error occurs during test execution.
//     */
//    @Test
//    void getAllTasks_WhenServiceThrowsException_ShouldReturnInternalServerError() throws Exception {
//        when(taskService.getAllTasks()).thenThrow(new RuntimeException("Internal Server Error"));
//
//        mockMvc.perform(get("/tasks"))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().string("Internal Server Error"));
//
//        verify(taskService).getAllTasks();
//    }
//
//    /**
//     * Tests retrieval of a task by ID when the task is not found.
//     * Verifies that the response returns a not found status with an error message.
//     *
//     * @throws Exception if an error occurs during test execution.
//     */
//    @Test
//    void getTaskById_WhenTaskNotFound_ShouldReturnNotFound() throws Exception {
//        String nonExistentId = "non-existent-id";
//        when(taskService.getTaskById(nonExistentId))
//                .thenThrow(new TaskException.TaskNotFoundException("Task not found with id: " + nonExistentId));
//
//        mockMvc.perform(get("/tasks/{id}", nonExistentId))
//                .andExpect(status().isNotFound());
//
//        verify(taskService).getTaskById(nonExistentId);
//    }
//
//    /**
//     * Tests creation of a task with invalid data.
//     * Verifies that the response returns a bad request status with an error message.
//     *
//     * @throws Exception if an error occurs during test execution.
//     */
//    @Test
//    void createTask_WhenInvalidData_ShouldReturnBadRequest() throws Exception {
//        TaskModel invalidTask = new TaskModel(); // Assuming this task is invalid due to missing data
//        when(taskService.createTask(any(TaskModel.class)))
//                .thenThrow(new TaskException.TaskInvalidValueException("Invalid task data"));
//
//        mockMvc.perform(post("/tasks")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(invalidTask)))
//                .andExpect(status().isBadRequest());
//
//        verify(taskService).createTask(any(TaskModel.class));
//    }
//
//    /**
//     * Tests updating a task that is not found.
//     * Verifies that the response returns a not found status with an error message.
//     *
//     * @throws Exception if an error occurs during test execution.
//     */
//    @Test
//    void updateTask_WhenTaskNotFound_ShouldReturnNotFound() throws Exception {
//        String nonExistentId = "non-existent-id";
//        TaskModel task = new TaskModel(); // Example task
//        when(taskService.updateTask(eq(nonExistentId), any(TaskModel.class)))
//                .thenThrow(new TaskException.TaskNotFoundException("Task not found with id: " + nonExistentId));
//
//        mockMvc.perform(patch("/tasks/{id}", nonExistentId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(task)))
//                .andExpect(status().isNotFound());
//
//        verify(taskService).updateTask(eq(nonExistentId), any(TaskModel.class));
//    }
//
//    /**
//     * Tests deletion of a task that is not found.
//     * Verifies that the response returns a not found status with an error message.
//     *
//     * @throws Exception if an error occurs during test execution.
//     */
//    @Test
//    void deleteTask_WhenTaskNotFound_ShouldReturnNotFound() throws Exception {
//        String nonExistentId = "non-existent-id";
//        when(taskService.deleteTask(nonExistentId))
//                .thenThrow(new TaskException.TaskNotFoundException("Task not found with id: " + nonExistentId));
//
//        mockMvc.perform(delete("/tasks/{id}", nonExistentId))
//                .andExpect(status().isNotFound());
//
//        verify(taskService).deleteTask(nonExistentId);
//    }
//
//    /**
//     * Tests creation of a task with a missing name field.
//     * Verifies that the response returns a bad request status
//     * when the task name is not provided in the request body.
//     *
//     * @throws Exception if an error occurs during test execution.
//     */
//    @Test
//    void createTask_WhenMissingName_ShouldReturnBadRequest() throws Exception {
//        TaskModel invalidTask = new TaskModel();
//        invalidTask.setDescription("Missing name field");
//
//        mockMvc.perform(post("/tasks")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(invalidTask.toString()))
//                .andExpect(status().isBadRequest());
//
//        verify(taskService, never()).createTask(any(TaskModel.class));
//    }
//
//    /**
//     * Tests creation of a task with a missing name field.
//     * Verifies that the response returns a bad request status
//     * when the task name is not provided in the request body.
//     *
//     * @throws Exception if an error occurs during test execution.
//     */
//    @Test
//    void deleteAllTasks_ShouldDeleteAllAndReturnCount() throws Exception {
//        when(taskService.deleteAllTasks()).thenReturn(Collections.singletonList(task));
//
//        mockMvc.perform(delete("/tasks/all"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("1 Tasks were deleted successfully"));
//
//        verify(taskService).deleteAllTasks();
//    }
//
//    /**
//     * Tests generation of example tasks.
//     * Mocks the task service to generate tasks and verifies
//     * that the HTTP response returns the correct task count.
//     *
//     * @throws Exception if an error occurs during test execution.
//     */
//    @Test
//    void generateTasks_ShouldGenerateTasks() throws Exception {
//        when(taskService.generateExamples()).thenReturn(Collections.singletonList(task));
//
//        mockMvc.perform(post("/tasks/gen"))
//                .andExpect(status().isCreated())
//                .andExpect(content().string("1 Tasks were generated"));
//
//        verify(taskService).generateExamples();
//    }
//
//    /**
//     * Tests the health check endpoint of the TaskController.
//     * Verifies that the HTTP response indicates the service is up.
//     *
//     * @throws Exception if an error occurs during test execution.
//     */
//    @Test
//    void checkHealth_ShouldReturnServiceUp() throws Exception {
//        mockMvc.perform(get("/tasks/health"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value("UP"))
//                .andExpect(jsonPath("$.message").value("The server is up"));
//    }
//
//}