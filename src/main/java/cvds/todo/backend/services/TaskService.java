package cvds.todo.backend.services;


import cvds.todo.backend.exceptions.AppException;
import cvds.todo.backend.exceptions.TaskException;
import cvds.todo.backend.model.UserModel;
import cvds.todo.backend.repository.TaskRepository;
import cvds.todo.backend.interfeces.TasksService;
import cvds.todo.backend.model.Difficulty;
import cvds.todo.backend.model.TaskModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

//TODO : renew cookie

@Service
public class TaskService implements TasksService {

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public List<TaskModel> getAllTasks(UserModel user) throws AppException {
        return taskRepository.findByOwnerIdsContaining(user.getId());
    }

    @Override
    public TaskModel getTaskById(String id, UserModel user) throws AppException {
        TaskModel result = taskRepository.findFirstByOwnerIdsContainingAndId(user.getId(), id);

        if (result != null) {
            return result;
        }

        throw new TaskException.TaskNotFoundException(id);
    }

    @Override
    public TaskModel createTask(TaskModel task, UserModel user) throws AppException {
        isValidTask(task);

        task.setId(UUID.randomUUID().toString());
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        task.setOwnerIds(Collections.singletonList(user.getId()));

        return taskRepository.insert(task);
    }

    @Override
    public TaskModel updateTask(String id, TaskModel task, UserModel user) throws AppException {

        Optional<TaskModel> existingTask = taskRepository.findById(id);

        if (existingTask.isPresent()) {
            TaskModel taskToUpdate = existingTask.get();

            taskToUpdate.setName(task.getName() == null ? taskToUpdate.getName() : task.getName());
            taskToUpdate.setDescription(task.getDescription() == null ? taskToUpdate.getDescription() : task.getDescription());
            taskToUpdate.setDeadline(task.getDeadline() == null ? taskToUpdate.getDeadline() : task.getDeadline());
            taskToUpdate.setPriority(task.getPriority() == 0 ? taskToUpdate.getPriority() : task.getPriority());
            taskToUpdate.setDifficulty(task.getDifficulty() == null ? taskToUpdate.getDifficulty() : task.getDifficulty());
            taskToUpdate.setDone(task.isDone());

            taskToUpdate.setUpdatedAt(LocalDateTime.now());
            this.isValidTask(taskToUpdate);
            this.taskRepository.save(taskToUpdate);

            return taskToUpdate;
        }

        throw new TaskException.TaskNotFoundException(id);
    }

    @Override
    public TaskModel deleteTask(String id, UserModel user) throws AppException {
        TaskModel taskToDelete = taskRepository.findFirstByOwnerIdsContainingAndId(user.getId(), id);

        if (taskToDelete != null) {
            taskRepository.delete(taskToDelete);
            return taskToDelete;
        }

        throw new TaskException.TaskNotFoundException(id);
    }

    @Override
    public List<TaskModel> generateExamples(UserModel user) throws AppException {
        Random random = new Random();
        int numberOfTasks = random.nextInt(901) + 100;
        List<TaskModel> tasks = new ArrayList<>();

        for (int i = 0; i < numberOfTasks; i++) {
            TaskModel task = new TaskModel();

            task.setId(UUID.randomUUID().toString());

            task.setName("Task: " + (i + 1));
            task.setDescription("Description for Task " + (i + 1));
            task.setPriority(random.nextInt(5) + 1);
            task.setDifficulty(String.valueOf(Difficulty.values()[random.nextInt(Difficulty.values().length)]));
            task.setDone(random.nextBoolean());

            task.setDeadline(this.getRandomDateTime(LocalDate.now().plusDays(-5), LocalTime.now(), 25));

            final LocalDateTime randomDateTime = this.getRandomDateTime(LocalDate.now(), LocalTime.now(), 30);
            task.setCreatedAt(randomDateTime);
            task.setUpdatedAt(randomDateTime);

            this.isValidTask(task);

            tasks.add(task);
        }
        this.taskRepository.insert(tasks);
        return tasks;
    }

    public LocalDateTime getRandomDateTime(final LocalDate startDate, final LocalTime startTime, final int daysOfRange) {
        long minDay = LocalDateTime.of(startDate, startTime).toEpochSecond(ZoneOffset.UTC);
        long maxDay = LocalDateTime.of(LocalDate.now().plusDays(daysOfRange), startTime).toEpochSecond(ZoneOffset.UTC);

        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return LocalDateTime.ofEpochSecond(randomDay, 0, ZoneOffset.UTC);
    }

    public List<TaskModel> deleteAllTasks(UserModel user) throws AppException {
        List<TaskModel> tasksDeleted = getAllTasks(user);
        tasksDeleted.forEach(task -> taskRepository.deleteByIdAndOwnerIdsContaining(user.getId(), task.getId()));
        return tasksDeleted;
    }

    public void isValidTask(TaskModel task) throws AppException {
        if (task.getName() == null) {
            throw new TaskException.TaskInvalidValueException("Task name is required");
        }
        if (task.getPriority() < 0 || 5 < task.getPriority()) {
            throw new TaskException.TaskInvalidValueException("Task priority invalid value, out of range [0, 1, 2, 3, 4, 5]");
        }
        if (task.getDifficulty() != null) {
            try {
                Difficulty.valueOf(task.getDifficulty().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new TaskException.TaskInvalidValueException("Task difficulty is invalid");
            }
        }
        if (task.getUpdatedAt() != null && task.getCreatedAt() != null && task.getUpdatedAt().isBefore(task.getCreatedAt())) {
            throw new TaskException.TaskInvalidValueException("Task updated at is before created at!");
        }
    }
}
