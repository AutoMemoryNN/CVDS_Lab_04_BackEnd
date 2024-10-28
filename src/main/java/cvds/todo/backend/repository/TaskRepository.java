package cvds.todo.backend.repository;

import cvds.todo.backend.model.TaskModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends MongoRepository<TaskModel, String> {
    List<TaskModel> findByOwnerIdsContaining(String ownerId);
    TaskModel findFirstByOwnerIdsContainingAndId(String ownerId, String id);
    TaskModel deleteByIdAndOwnerIdsContaining(String ownerId, String id);


    List<TaskModel> findByOwnerIdsContainingAndDoneTrue(String ownerId);
    List<TaskModel> findByOwnerIdsContainingAndDoneFalse(String ownerId);
    List<TaskModel> findByOwnerIdsContainingAndPriorityBetween(String ownerId, int minPriority, int maxPriority);
    List<TaskModel> findByOwnerIdsContainingAndDeadlineBefore(String ownerId, LocalDateTime deadline);
    List<TaskModel> findByOwnerIdsContainingAndDeadlineAfter(String ownerId, LocalDateTime deadline);
    List<TaskModel> findByOwnerIdsContainingAndCreatedAtAfter(String ownerId, LocalDateTime createdAt);
    List<TaskModel> findByOwnerIdsContainingAndUpdatedAtAfter(String ownerId, LocalDateTime updatedAt);
}