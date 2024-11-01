package cvds.todo.backend.interfeces;

import cvds.todo.backend.exceptions.AppException;
import cvds.todo.backend.model.TaskModel;
import cvds.todo.backend.model.UserModel;

import java.util.List;

/**
 * Interfaz para los servicios de gestión de tareas.
 * Proporciona métodos para crear, leer, actualizar, eliminar y validar tareas.
 */
public interface TasksService {

    /**
     * Obtiene todas las tareas de un usuario específico.
     *
     * @param user El usuario para el que se recuperan las tareas.
     * @return Lista de todas las tareas disponibles del usuario.
     * @throws AppException Si ocurre un error al recuperar las tareas.
     */
    List<TaskModel> getAllTasks(UserModel user) throws AppException;

    /**
     * Obtiene una tarea por su ID.
     *
     * @param id   Identificador de la tarea.
     * @param user El usuario propietario de la tarea.
     * @return La tarea correspondiente al ID proporcionado.
     * @throws AppException Si ocurre un error al recuperar la tarea.
     */
    TaskModel getTaskById(String id, UserModel user) throws AppException;

    /**
     * Crea una nueva tarea.
     *
     * @param task La tarea a crear.
     * @param user El usuario propietario de la tarea.
     * @return La tarea creada.
     * @throws AppException Si ocurre un error al crear la tarea.
     */
    TaskModel createTask(TaskModel task, UserModel user) throws AppException;

    /**
     * Actualiza una tarea existente por su ID.
     *
     * @param id   Identificador de la tarea a actualizar.
     * @param task Modelo de la tarea actualizada.
     * @param user El usuario propietario de la tarea.
     * @return La tarea actualizada.
     * @throws AppException Si ocurre un error al actualizar la tarea.
     */
    TaskModel updateTask(String id, TaskModel task, UserModel user) throws AppException;

    /**
     * Elimina una tarea por su ID.
     *
     * @param id   Identificador de la tarea a eliminar.
     * @param user El usuario propietario de la tarea.
     * @return La tarea eliminada.
     * @throws AppException Si ocurre un error al eliminar la tarea.
     */
    TaskModel deleteTask(String id, UserModel user) throws AppException;

    /**
     * Genera tareas de ejemplo para el usuario especificado.
     *
     * @param user El usuario para el que se generan las tareas.
     * @return Lista de tareas de ejemplo generadas.
     * @throws AppException Si ocurre un error al generar las tareas.
     */
    List<TaskModel> generateExamples(UserModel user) throws AppException;

    /**
     * Elimina todas las tareas de un usuario específico.
     *
     * @param user El usuario cuyas tareas serán eliminadas.
     * @return Lista de todas las tareas eliminadas.
     * @throws AppException Si ocurre un error al eliminar las tareas.
     */
    List<TaskModel> deleteAllTasks(UserModel user) throws AppException;

    /**
     * Valida una tarea antes de insertarla en la base de datos.
     *
     * @param task Modelo de la tarea a validar.
     * @throws AppException Si la tarea no es válida.
     */
    void isValidTask(TaskModel task) throws AppException;
}