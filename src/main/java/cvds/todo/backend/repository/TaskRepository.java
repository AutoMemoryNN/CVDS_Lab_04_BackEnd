package cvds.todo.backend.repository;

import cvds.todo.backend.model.TaskModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para la gestión de tareas en la base de datos MongoDB.
 * Proporciona métodos personalizados para realizar consultas en la colección "tasks".
 */
public interface TaskRepository extends MongoRepository<TaskModel, String> {

    /**
     * Encuentra todas las tareas asociadas a un ID de propietario específico.
     *
     * @param ownerId ID del propietario de la tarea.
     * @return Lista de tareas asociadas al propietario.
     */
    List<TaskModel> findByOwnerIdsContaining(String ownerId);

    /**
     * Encuentra una tarea específica por su ID y su propietario.
     *
     * @param ownerId ID del propietario de la tarea.
     * @param id      ID de la tarea.
     * @return La tarea que coincide con el ID del propietario y el ID de la tarea.
     */
    TaskModel findFirstByOwnerIdsContainingAndId(String ownerId, String id);

    /**
     * Elimina una tarea específica por su ID y propietario.
     *
     * @param ownerId ID del propietario de la tarea.
     * @param id      ID de la tarea a eliminar.
     * @return La tarea eliminada.
     */
    TaskModel deleteByIdAndOwnerIdsContaining(String ownerId, String id);

    /**
     * Encuentra todas las tareas completadas asociadas a un ID de propietario.
     *
     * @param ownerId ID del propietario de la tarea.
     * @return Lista de tareas completadas.
     */
    List<TaskModel> findByOwnerIdsContainingAndDoneTrue(String ownerId);

    /**
     * Encuentra todas las tareas no completadas asociadas a un ID de propietario.
     *
     * @param ownerId ID del propietario de la tarea.
     * @return Lista de tareas no completadas.
     */
    List<TaskModel> findByOwnerIdsContainingAndDoneFalse(String ownerId);

    /**
     * Encuentra todas las tareas dentro de un rango de prioridad específico.
     *
     * @param ownerId    ID del propietario de la tarea.
     * @param minPriority Prioridad mínima.
     * @param maxPriority Prioridad máxima.
     * @return Lista de tareas dentro del rango de prioridad especificado.
     */
    List<TaskModel> findByOwnerIdsContainingAndPriorityBetween(String ownerId, int minPriority, int maxPriority);

    /**
     * Encuentra todas las tareas con fecha límite antes de una fecha específica.
     *
     * @param ownerId  ID del propietario de la tarea.
     * @param deadline Fecha límite.
     * @return Lista de tareas con fecha límite anterior a la especificada.
     */
    List<TaskModel> findByOwnerIdsContainingAndDeadlineBefore(String ownerId, LocalDateTime deadline);

    /**
     * Encuentra todas las tareas con fecha límite después de una fecha específica.
     *
     * @param ownerId  ID del propietario de la tarea.
     * @param deadline Fecha límite.
     * @return Lista de tareas con fecha límite posterior a la especificada.
     */
    List<TaskModel> findByOwnerIdsContainingAndDeadlineAfter(String ownerId, LocalDateTime deadline);

    /**
     * Encuentra todas las tareas creadas después de una fecha específica.
     *
     * @param ownerId   ID del propietario de la tarea.
     * @param createdAt Fecha de creación mínima.
     * @return Lista de tareas creadas después de la fecha especificada.
     */
    List<TaskModel> findByOwnerIdsContainingAndCreatedAtAfter(String ownerId, LocalDateTime createdAt);

    /**
     * Encuentra todas las tareas actualizadas después de una fecha específica.
     *
     * @param ownerId   ID del propietario de la tarea.
     * @param updatedAt Fecha de última actualización mínima.
     * @return Lista de tareas actualizadas después de la fecha especificada.
     */
    List<TaskModel> findByOwnerIdsContainingAndUpdatedAtAfter(String ownerId, LocalDateTime updatedAt);
}
