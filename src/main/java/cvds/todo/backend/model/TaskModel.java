package cvds.todo.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

import java.util.List;

import java.util.Objects;

@Document(collection = "tasks")
public class TaskModel {
    @Id
    private String id;
    private String name;
    private String description;
    private String difficulty;
    private int priority;
    private LocalDateTime deadline;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean done;

    private List<String> ownerIds;


    public TaskModel() {
    }

    public TaskModel(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<String> getOwnerIds() {
        return ownerIds;
    }

    public void setOwnerIds(List<String> ownerIds) {
        this.ownerIds = ownerIds;
    }

    public void addOwnerId(String ownerId) {
        this.ownerIds.add(ownerId);
    }

    public void removeOwnerId(String ownerId) {
        this.ownerIds.remove(ownerId);

    }

    @Override
    public String toString() {
        return "TaskModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", done='" + done + '\'' +
                ", priority='" + priority + '\'' +
                ", difficult='" + difficulty + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TaskModel task = (TaskModel) obj;
        return done == task.done &&
                Objects.equals(id, task.id) &&
                Objects.equals(name, task.name) &&
                Objects.equals(description, task.description) &&
                Objects.equals(priority, task.priority) &&
                Objects.equals(difficulty, task.difficulty);
    }
}
