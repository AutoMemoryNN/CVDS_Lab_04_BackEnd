package cvds.todo.backend.controller;

import cvds.todo.backend.exceptions.UserException;
import cvds.todo.backend.model.UserModel;
import cvds.todo.backend.services.AuthorizationService;
import cvds.todo.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Set;

/**
 * Controlador REST para la gestión de usuarios en el sistema.
 * Proporciona endpoints para la creación, obtención, actualización y eliminación de usuarios.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService; // Servicio para la lógica de negocio de usuarios

    @Autowired
    private AuthorizationService authorizationService; // Servicio para la autorización de acciones de usuario

    /**
     * Crear un usuario con permisos estándar.
     *
     * @param user Objeto UserModel con los datos del usuario a crear.
     * @return Un mensaje indicando si el usuario fue creado exitosamente o un mensaje de error.
     */
    @PostMapping
    public ResponseEntity<?> createUserAsUser(@RequestBody UserModel user) {
        try {
            final UserModel modelUser = userService.createUserAsUser(user);
            return ResponseEntity.status(201).body(Collections.singletonMap("message", "The user " + modelUser.getUsername() + " was created successfully."));
        } catch (Exception e) {
            if (e instanceof UserException) {
                return ((UserException) e).getResponse();
            }
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Server error"));
        }
    }

    /**
     * Crear un usuario con permisos de administrador.
     *
     * @param token Token de autorización del usuario solicitante.
     * @param user  Objeto UserModel con los datos del usuario a crear.
     * @param roles Roles a asignar al usuario.
     * @return Un mensaje indicando si el usuario fue creado exitosamente o un mensaje de error.
     */
    @PostMapping("/admin")
    public ResponseEntity<?> createUserAsAdmin(@RequestHeader("Authorizacion") String token, @RequestBody UserModel user, @RequestBody String roles) {
        try {
            final UserModel modelUser = userService.createUserAsAdmin(user, roles);
            this.authorizationService.adminResource(token);
            return ResponseEntity.status(201).body(Collections.singletonMap("message", "The user " + modelUser.getUsername() + " was created successfully."));
        } catch (Exception e) {
            if (e instanceof UserException) {
                return ((UserException) e).getResponse();
            }
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Server error"));
        }
    }

    /**
     * Obtener un usuario por su ID.
     *
     * @param token Token de autorización del usuario solicitante.
     * @param id    Identificador del usuario a obtener.
     * @return El usuario correspondiente al ID o un mensaje de error.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@RequestHeader("Authorizacion") String token, @PathVariable String id) {
        try {
            this.authorizationService.adminResource(token);
            return ResponseEntity.ok(userService.getUserById(id));
        } catch (Exception e) {
            if (e instanceof UserException) {
                return ((UserException) e).getResponse();
            }
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Server error"));
        }
    }

    /**
     * Obtener todos los usuarios del sistema.
     *
     * @param token Token de autorización del usuario solicitante.
     * @return Lista de todos los usuarios o un mensaje de error.
     */
    @GetMapping
    public ResponseEntity<?> getAllUsers(@RequestHeader("Authorizacion") String token) {
        try {
            this.authorizationService.adminResource(token);
            return ResponseEntity.ok(userService.getAllUsers());
        } catch (Exception e) {
            if (e instanceof UserException) {
                return ((UserException) e).getResponse();
            }
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Server error"));
        }
    }

    /**
     * Actualizar un usuario existente.
     *
     * @param token Token de autorización del usuario solicitante.
     * @param id    Identificador del usuario a actualizar.
     * @param user  Objeto UserModel con los datos actualizados del usuario.
     * @return El usuario actualizado o un mensaje de error.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestHeader("Authorizacion") String token, @PathVariable String id, @RequestBody UserModel user) {
        try {
            this.authorizationService.adminResource(token);
            return ResponseEntity.ok(userService.updateUser(id, user));
        } catch (Exception e) {
            if (e instanceof UserException) {
                return ((UserException) e).getResponse();
            }
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Server error"));
        }
    }

    /**
     * Eliminar un usuario por su ID.
     *
     * @param token Token de autorización del usuario solicitante.
     * @param id    Identificador del usuario a eliminar.
     * @return Un mensaje de éxito o un mensaje de error.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorizacion") String token, @PathVariable String id) {
        try {
            userService.deleteUser(id);
            this.authorizationService.adminResource(token);
            return ResponseEntity.ok(Collections.singletonMap("message", "User deleted successfully"));
        } catch (Exception e) {
            if (e instanceof UserException) {
                return ((UserException) e).getResponse();
            }
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Server error"));
        }
    }
}
