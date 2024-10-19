package cvds.todo.backend.service;

import cvds.todo.backend.exceptions.AppException;
import cvds.todo.backend.exceptions.TaskException;
import cvds.todo.backend.interfeces.TaskRepository;
import cvds.todo.backend.model.TaskModel;
import cvds.todo.backend.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        List<TaskModel> expectedTasks = Arrays.asList(
                this.genTaskModel("1", "Task 1", "Description 1", false),
                this.genTaskModel("2", "Task 2", "Description 2", true)
        );
        when(taskRepository.findAll()).thenReturn(expectedTasks);

        List<TaskModel> actualTasks = taskService.getAllTasks();

        assertEquals(expectedTasks, actualTasks, "Expected tasks do not match actual tasks.");
        verify(taskRepository, times(1)).findAll();
    }

    /**
     * Test case to verify retrieval of an existing task by ID.
     * This method tests the getTaskById() method of TaskService.
     *
     * @throws AppException if there is an error retrieving the task
     */
    @Test
    void getTaskById_ExistingTask_ShouldReturnTask() throws AppException {
        TaskModel expectedTask = this.genTaskModel(EXISTING_TASK_ID, TASK_NAME, TASK_DESCRIPTION, false);
        when(taskRepository.findById(EXISTING_TASK_ID)).thenReturn(Optional.of(expectedTask));

        TaskModel actualTask = taskService.getTaskById(EXISTING_TASK_ID);

        assertEquals(expectedTask, actualTask, "The returned task should match the expected task.");
        verify(taskRepository, times(1)).findById(EXISTING_TASK_ID);
    }

    /**
     * Test case to verify exception thrown when trying to retrieve a non-existing task by ID.
     * This method tests the getTaskById() method of TaskService.
     */
    @Test
    void getTaskById_NonExistingTask_ShouldThrowTaskNotFoundException() {
        when(taskRepository.findById(NON_EXISTING_TASK_ID)).thenReturn(Optional.empty());

        TaskException.TaskNotFoundException thrownException = assertThrows(
                TaskException.TaskNotFoundException.class,
                () -> taskService.getTaskById(NON_EXISTING_TASK_ID)
        );

        assertEquals("Task: " + NON_EXISTING_TASK_ID + ", not found in the database.", thrownException.getMessage());
        verify(taskRepository, times(1)).findById(NON_EXISTING_TASK_ID);
    }

    /**
     * Test case to verify the creation of a new task.
     * This method tests the createTask() method of TaskService.
     *
     * @throws AppException if there is an error creating the task
     */
    @Test
    void createTask_ShouldReturnCreatedTask() throws AppException {
        TaskModel taskToCreate = this.genTaskModel(null, TASK_NAME, TASK_DESCRIPTION, false);
        TaskModel expectedTask = this.genTaskModel(UUID.randomUUID().toString(), TASK_NAME, TASK_DESCRIPTION, false);
        when(taskRepository.insert(any(TaskModel.class))).thenReturn(expectedTask);

        TaskModel createdTask = taskService.createTask(taskToCreate);

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
        TaskModel existingTask = this.genTaskModel(EXISTING_TASK_ID, "Old Task", "Old Description", false);
        TaskModel updatedTask = this.genTaskModel(EXISTING_TASK_ID, "Updated Task", "Updated Description", true);
        when(taskRepository.findById(EXISTING_TASK_ID)).thenReturn(Optional.of(existingTask));
        when(taskRepository.existsById(EXISTING_TASK_ID)).thenReturn(true);
        when(taskRepository.insert(any(TaskModel.class))).thenReturn(updatedTask);

        TaskModel result = taskService.updateTask(EXISTING_TASK_ID, updatedTask);

        assertEquals(updatedTask, result, "Updated task should match the provided task model.");
        verify(taskRepository, times(1)).findById(EXISTING_TASK_ID);
        verify(taskRepository, times(1)).save(any(TaskModel.class));
    }

    /**
     * Test case to verify exception thrown when trying to update a non-existing task.
     * This method tests the updateTask() method of TaskService.
     */
    @Test
    void updateTask_NonExistingTask_ShouldThrowTaskNotFoundException() {
        TaskModel updatedTask = this.genTaskModel(NON_EXISTING_TASK_ID, "Updated Task", "Updated Description", true);
        when(taskRepository.findById(NON_EXISTING_TASK_ID)).thenReturn(Optional.empty());

        TaskException.TaskNotFoundException thrownException = assertThrows(
                TaskException.TaskNotFoundException.class,
                () -> taskService.updateTask(NON_EXISTING_TASK_ID, updatedTask)
        );

        assertEquals("Task: " + NON_EXISTING_TASK_ID + ", not found in the database.", thrownException.getMessage());
        verify(taskRepository, times(1)).findById(NON_EXISTING_TASK_ID);
    }

    /**
     * Test case to verify the deletion of an existing task.
     * This method tests the deleteTask() method of TaskService.
     *
     * @throws AppException if there is an error deleting the task
     */
    @Test
    void deleteTask_ExistingTask_ShouldDeleteTask() throws AppException {
        TaskModel taskToDelete = this.genTaskModel(EXISTING_TASK_ID, TASK_NAME, TASK_DESCRIPTION, false);
        when(taskRepository.existsById(EXISTING_TASK_ID)).thenReturn(true);
        when(taskRepository.findById(EXISTING_TASK_ID)).thenReturn(Optional.of(taskToDelete));

        TaskModel deletedTask = taskService.deleteTask(EXISTING_TASK_ID);

        assertEquals(taskToDelete, deletedTask, "Deleted task should match the existing task.");
        verify(taskRepository, times(1)).findById(EXISTING_TASK_ID);
        verify(taskRepository, times(1)).deleteById(EXISTING_TASK_ID);
    }

    /**
     * Test case to verify exception thrown when trying to delete a non-existing task.
     * This method tests the deleteTask() method of TaskService.
     */
    @Test
    void deleteTask_NonExistingTask_ShouldThrowTaskNotFoundException() {
        when(taskRepository.existsById(NON_EXISTING_TASK_ID)).thenReturn(false);

        TaskException.TaskNotFoundException thrownException = assertThrows(
                TaskException.TaskNotFoundException.class,
                () -> taskService.deleteTask(NON_EXISTING_TASK_ID)
        );

        assertEquals("Task: " + NON_EXISTING_TASK_ID + ", not found in the database.", thrownException.getMessage());
    }

    /**
     * Test case to verify the generation of example tasks.
     * This method tests the generateExamples() method of TaskService.
     *
     * @throws AppException if there is an error generating tasks
     */
    @Test
    void generateExamples_ShouldCreateRandomTasks() throws AppException {
        when(taskRepository.insert(any(TaskModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<TaskModel> generatedTasks = taskService.generateExamples();

        assertNotNull(generatedTasks, "Generated tasks should not be null.");
        assertTrue(generatedTasks.size() >= 100 && generatedTasks.size() <= 1000, "Number of generated tasks should be between 100 and 1000.");
        verify(taskRepository, times(generatedTasks.size())).insert(any(TaskModel.class));
    }

    /**
     * Tests the deletion of all existing tasks in the repository.
     *
     * This test verifies that when the deleteAllTasks() method is called,
     * it retrieves all existing tasks from the repository, verifies that
     * each task can be found, and then deletes each task. Finally, it checks
     * that the deleted tasks match the originally retrieved tasks.
     *
     * @throws AppException if an application error occurs during the deletion process.
     */
    @Test
    void deleteAllTasks_ShouldDeleteAllExistingTasks() throws AppException {
        List<TaskModel> existingTasks = Arrays.asList(
                this.genTaskModel("1", "Task 1", "Description 1", false),
                this.genTaskModel("2", "Task 2", "Description 2", true)
        );

        when(taskRepository.findAll()).thenReturn(existingTasks);

        for (TaskModel task : existingTasks) {
            when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        }

        when(taskRepository.existsById(anyString())).thenReturn(true);

        doNothing().when(taskRepository).deleteById(anyString());

        List<TaskModel> deletedTasks = taskService.deleteAllTasks();

        assertEquals(existingTasks, deletedTasks, "Deleted tasks should match the existing tasks.");

        verify(taskRepository, times(existingTasks.size())).deleteById(anyString());
    }

    /**
     * Tests the validation of a valid task.
     *
     * This test checks that when a valid TaskModel object is provided to
     * the isValidTask() method, no exceptions are thrown, indicating that
     * the task is considered valid according to the application's criteria.
     *
     * @throws AppException if an application error occurs during the validation process.
     */
    @Test
    void isValidTask_ValidTask_ShouldNotThrowException() throws AppException {
        TaskModel validTask = this.genTaskModel(null, "Valid Task", "Valid Description", false);
        validTask.setDifficult("MEDIUM");

        assertDoesNotThrow(() -> taskService.isValidTask(validTask), "Valid task should not throw an exception.");
    }

    /**
     * Generates a TaskModel object with the given parameters.
     *
     * This method is used to create a new instance of TaskModel
     * with specified values for the task's ID, name, description,
     * and completion status.
     *
     * @param taskId the unique identifier for the task (can be null for new tasks).
     * @param taskName the name of the task.
     * @param taskDescription a description of the task.
     * @param done the completion status of the task.
     * @return a TaskModel object populated with the provided parameters.
     */
    private TaskModel genTaskModel(String taskId, String taskName, String taskDescription, boolean done) {
        final TaskModel newTask = new TaskModel();
        newTask.setId(taskId);
        newTask.setName(taskName);
        newTask.setDescription(taskDescription);
        newTask.setDone(done);
        return newTask;
    }
}
