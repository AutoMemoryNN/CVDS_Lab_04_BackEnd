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

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorizationService authorizationService;

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