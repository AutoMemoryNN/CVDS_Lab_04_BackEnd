package cvds.todo.backend.controller;

import cvds.todo.backend.model.LoginModel;
import cvds.todo.backend.model.UserModel;
import cvds.todo.backend.services.SessionService;
import cvds.todo.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SessionService sessionService;

    @PostMapping
    public ResponseEntity<?> createUserAsUser(@RequestBody UserModel user) {
        try {
            final UserModel modelUser = userService.createUserAsUser(user);
            return ResponseEntity.status(201).body("The user " + modelUser.getUsername() + " was created successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(499).body(e.getMessage());
        }
    }

    @PostMapping("/admin")
    public ResponseEntity<?> createUserAsAdmin(@RequestBody UserModel user, @RequestBody Set<String> roles) {
        try {
            final UserModel modelUser = userService.createUserAsAdmin(user, roles);
            return ResponseEntity.status(201).body("The user " + modelUser.getUsername() + " was created successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(499).body(e.getMessage());
        }
    }

    @PostMapping("/auth")
    public ResponseEntity<?> loginUser(@RequestBody LoginModel login) {
        try {
            UserModel user = userService.loginUser(login.getUsername(), login.getPassword());
            if (user == null) {
                return ResponseEntity.status(401).body("Invalid username or password.");
            }

            HashMap<String, String> response = new HashMap<>(1);

            response.put("cookie", this.sessionService.createSessionCookie(user));

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.status(499).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        try {
            UserModel user = userService.getUserById(id);
            if (user != null) {
                return ResponseEntity.ok(user);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(499).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            List<UserModel> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(499).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody UserModel user) {
        try {
            UserModel updatedUser = userService.updateUser(id, user);
            if (updatedUser != null) {
                return ResponseEntity.ok(updatedUser);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(499).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            if (userService.deleteUser(id) != null) {
                return ResponseEntity.ok("User deleted successfully");
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(499).body(e.getMessage());
        }
    }
}