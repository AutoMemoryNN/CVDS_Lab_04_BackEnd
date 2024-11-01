package cvds.todo.backend.service;

import cvds.todo.backend.enums.Difficulty;
import cvds.todo.backend.exceptions.AppException;
import cvds.todo.backend.exceptions.TaskException;
import cvds.todo.backend.model.UserModel;
import cvds.todo.backend.repository.TaskRepository;
import cvds.todo.backend.model.TaskModel;
import cvds.todo.backend.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class TaskServiceTest {

    // Define reusable constants for cleaner and more maintainable code
    private static final String EXISTING_TASK_ID = UUID.randomUUID().toString();
    private static final String NON_EXISTING_TASK_ID = "1";
    private static final String TASK_NAME = "Task 1";
    private static final String TASK_DESCRIPTION = "Description 1";
    @Mock
    private TaskRepository taskRepository;
    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllTasks_ShouldReturnAllTasks() throws AppException {
        // Arrange
        UserModel user = this.genUserModel(UUID.randomUUID().toString(), "testUser");
        List<TaskModel> expectedTasks = Arrays.asList(
                this.genTaskModel("1", "Task 1", "Description 1", false, user),
                this.genTaskModel("2", "Task 2", "Description 2", true, user)
        );
        when(taskRepository.findByOwnerIdsContaining(user.getId())).thenReturn(expectedTasks);

        List<TaskModel> actualTasks = taskService.getAllTasks(user);

        assertEquals(expectedTasks, actualTasks, "Expected tasks do not match actual tasks.");
        verify(taskRepository, times(1)).findByOwnerIdsContaining(user.getId());
    }

    /**
     * Test case to verify retrieval of an existing task by ID.
     * This method tests the getTaskById() method of TaskService.
     *
     * @throws AppException if there is an error retrieving the task
     */
    @Test
    void getTaskById_ExistingTask_ShouldReturnTask() throws AppException {
        UserModel user = this.genUserModel(UUID.randomUUID().toString(), "testUser");
        TaskModel expectedTask = this.genTaskModel(EXISTING_TASK_ID, TASK_NAME, TASK_DESCRIPTION, false, user);
        when(taskRepository.findFirstByOwnerIdsContainingAndId(user.getId(), EXISTING_TASK_ID)).thenReturn(expectedTask);

        TaskModel actualTask = taskService.getTaskById(EXISTING_TASK_ID, user);

        assertEquals(expectedTask, actualTask, "The returned task should match the expected task.");
        verify(taskRepository, times(1)).findFirstByOwnerIdsContainingAndId(user.getId(), EXISTING_TASK_ID);
    }

//change this to works using the user owner logic

    /**
     * Test case to verify exception thrown when trying to retrieve a non-existing task by ID.
     * This method tests the getTaskById() method of TaskService.
     */
    @Test
    void getTaskById_NonExistingTask_ShouldThrowTaskNotFoundException() throws AppException {
        UserModel user = this.genUserModel(UUID.randomUUID().toString(), "testUser");
        when(taskRepository.findFirstByOwnerIdsContainingAndId(user.getId(), NON_EXISTING_TASK_ID)).thenReturn(null);

        TaskException.TaskNotFoundException thrownException = assertThrows(
                TaskException.TaskNotFoundException.class,
                () -> taskService.getTaskById(NON_EXISTING_TASK_ID, user)
        );

        assertEquals("Task: " + NON_EXISTING_TASK_ID + ", not found in the database.", thrownException.getMessage());
        verify(taskRepository, times(1)).findFirstByOwnerIdsContainingAndId(user.getId(), NON_EXISTING_TASK_ID);
    }


    /**
     * Test case to verify the creation of a new task.
     * This method tests the createTask() method of TaskService.
     *
     * @throws AppException if there is an error creating the task
     */
    @Test
    void createTask_ShouldReturnCreatedTask() throws AppException {
        UserModel user = this.genUserModel(UUID.randomUUID().toString(), "testUser");
        TaskModel taskToCreate = this.genTaskModel(null, TASK_NAME, TASK_DESCRIPTION, false, user);
        TaskModel expectedTask = this.genTaskModel(UUID.randomUUID().toString(), TASK_NAME, TASK_DESCRIPTION, false, user);
        when(taskRepository.insert(any(TaskModel.class))).thenReturn(expectedTask);

        TaskModel createdTask = taskService.createTask(taskToCreate, user);

        assertNotNull(createdTask.getId(), "Created task should have a generated ID.");
        assertEquals(expectedTask.getName(), createdTask.getName(), "Task name should match.");
        assertEquals(expectedTask.getDescription(), createdTask.getDescription(), "Task description should match.");
        assertEquals(expectedTask.isDone(), createdTask.isDone(), "Task status should match.");
        verify(taskRepository, times(1)).insert(any(TaskModel.class));
    }


    /**
     * Test case to verify the update of an existing task.
     * This method tests the updateTask() method of TaskService.
     *
     * @throws AppException if there is an error updating the task
     */
    @Test
    void updateTask_ExistingTask_ShouldReturnUpdatedTask() throws AppException {
        UserModel user = this.genUserModel(UUID.randomUUID().toString(), "testUser");
        TaskModel existingTask = this.genTaskModel(EXISTING_TASK_ID, "Old Task", "Old Description", false, user);
        TaskModel updatedTask = this.genTaskModel(EXISTING_TASK_ID, "Updated Task", "Updated Description", true, user);

        when(taskRepository.findById(EXISTING_TASK_ID)).thenReturn(Optional.of(existingTask));
        when(taskRepository.findFirstByOwnerIdsContainingAndId(user.getId(), EXISTING_TASK_ID)).thenReturn(existingTask);
        when(taskRepository.save(any(TaskModel.class))).thenReturn(updatedTask);

        TaskModel result = taskService.updateTask(EXISTING_TASK_ID, updatedTask, user);

        assertAll("Update task verification",
                () -> assertEquals(updatedTask.getId(), result.getId(), "Task ID should match"),
                () -> assertEquals(updatedTask.getName(), result.getName(), "Task name should be updated"),
                () -> assertEquals(updatedTask.getDescription(), result.getDescription(), "Task description should be updated"),
                () -> assertEquals(updatedTask.isDone(), result.isDone(), "Task status should be updated")
        );
    }


    /**
     * Test case to verify exception thrown when trying to update a non-existing task.
     * This method tests the updateTask() method of TaskService.
     */
    @Test
    void updateTask_NonExistingTask_ShouldThrowTaskNotFoundException() throws AppException {
        UserModel user = this.genUserModel(UUID.randomUUID().toString(), "testUser");
        TaskModel updatedTask = this.genTaskModel(NON_EXISTING_TASK_ID, "Updated Task", "Updated Description", true, user);
        when(taskRepository.findFirstByOwnerIdsContainingAndId(user.getId(), NON_EXISTING_TASK_ID)).thenReturn(null);

        TaskException.TaskNotFoundException thrownException = assertThrows(
                TaskException.TaskNotFoundException.class,
                () -> taskService.updateTask(NON_EXISTING_TASK_ID, updatedTask, user)
        );

        assertEquals("Task: " + NON_EXISTING_TASK_ID + ", not found in the database.", thrownException.getMessage());
    }


    /**
     * Test case to verify the deletion of an existing task.
     * This method tests the deleteTask() method of TaskService.
     *
     * @throws AppException if there is an error deleting the task
     */
    @Test
    void deleteTask_ExistingTask_ShouldDeleteTask() throws AppException {
        UserModel user = this.genUserModel(UUID.randomUUID().toString(), "testUser");
        TaskModel taskToDelete = this.genTaskModel(EXISTING_TASK_ID, TASK_NAME, TASK_DESCRIPTION, false, user);
        when(taskRepository.findFirstByOwnerIdsContainingAndId(user.getId(), EXISTING_TASK_ID)).thenReturn(taskToDelete);

        TaskModel deletedTask = taskService.deleteTask(EXISTING_TASK_ID, user);

        assertEquals(taskToDelete, deletedTask, "Deleted task should match the existing task.");
    }

    /**
     * Test case to verify exception thrown when trying to delete a non-existing task.
     * This method tests the deleteTask() method of TaskService.
     */
    @Test
    void deleteTask_NonExistingTask_ShouldThrowTaskNotFoundException() throws AppException {
        UserModel user = this.genUserModel(UUID.randomUUID().toString(), "testUser");
        when(taskRepository.findFirstByOwnerIdsContainingAndId(user.getId(), NON_EXISTING_TASK_ID)).thenReturn(null);

        TaskException.TaskNotFoundException thrownException = assertThrows(
                TaskException.TaskNotFoundException.class,
                () -> taskService.deleteTask(NON_EXISTING_TASK_ID, user)
        );

        assertEquals("Task: " + NON_EXISTING_TASK_ID + ", not found in the database.", thrownException.getMessage());
        verify(taskRepository, times(1)).findFirstByOwnerIdsContainingAndId(user.getId(), NON_EXISTING_TASK_ID);
    }


    /**
     * Test case to verify the generation of example tasks.
     * This method tests the generateExamples() method of TaskService.
     *
     * @throws AppException if there is an error generating tasks
     */
    void generateExamples_ShouldCreateRandomTasks() throws AppException {
        UserModel user = this.genUserModel(UUID.randomUUID().toString(), "testUser");
        when(taskRepository.insert(any(List.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<TaskModel> generatedTasks = taskService.generateExamples(user);

        assertNotNull(generatedTasks, "Generated tasks should not be null.");
        assertTrue(generatedTasks.size() >= 100 && generatedTasks.size() <= 1000, "Number of generated tasks should be between 100 and 1000.");
    }


//    /**
//     * Tests the deletion of all existing tasks in the repository.
//     * <p>
//     * This test verifies that when the deleteAllTasks() method is called,
//     * it retrieves all existing tasks from the repository, verifies that
//     * each task can be found, and then deletes each task. Finally, it checks
//     * that the deleted tasks match the originally retrieved tasks.
//     *
//     * @throws AppException if an application error occurs during the deletion process.
//     */
//    @Test
//    void deleteAllTasks_ShouldHandleVariousScenarios() throws AppException {
//        UserModel user = this.genUserModel(UUID.randomUUID().toString(), "testUser");
//        List<TaskModel> existingTasks = Arrays.asList(
//                genTaskModel("1", "Task 1", "Description 1", false, user),
//                genTaskModel("2", "Task 2", "Description 2", true, user),
//                genTaskModel("3", "Task 3", "Description 3", false, user)
//        );
//
//        when(taskRepository.findByOwnerIdsContaining(user.getId())).thenReturn(existingTasks);
//        when(taskRepository.deleteByIdAndOwnerIdsContaining(eq(user.getId()), anyString())).thenAnswer(invocation -> {
//            String taskId = invocation.getArgument(1);
//            return existingTasks.stream().filter(task -> task.getId().equals(taskId)).findFirst().orElse(null);
//        });
//
//        List<TaskModel> deletedTasks = taskService.deleteAllTasks(user);
//
//        assertEquals(existingTasks, deletedTasks, "Deleted tasks should match existing tasks");
//        assertEquals(3, deletedTasks.size(), "Should have deleted 3 tasks");
//
//        when(taskRepository.findByOwnerIdsContaining(user.getId())).thenReturn(List.of());
//
//        List<TaskModel> emptyDeletedTasks = taskService.deleteAllTasks(user);
//
//        assertTrue(emptyDeletedTasks.isEmpty(), "Should return empty list when no tasks exist");
//
//        when(taskRepository.findByOwnerIdsContaining(user.getId())).thenThrow(new RuntimeException("Database error"));
//
//        assertThrows(java.lang.RuntimeException.class, () -> taskService.deleteAllTasks(user),
//                "Should throw AppException when repository operation fails");
//    }

    @Test
    void generateExamples_ShouldCreateTasksWithValidProperties() throws AppException {
        UserModel user = this.genUserModel(UUID.randomUUID().toString(), "testUser");
        when(taskRepository.insert(any(List.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<TaskModel> generatedTasks = taskService.generateExamples(user);

        assertNotNull(generatedTasks, "Generated tasks should not be null.");
        assertTrue(generatedTasks.size() >= 100 && generatedTasks.size() <= 1000, "Number of generated tasks should be between 100 and 1000.");
        for (TaskModel task : generatedTasks) {
            assertNotNull(task.getId(), "Task ID should not be null.");
            assertNotNull(task.getName(), "Task name should not be null.");
            assertNotNull(task.getDescription(), "Task description should not be null.");
            assertNotNull(task.getCreatedAt(), "Task created at should not be null.");
            assertNotNull(task.getUpdatedAt(), "Task updated at should not be null.");
            assertNotNull(task.getOwnerIds(), "Task owner IDs should not be null.");
            assertFalse(task.getOwnerIds().isEmpty(), "Task owner IDs should not be empty.");
            assertTrue(task.getPriority() >= 1 && task.getPriority() <= 5, "Task priority should be between 1 and 5.");
            assertDoesNotThrow(() -> Difficulty.valueOf(task.getDifficulty()), "Task difficulty should be valid.");
        }
    }

    @Test
    void isValidTask_NullName_ShouldThrowException() {
        UserModel user = this.genUserModel(UUID.randomUUID().toString(), "testUser");
        TaskModel invalidTask = this.genTaskModel(null, null, "Description", false, user);

        TaskException.TaskInvalidValueException thrownException = assertThrows(
                TaskException.TaskInvalidValueException.class,
                () -> taskService.isValidTask(invalidTask)
        );

        assertEquals("Invalid value for: Task name is required", thrownException.getMessage());
    }

    @Test
    void isValidTask_InvalidPriority_ShouldThrowException() {
        UserModel user = this.genUserModel(UUID.randomUUID().toString(), "testUser");
        TaskModel invalidTask = this.genTaskModel(null, "Task", "Description", false, user);
        invalidTask.setPriority(6);

        TaskException.TaskInvalidValueException thrownException = assertThrows(
                TaskException.TaskInvalidValueException.class,
                () -> taskService.isValidTask(invalidTask)
        );

        assertEquals("Invalid value for: Task priority invalid value, out of range [0, 1, 2, 3, 4, 5]", thrownException.getMessage());
    }

    @Test
    void isValidTask_InvalidDifficulty_ShouldThrowException() {
        UserModel user = this.genUserModel(UUID.randomUUID().toString(), "testUser");
        TaskModel invalidTask = this.genTaskModel(null, "Task", "Description", false, user);
        invalidTask.setDifficulty("INVALID");

        TaskException.TaskInvalidValueException thrownException = assertThrows(
                TaskException.TaskInvalidValueException.class,
                () -> taskService.isValidTask(invalidTask)
        );

        assertEquals("Invalid value for: Task difficulty is invalid", thrownException.getMessage());
    }

    @Test
    void isValidTask_UpdatedAtBeforeCreatedAt_ShouldThrowException() {
        UserModel user = this.genUserModel(UUID.randomUUID().toString(), "testUser");
        TaskModel invalidTask = this.genTaskModel(null, "Task", "Description", false, user);
        invalidTask.setCreatedAt(LocalDateTime.now());
        invalidTask.setUpdatedAt(LocalDateTime.now().minusMinutes(1));

        TaskException.TaskInvalidValueException thrownException = assertThrows(
                TaskException.TaskInvalidValueException.class,
                () -> taskService.isValidTask(invalidTask)
        );

        assertEquals("Invalid value for: Task updated at is before created at!", thrownException.getMessage());
    }

    /**
     * Generates a TaskModel object with the given parameters.
     * <p>
     * This method is used to create a new instance of TaskModel
     * with specified values for the task's ID, name, description,
     * and completion status.
     *
     * @param taskId          the unique identifier for the task (can be null for new tasks).
     * @param taskName        the name of the task.
     * @param taskDescription a description of the task.
     * @param done            the completion status of the task.
     * @return a TaskModel object populated with the provided parameters.
     */
    private TaskModel genTaskModel(String taskId, String taskName, String taskDescription, boolean done, UserModel owner) {
        final TaskModel newTask = new TaskModel();
        newTask.setId(taskId);
        newTask.setName(taskName);
        newTask.setDescription(taskDescription);
        newTask.setDone(done);
        newTask.setOwnerIds(Collections.singletonList(owner.getId()));
        return newTask;
    }

    private UserModel genUserModel(String userId, String username) {
        final UserModel newUser = new UserModel();
        newUser.setId(userId);
        newUser.setUsername(username);
        return newUser;
    }
}
