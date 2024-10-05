package cvds.todo.backend.controller;

import cvds.todo.backend.exceptions.AppException;
import cvds.todo.backend.model.TaskModel;
import cvds.todo.backend.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * Controlador REST para la gestión de tareas (TaskModel).
 * Proporciona endpoints para crear, leer, actualizar y eliminar tareas.
 */
@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    /**
     * Obtener todas las tareas.
     *
     * @return Lista de todas las tareas.
     */
    @GetMapping
    public ResponseEntity<?> getAllTasks() {
        try {
            List<TaskModel> tasks = taskService.getAllTasks();
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            if (e instanceof AppException) {
                return ((AppException) e).getResponse();
            } else {
                return ResponseEntity.status(500).body(e.getMessage());
            }
        }
    }

    /**
     * Obtener una tarea por su ID.
     *
     * @param id Identificador de la tarea.
     * @return La tarea correspondiente al ID proporcionado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable("id") String id) {
        try {
            TaskModel task = taskService.getTaskById(id);
            return ResponseEntity.ok(task);
        } catch (Exception e) {
            if (e instanceof AppException) {
                return ((AppException) e).getResponse();
            }
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    /**
     * Crear una nueva tarea.
     *
     * @param task Modelo de tarea enviado en el cuerpo de la solicitud.
     * @return El UUID de la nueva tarea creada.
     */
    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody TaskModel task) {
        try {
            taskService.createTask(task);
            return ResponseEntity.status(201).body(task);
        } catch (Exception e) {
            if (e instanceof AppException) {
                return ((AppException) e).getResponse();
            }
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    /**
     * Actualizar una tarea existente por su ID.
     *
     * @param id   Identificador de la tarea a actualizar.
     * @param task Modelo de tarea actualizado.
     * @return La tarea actualizada.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable("id") String id, @RequestBody TaskModel task) {
        try {
            TaskModel updatedTask = taskService.updateTask(id, task);
            return ResponseEntity.status(200).body(updatedTask);
        } catch (Exception e) {
            if (e instanceof AppException) {
                return ((AppException) e).getResponse();
            }
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    /**
     * Eliminar una tarea por su ID.
     *
     * @param id Identificador de la tarea a eliminar.
     * @return Respuesta sin contenido.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable("id") String id) {
        try {
            TaskModel deletedTask = taskService.deleteTask(id);
            return ResponseEntity.ok(deletedTask);
        }
        catch (Exception e){
            if (e instanceof AppException){
                return ((AppException) e).getResponse();
            }
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    /**
     * Endpoint for verify service state
     *
     * @return message
     */
    @GetMapping("/health")
    public ResponseEntity<?> checkHealth() {
        HashMap<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "The server is up");
        return ResponseEntity.ok(response);
    }
}

