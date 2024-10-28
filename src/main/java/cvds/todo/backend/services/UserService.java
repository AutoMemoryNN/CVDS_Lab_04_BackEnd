package cvds.todo.backend.services;

import cvds.todo.backend.interfeces.UsersService;
import cvds.todo.backend.model.Role;
import cvds.todo.backend.model.UserModel;
import cvds.todo.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class UserService implements UsersService {
    @Autowired
    private UserRepository userRepository;


    private UserModel createUser(UserModel user) {
        user.setId(UUID.randomUUID().toString());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        this.validateUser(user);
        userRepository.save(user);
        return user;
    }

    public UserModel createUserAsUser(UserModel user) {
        final HashSet<String> roles = new HashSet<String>(1);
        roles.add(Role.USER.name());
        user.setRoles(roles);

        return this.createUser(user);
    }

    public UserModel createUserAsAdmin(UserModel user, Set<String> roles) {
        for (String role : roles) {
            this.validateRole(role);
        }
        user.setRoles(roles);

        return this.createUser(user);
    }

    public UserModel updateUser(String id, UserModel user) throws UsernameNotFoundException {
        Optional<UserModel> existingUser = userRepository.findById(id);

        if (existingUser.isPresent()) {
            UserModel userToUpdate = existingUser.get();

            userToUpdate.setUsername(user.getUsername() == null ? userToUpdate.getUsername() : user.getUsername());
            userToUpdate.setEmail(user.getEmail() == null ? userToUpdate.getEmail() : user.getEmail());

            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                userToUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
            }

            userToUpdate.setRoles(user.getRoles() == null || user.getRoles().isEmpty() ? userToUpdate.getRoles() : user.getRoles());

            userRepository.save(userToUpdate);
            return userToUpdate;
        }

        throw new UsernameNotFoundException("User with ID " + id + " not found");
    }


    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    public UserModel getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public UserModel deleteUser(String id) {  //by id, username or gmail?
        return null;
    }

    public UserModel loginUser(String username, String password) {
        UserModel userModel = this.userRepository.findByUsername(username);

        if (userModel == null) {
            throw new UsernameNotFoundException("User not found");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (passwordEncoder.matches(password, userModel.getPassword())) {
            return userModel;
        } else {
            throw new UsernameNotFoundException("Invalid credentials");
        }
    }

    public void validateUser(UserModel user) {
        if (user == null) {
            throw new InvalidParameterException("User cannot be null");
        }
        if (user.getUsername() == null || user.getPassword() == null || user.getId() == null) {
            throw new InvalidParameterException("Username, password and id cannot be null");
        }
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new InvalidParameterException("Username already exists");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new InvalidParameterException("Email already exists");
        }
        String username = user.getUsername();
        if (username.length() > 30 || username.length() < 5) {
            throw new InvalidParameterException("Username cannot exceed 30 characters and cannot be less than 5 characters");
        }
        if (!Pattern.matches("^[a-zA-Z0-9.!@#$%^&*()_+=-]+$", username)) {
            throw new InvalidParameterException("Username can only contain alphanumeric characters, hyphens, and underscores");
        }
        String email = user.getEmail();
        if (email == null || !Pattern.matches("^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$", email)) { //TODO : Solve
            throw new InvalidParameterException("Email format is invalid");
        }
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            throw new InvalidParameterException("All users must have a role");
        }
        for (String role : user.getRoles()) {
            this.validateRole(role);
        }
    }

    public void validateRole(String role) throws InvalidParameterException {
        try {
            Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidParameterException("Invalid role: " + e.getMessage());
        }
    }

}
