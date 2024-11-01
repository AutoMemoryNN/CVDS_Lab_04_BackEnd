package cvds.todo.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Modelo que representa una tarea en el sistema.
 * Este modelo contiene detalles de la tarea como nombre, descripción, prioridad, y otros atributos.
 */
@Document(collection = "tasks")
public class TaskModel {
    @Id
    private String id;  // Identificador único de la tarea
    private String name;  // Nombre de la tarea
    private String description;  // Descripción de la tarea
    private String difficulty;  // Dificultad de la tarea (ej. "HIGH", "MEDIUM", "LOW")
    private int priority;  // Prioridad de la tarea
    private LocalDateTime deadline;  // Fecha límite para completar la tarea
    private LocalDateTime createdAt;  // Fecha de creación de la tarea
    private LocalDateTime updatedAt;  // Última fecha de actualización de la tarea
    private boolean done;  // Estado de finalización de la tarea
    private List<String> ownerIds;  // Lista de IDs de los propietarios de la tarea

    /**
     * Constructor vacío para inicialización.
     */
    public TaskModel() {
    }

    /**
     * Constructor con ID para inicialización con identificador específico.
     *
     * @param id El identificador de la tarea.
     */
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

    /**
     * Añade un ID de propietario a la tarea.
     *
     * @param ownerId ID del propietario a añadir.
     */
    public void addOwnerId(String ownerId) {
        this.ownerIds.add(ownerId);
    }

    /**
     * Elimina un ID de propietario de la tarea.
     *
     * @param ownerId ID del propietario a eliminar.
     */
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
