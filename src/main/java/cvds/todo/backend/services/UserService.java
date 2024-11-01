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

/**
 * Servicio para la gestión de usuarios en el sistema.
 * Proporciona métodos para crear, actualizar, eliminar, validar y autenticar usuarios.
 */
@Service
public class UserService implements UsersService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Crea un nuevo usuario y lo guarda en la base de datos después de validarlo.
     *
     * @param user El usuario a crear.
     * @return El usuario creado.
     * @throws UserException Si el usuario no es válido o ya existe.
     */
    private UserModel createUser(UserModel user) throws UserException {
        this.validateUser(user);
        user.setId(UUID.randomUUID().toString());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    /**
     * Crea un usuario con permisos de usuario estándar.
     *
     * @param user El usuario a crear.
     * @return El usuario creado.
     * @throws UserException Si el usuario no es válido.
     */
    public UserModel createUserAsUser(UserModel user) throws UserException {
        user.setRoles(Role.ROLE_ADMIN.name());
        return this.createUser(user);
    }

    /**
     * Crea un usuario con permisos de administrador.
     *
     * @param user  El usuario a crear.
     * @param roles El rol del usuario.
     * @return El usuario creado.
     * @throws UserException Si el rol no es válido.
     */
    public UserModel createUserAsAdmin(UserModel user, String roles) throws UserException {
        this.validateRole(user.getRoles());
        user.setRoles(roles);
        return this.createUser(user);
    }

    /**
     * Actualiza un usuario existente.
     *
     * @param id   El ID del usuario a actualizar.
     * @param user Datos del usuario para actualizar.
     * @return El usuario actualizado.
     * @throws UserException Si el usuario no existe o los datos no son válidos.
     */
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

            userToUpdate.setRoles(user.getRoles() == null || user.getRoles().isEmpty() ? userToUpdate.getRoles() : user.getRoles());

            userRepository.save(userToUpdate);
            return userToUpdate;
        }

        throw new UserException.UserNotFoundException(user.getUsername());
    }

    /**
     * Obtiene todos los usuarios.
     *
     * @return Lista de todos los usuarios.
     * @throws UserException En caso de error al obtener los usuarios.
     */
    public List<UserModel> getAllUsers() throws UserException {
        return userRepository.findAll();
    }

    /**
     * Obtiene un usuario específico por su ID.
     *
     * @param id ID del usuario.
     * @return El usuario si se encuentra.
     * @throws UserException Si el usuario no se encuentra.
     */
    public UserModel getUserById(String id) throws UserException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserException.UserNotFoundException(id));
    }

    @Override
    public UserModel deleteUser(String id) throws UserException {  //by id, username or gmail?
        return null;
    }

    /**
     * Autentica a un usuario según su nombre de usuario y contraseña.
     *
     * @param username Nombre de usuario.
     * @param password Contraseña.
     * @return El usuario autenticado.
     * @throws UserException Si las credenciales son inválidas.
     */
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

    /**
     * Valida que un usuario sea correcto en cuanto a formato de nombre, email y contraseña.
     *
     * @param user El usuario a validar.
     * @throws UserException Si el usuario es nulo o sus datos no son válidos.
     */
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

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            throw new UserException.UserInvalidValueException("All users must have a role");
        }

        this.validateRole(user.getRoles());
    }

    /**
     * Valida el formato del nombre de usuario.
     *
     * @param username Nombre de usuario.
     * @throws UserException.UserInvalidValueException Si el nombre no es válido.
     */
    private void validateUsername(String username) throws UserException.UserInvalidValueException {
        if (username.length() > 30 || username.length() < 5) {
            throw new UserException.UserInvalidValueException("Username must be between 5 and 30 characters");
        }
        if (!Pattern.matches("^[a-zA-Z0-9._-]+$", username)) {
            throw new UserException.UserInvalidValueException("Username can only contain alphanumeric characters, dots, hyphens, and underscores");
        }
    }

    /**
     * Valida el formato del email.
     *
     * @param email Email del usuario.
     * @throws UserException.UserInvalidValueException Si el email no es válido.
     */
    private void validateEmail(String email) throws UserException.UserInvalidValueException {
        String emailPattern = "^[\\w.-]+@[\\w-]+(\\.[\\w-]+)+$";
        if (email == null || !Pattern.matches(emailPattern, email)) {
            throw new UserException.UserInvalidValueException("Email format is invalid");
        }
    }

    /**
     * Valida el formato de la contraseña.
     *
     * @param password Contraseña del usuario.
     * @throws UserException.UserInvalidValueException Si la contraseña no es válida.
     */
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

    /**
     * Valida el rol del usuario.
     *
     * @param role Rol a validar.
     * @throws UserException Si el rol no es válido.
     */
    public void validateRole(String role) throws UserException {
        try {
            Role.valueOf(role.toUpperCase());
        } catch (Exception e) {
            throw new UserException.UserInvalidValueException("role: " + role);
        }
    }
}
