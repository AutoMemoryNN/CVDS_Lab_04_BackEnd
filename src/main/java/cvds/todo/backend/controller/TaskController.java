package cvds.todo.backend.controller;

import cvds.todo.backend.exceptions.AppException;
import cvds.todo.backend.exceptions.SessionException;
import cvds.todo.backend.model.TaskModel;
import cvds.todo.backend.model.UserModel;
import cvds.todo.backend.services.AuthorizationService;
import cvds.todo.backend.services.SessionService;
import cvds.todo.backend.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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
    private TaskService taskService; // Servicio para la lógica de negocio de tareas

    @Autowired
    private SessionService sessionService; // Servicio para la gestión de sesiones

    @Autowired
    private AuthorizationService authorizationService; // Servicio para verificar autorizaciones de usuario

    /**
     * Obtener todas las tareas del usuario autenticado.
     *
     * @param sessionToken Token de sesión para autenticar al usuario.
     * @return Una lista con todas las tareas del usuario o un mensaje de error.
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
                return ResponseEntity.status(500).body(Collections.singletonMap("error", "Server error"));
            }
        }
    }

    /**
     * Obtener una tarea específica por su ID.
     *
     * @param sessionToken Token de sesión del usuario.
     * @param id           Identificador de la tarea a recuperar.
     * @return La tarea correspondiente al ID proporcionado o un mensaje de error.
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
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Server error"));
        }
    }

    /**
     * Crear una nueva tarea.
     *
     * @param sessionToken Token de sesión del usuario autenticado.
     * @param task         Modelo de tarea enviado en el cuerpo de la solicitud.
     * @return El UUID de la nueva tarea creada o un mensaje de error.
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
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Server error"));
        }
    }

    /**
     * Actualizar una tarea existente por su ID.
     *
     * @param sessionToken Token de sesión del usuario.
     * @param id           Identificador de la tarea a actualizar.
     * @param task         Modelo de tarea actualizado.
     * @return La tarea actualizada o un mensaje de error.
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
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Server error"));
        }
    }

    /**
     * Eliminar una tarea por su ID.
     *
     * @param sessionToken Token de sesión del usuario autenticado.
     * @param id           Identificador de la tarea a eliminar.
     * @return La tarea eliminada o un mensaje de error.
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
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Server error"));
        }
    }

    /**
     * Eliminar todas las tareas del usuario autenticado.
     *
     * @param sessionToken Token de sesión del usuario.
     * @return Un mensaje indicando cuántas tareas fueron eliminadas o un mensaje de error.
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
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Server error"));
        }
    }

    /**
     * Generar tareas de ejemplo para el usuario autenticado.
     *
     * @param sessionToken Token de sesión del usuario autenticado.
     * @return Un mensaje indicando cuántas tareas fueron generadas o un mensaje de error.
     */
    @PostMapping("/gen")
    public ResponseEntity<?> generateTasks(@RequestHeader("Authorization") String sessionToken) {
        try {
            UserModel userLogged = this.getUserFromSessions(sessionToken);
            authorizationService.adminResource(sessionToken);
            List<TaskModel> newTasks = taskService.generateExamples(userLogged);
            return ResponseEntity.status(201).body(newTasks.size() + " Tasks were generated");
        } catch (Exception e) {
            if (e instanceof AppException) {
                return ((AppException) e).getResponse();
            }
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Server error"));
        }
    }

    /**
     * Verificar el estado del servicio.
     *
     * @return Un mensaje indicando el estado del servidor.
     */
    @GetMapping("/health")
    public ResponseEntity<?> checkHealth() {
        HashMap<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "The server is up");
        return ResponseEntity.ok(response);
    }

    /**
     * Obtener el usuario autenticado desde el token de sesión.
     *
     * @param sessionToken Token de sesión del usuario.
     * @return El modelo de usuario autenticado o null si la sesión es inválida.
     * @throws SessionException Si la sesión no es válida.
     */
    private UserModel getUserFromSessions(String sessionToken) throws SessionException {
        if (sessionToken == null || !this.sessionService.isSessionActive(sessionToken)) {
            return null;
        }
        return this.sessionService.getUserFromSession(sessionToken);
    }
}
