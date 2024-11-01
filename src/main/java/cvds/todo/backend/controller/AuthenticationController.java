package cvds.todo.backend.controller;

import cvds.todo.backend.exceptions.UserException;
import cvds.todo.backend.model.LoginModel;
import cvds.todo.backend.model.PublicUserModel;
import cvds.todo.backend.model.UserModel;
import cvds.todo.backend.services.SessionService;
import cvds.todo.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

/**
 * Controlador REST para la autenticación de usuarios.
 * Proporciona endpoints para el inicio de sesión, obtención de información del usuario autenticado y cierre de sesión.
 */
@RequestMapping("/")
@RestController
public class AuthenticationController {

    @Autowired
    private UserService userService; // Servicio de lógica de usuario

    @Autowired
    private SessionService sessionService; // Servicio de gestión de sesiones

    /**
     * Iniciar sesión.
     * Endpoint para autenticar un usuario en el sistema. Al recibir las credenciales,
     * intenta autenticar al usuario y, si es exitoso, genera una cookie de sesión.
     *
     * @param login Un objeto LoginModel que contiene las credenciales de inicio de sesión (username y password).
     * @return ResponseEntity con la cookie de sesión en caso de éxito o un mensaje de error en caso de fallo.
     */
    @PostMapping("auth")
    public ResponseEntity<?> loginUser(@RequestBody LoginModel login) {
        try {
            UserModel user = userService.loginUser(login.getUsername(), login.getPassword());
            return ResponseEntity.ok().body(Collections.singletonMap("cookie", this.sessionService.createSessionCookie(user)));
        } catch (Exception e) {
            if (e instanceof UserException) {
                return ((UserException) e).getResponse();
            }
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Server error"));
        }
    }

    /**
     * Obtener información del usuario autenticado.
     * Endpoint para validar el estado de la sesión y devolver la información pública del usuario autenticado.
     *
     * @param token El token de autorización en el encabezado de la solicitud.
     * @return ResponseEntity con el modelo PublicUserModel que contiene la información del usuario si la sesión es válida,
     * o un mensaje de error si hay problemas con la autenticación o la sesión.
     */
    @GetMapping("auth")
    public ResponseEntity<?> getUser(@RequestHeader("Authorization") String token) {
        try {
            sessionService.isSessionActive(token);
            return ResponseEntity.ok().body(new PublicUserModel(sessionService.getUserFromSession(token)));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Server error"));
        }
    }

    /**
     * Cerrar sesión.
     * Endpoint para cerrar la sesión del usuario autenticado invalidando su token de sesión.
     *
     * @param token El token de autenticación en el encabezado de la solicitud.
     * @return ResponseEntity con un mensaje de éxito si la sesión fue cerrada correctamente o un mensaje de error en caso de fallo.
     */
    @PostMapping("logout")
    public ResponseEntity<?> logoutUser(@RequestHeader("Authentication") String token) {
        try {
            this.sessionService.invalidateSession(token);
            return ResponseEntity.ok().body(Collections.singletonMap("message", "User logged out"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Server error"));
        }
    }

}
