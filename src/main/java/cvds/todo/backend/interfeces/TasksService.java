package cvds.todo.backend.interfeces;

import cvds.todo.backend.exceptions.AppException;
import cvds.todo.backend.model.TaskModel;
import cvds.todo.backend.model.UserModel;

import java.util.List;

/**
 * Interface for task management services.
 * Provides methods for creating, reading, updating, and deleting tasks.
 */
public interface TasksService {

    /**
     * Get all tasks.
     *
     * @return List of all available tasks.
     * @throws AppException if an error occurs while retrieving the tasks.
     */
    List<TaskModel> getAllTasks(UserModel user) throws AppException;

    /**
     * Get a task by its ID.
     *
     * @param id Identifier of the task.
     * @return The task corresponding to the provided ID.
     * @throws AppException if an error occurs while retrieving the task.
     */
    TaskModel getTaskById(String id, UserModel user) throws AppException;

    /**
     * Create a new task.
     *
     * @param task Task model to create.
     * @return The created task.
     * @throws AppException if an error occurs while creating the task.
     */
    TaskModel createTask(TaskModel task, UserModel user) throws AppException;

    /**
     * Update an existing task by its ID.
     *
     * @param id   Identifier of the task to update.
     * @param task Updated task model.
     * @return The updated task.
     * @throws AppException if an error occurs while updating the task.
     */
    TaskModel updateTask(String id, TaskModel task, UserModel user) throws AppException;

    /**
     * Delete a task by its ID.
     *
     * @param id Identifier of the task to delete.
     * @return The deleted task.
     * @throws AppException if an error occurs while deleting the task.
     */
    TaskModel deleteTask(String id, UserModel user) throws AppException;

    /**
     * Generate examples of tasks.
     *
     * @return List of generated example tasks.
     * @throws AppException if an error occurs while generating the tasks.
     */
    List<TaskModel> generateExamples(UserModel user) throws AppException;

    /**
     * Delete all tasks.
     *
     * @return List of all deleted tasks.
     * @throws AppException if an error occurs while deleting the tasks.
     */
    List<TaskModel> deleteAllTasks(UserModel user) throws AppException;

    /**
     * Validate a task before insert in database.
     *
     * @param task Task model to validate.
     * @throws AppException if the task is not valid.
     */
    void isValidTask(TaskModel task) throws AppException;
}