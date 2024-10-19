package cvds.todo.backend.interfeces;

import cvds.todo.backend.exceptions.AppException;
import cvds.todo.backend.model.TaskModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface TasksService {

    public List<TaskModel> getAllTasks() throws AppException;

    public TaskModel getTaskById(String id) throws AppException;

    public TaskModel createTask(TaskModel task) throws AppException;

    public TaskModel updateTask(String id, TaskModel task) throws AppException;

    public TaskModel deleteTask(String id) throws AppException;

    public List<TaskModel> generateExamples() throws AppException;

    public void isValidTask(TaskModel task) throws AppException;
}
