package cvds.todo.backend.services;

import cvds.todo.backend.exceptions.UserException;
import cvds.todo.backend.interfeces.UsersService;
import cvds.todo.backend.enums.Role;
import cvds.todo.backend.model.UserModel;
import cvds.todo.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

@Service
public class UserService implements UsersService {
    @Autowired
    private UserRepository userRepository;


    private UserModel createUser(UserModel user) throws UserException {
        this.validateUser(user);
        user.setId(UUID.randomUUID().toString());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    public UserModel createUserAsUser(UserModel user) throws UserException {
        user.setRole(Role.ROLE_ADMIN.name());
        return this.createUser(user);
    }

    public UserModel createUserAsAdmin(UserModel user, String roles) throws UserException {
        this.validateRole(user.getRole());
        user.setRole(roles);

        return this.createUser(user);
    }

    public UserModel updateUser(String id, UserModel user) throws UserException {
        Optional<UserModel> existingUser = userRepository.findById(id);

        if (existingUser.isPresent()) {
            UserModel userToUpdate = existingUser.get();

            userToUpdate.setUsername(user.getUsername() == null ? userToUpdate.getUsername() : user.getUsername());
            userToUpdate.setEmail(user.getEmail() == null ? userToUpdate.getEmail() : user.getEmail());

            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                userToUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
            }

            userToUpdate.setRole(user.getRole() == null || user.getRole().isEmpty() ? userToUpdate.getRole() : user.getRole());

            userRepository.save(userToUpdate);
            return userToUpdate;
        }

        throw new UserException.UserNotFoundException(user.getUsername());
    }


    public List<UserModel> getAllUsers() throws UserException {
        return userRepository.findAll();
    }

    public UserModel getUserById(String id) throws UserException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserException.UserNotFoundException(id));
    }

    @Override
    public UserModel deleteUser(String id) throws UserException {  //by id, username or gmail?
        return null;
    }

    public UserModel loginUser(String username, String password) throws UserException {
        UserModel userModel = this.userRepository.findByUsername(username);

        if (userModel == null) {
            throw new UserException.UserNotFoundException(username);
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (passwordEncoder.matches(password, userModel.getPassword())) {
            return userModel;
        } else {
            throw new UserException.UserInvalidValueException("Invalid credentials");
        }
    }

    public void validateUser(UserModel user) throws UserException {
        if (user == null) {
            throw new UserException.UserInvalidValueException("User cannot be null");
        }
        if (user.getUsername() == null || user.getPassword() == null || user.getEmail() == null) {
            throw new UserException.UserInvalidValueException("Username or password or email cannot be null");
        }
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new UserException.UserConflictException(user.getUsername());
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new UserException.UserConflictException(user.getEmail());
        }

        validateUsername(user.getUsername());
        validateEmail(user.getEmail());
        validatePassword(user.getPassword());

        if (user.getRole() == null || user.getRole().isEmpty()) {
            throw new UserException.UserInvalidValueException("All users must have a role");
        }

        this.validateRole(user.getRole());
    }

    private void validateUsername(String username) throws UserException.UserInvalidValueException {
        if (username.length() > 30 || username.length() < 5) {
            throw new UserException.UserInvalidValueException("Username must be between 5 and 30 characters");
        }
        if (!Pattern.matches("^[a-zA-Z0-9._-]+$", username)) {
            throw new UserException.UserInvalidValueException("Username can only contain alphanumeric characters, dots, hyphens, and underscores");
        }
    }

    private void validateEmail(String email) throws UserException.UserInvalidValueException {
        String emailPattern = "^[\\w.-]+@[\\w-]+(\\.[\\w-]+)+$";
        if (email == null || !Pattern.matches(emailPattern, email)) {
            throw new UserException.UserInvalidValueException("Email format is invalid");
        }
    }

    private void validatePassword(String password) throws UserException.UserInvalidValueException {
        if (password == null) {
            throw new UserException.UserInvalidValueException("Password cannot be null");
        }
        if (password.length() < 6 || password.length() > 29) {
            throw new UserException.UserInvalidValueException("Password must be between 6 and 29 characters");
        }
        if (!Pattern.matches("^[a-zA-Z0-9!@#$%^&*()\\-_=+]+$", password)) {
            throw new UserException.UserInvalidValueException("Illegal password, try another");
        }
    }

    public void validateRole(String role) throws UserException {
        try {
            Role.valueOf(role.toUpperCase());
        } catch (Exception e) {
            throw new UserException.UserInvalidValueException("role: " + role);
        }
    }

}
