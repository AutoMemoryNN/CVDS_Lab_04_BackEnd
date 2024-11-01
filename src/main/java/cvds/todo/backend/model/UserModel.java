package cvds.todo.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Modelo que representa un usuario en el sistema.
 * Contiene la información básica del usuario, incluyendo su identificación, nombre de usuario, correo, contraseña y roles.
 */
@Document(collection = "users")
public class UserModel {
    @Id
    private String id;  // Identificador único del usuario
    private String username;  // Nombre de usuario
    private String email;  // Correo electrónico del usuario
    private String password;  // Contraseña del usuario
    private String roles;  // Roles asignados al usuario, como "USER" o "ADMIN"

    public UserModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}