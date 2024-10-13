package cvds.todo.backend.services;


import cvds.todo.backend.exceptions.AppException;
import cvds.todo.backend.exceptions.TaskException;
import cvds.todo.backend.interfeces.TaskRepository;
import cvds.todo.backend.interfeces.TasksService;
import cvds.todo.backend.model.Difficulty;
import cvds.todo.backend.model.TaskModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Servicio que implementa la lógica para gestionar las tareas.
 * Se comunica con el repositorio de tareas para realizar operaciones CRUD.
 */
@Service
public class TaskService implements TasksService {

    @Autowired
    private TaskRepository taskRepository;

    /**
     * Obtiene todas las tareas desde el repositorio.
     *
     * @return Lista de tareas almacenadas en el repositorio.
     */
    @Override
    public List<TaskModel> getAllTasks() throws AppException {
        return taskRepository.findAll();
    }

    /**
     * Obtiene una tarea por su ID desde el repositorio.
     *
     * @param id Identificador de la tarea.
     * @return La tarea correspondiente al ID o null si no se encuentra.
     */
    @Override
    public TaskModel getTaskById(String id) throws AppException {
        Optional<TaskModel> result = taskRepository.findById(id);

        if (result.isPresent()) {
            return result.get();
        }

        throw new TaskException.TaskNotFoundException(id);
    }

    /**
     * Crea una nueva tarea y la almacena en el repositorio.
     *
     * @param task Modelo de la nueva tarea a crear.
     * @return La tarea creada.
     */
    @Override
    public TaskModel createTask(TaskModel task) throws AppException {
        this.isValidTask(task);

        String id = UUID.randomUUID().toString();
        task.setId(id);

        return taskRepository.insert(task);
    }

    /**
     * Actualiza una tarea existente en el repositorio.
     *
     * @param id   Identificador de la tarea a actualizar.
     * @param task Modelo de la tarea con los nuevos valores.
     * @return La tarea actualizada.
     */
    @Override
    public TaskModel updateTask(String id, TaskModel task) throws AppException {
        this.isValidTask(task);

        Optional<TaskModel> existingTask = taskRepository.findById(id);

        if (existingTask.isPresent()) {
            TaskModel taskToUpdate = existingTask.get();

            taskToUpdate.setName(task.getName() == null ? taskToUpdate.getName() : task.getName());
            taskToUpdate.setDescription(task.getDescription() == null ? taskToUpdate.getDescription() : task.getDescription());
            taskToUpdate.setDone(task.isDone());

            this.taskRepository.save(taskToUpdate);

            return taskToUpdate;
        }

        throw new TaskException.TaskNotFoundException(id);
    }

    /**
     * Elimina una tarea del repositorio por su ID.
     *
     * @param id Identificador de la tarea a eliminar.
     */
    @Override
    public TaskModel deleteTask(String id) throws AppException {
        if (taskRepository.existsById(id)) {
            TaskModel taskToDelete = taskRepository.findById(id).get();
            taskRepository.deleteById(id);
            return taskToDelete;
        }
        throw new TaskException.TaskNotFoundException(id);
    }

    private void isValidTask(TaskModel task) throws AppException {
        if (task.getName() == null) {
            throw new TaskException.TaskInvalidValueException("Task name is required");
        }
        if (task.getDifficult() != null) {
            try {
                Difficulty.valueOf(task.getDifficult().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new TaskException.TaskInvalidValueException("Task difficult is invalid");
            }
        }
    }
}
