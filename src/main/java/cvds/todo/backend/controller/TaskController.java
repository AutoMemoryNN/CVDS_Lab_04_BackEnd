package cvds.todo.backend.controller;

import cvds.todo.backend.exceptions.AppException;
import cvds.todo.backend.exceptions.SessionException;
import cvds.todo.backend.model.TaskModel;
import cvds.todo.backend.model.UserModel;
import cvds.todo.backend.services.SessionService;
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

    @Autowired
    private SessionService sessionService;

    /**
     * Obtener todas las tareas.
     *
     * @return Lista de todas las tareas.
     */
    @GetMapping
    public ResponseEntity<?> getAllTasks(@RequestHeader("Authorization") String sessionToken) {
        try {
            UserModel userLogged = this.getUserFromSessions(sessionToken);
            List<TaskModel> tasks = taskService.getAllTasks(userLogged);
            tasks.forEach(elem -> elem.setOwnerIds(null));
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
    public ResponseEntity<?> getTaskById(@RequestHeader("Authorization") String sessionToken, @PathVariable("id") String id) {
        try {
            UserModel userLogged = this.getUserFromSessions(sessionToken);
            TaskModel task = taskService.getTaskById(id, userLogged);
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
    public ResponseEntity<?> createTask(@RequestHeader("Authorization") String sessionToken, @RequestBody TaskModel task) {
        try {
            UserModel userLogged = this.getUserFromSessions(sessionToken);
            TaskModel taskModel = taskService.createTask(task, userLogged);
            taskModel.setOwnerIds(null);
            return ResponseEntity.status(201).body(taskModel);
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
    public ResponseEntity<?> updateTask(@RequestHeader("Authorization") String sessionToken, @PathVariable("id") String id, @RequestBody TaskModel task) {
        try {
            UserModel userLogged = this.getUserFromSessions(sessionToken);
            TaskModel updatedTask = taskService.updateTask(id, task, userLogged);
            updatedTask.setOwnerIds(null);
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
    public ResponseEntity<?> deleteTask(@RequestHeader("Authorization") String sessionToken, @PathVariable("id") String id) {
        try {
            UserModel userLogged = this.getUserFromSessions(sessionToken);
            TaskModel deletedTask = taskService.deleteTask(id, userLogged);
            deletedTask.setOwnerIds(null);
            return ResponseEntity.ok(deletedTask);
        } catch (Exception e) {
            if (e instanceof AppException) {
                return ((AppException) e).getResponse();
            }
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    /**
     * Eliminar todas las tareas.
     *
     * @return Un mensaje indicando cuántas tareas fueron eliminadas exitosamente.
     */
    @DeleteMapping("/all")
    public ResponseEntity<?> deleteAllTasks(@RequestHeader("Authorization") String sessionToken) {

        try {
            UserModel userLogged = this.getUserFromSessions(sessionToken);
            List<TaskModel> taskDeleted = this.taskService.deleteAllTasks(userLogged);
            return ResponseEntity.status(200).body(taskDeleted.size() + " Tasks were deleted successfully");
        } catch (Exception e) {
            if (e instanceof AppException) {
                return ((AppException) e).getResponse();
            }
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    /**
     * Generar tareas de ejemplo.
     *
     * @return Un mensaje indicando cuántas tareas fueron generadas.
     */
    @PostMapping("/gen")
    public ResponseEntity<?> generateTasks(@RequestHeader("Authorization") String sessionToken) {
        try {
            UserModel userLogged = this.getUserFromSessions(sessionToken);
            List<TaskModel> newTasks = taskService.generateExamples(userLogged);
            return ResponseEntity.status(201).body(newTasks.size() + " Tasks were generated");
        } catch (Exception e) {
            if (e instanceof AppException) {
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

    private UserModel getUserFromSessions(String sessionToken) throws SessionException {
        if (sessionToken == null || !this.sessionService.isSessionActive(sessionToken)) {
            return null;
        }
        return this.sessionService.getUserFromSession(sessionToken);
    }
}

