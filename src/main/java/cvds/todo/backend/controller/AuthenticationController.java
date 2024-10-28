package cvds.todo.backend.controller;

import cvds.todo.backend.exceptions.UserException;
import cvds.todo.backend.model.LoginModel;
import cvds.todo.backend.model.UserModel;
import cvds.todo.backend.services.SessionService;
import cvds.todo.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Collections;

@Controller
public class AuthenticationController {
    @Autowired
    private UserService userService;

    @Autowired
    private SessionService sessionService;

    @PostMapping("/auth")
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

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestHeader("Authentication") String token) {
        try {
            this.sessionService.invalidateSession(token);
            return ResponseEntity.ok().body(Collections.singletonMap("message", "User logged out"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Server error"));
        }
    }

}
