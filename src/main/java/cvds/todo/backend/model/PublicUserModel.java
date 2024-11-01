package cvds.todo.backend.model;

import org.springframework.data.annotation.Id;

import java.util.HashSet;
import java.util.Set;

/**
 * Modelo que representa la información pública de un usuario.
 * Este modelo se utiliza para mostrar datos no sensibles del usuario en respuestas públicas.
 */
public class PublicUserModel {

    @Id
    private String id;  // Identificador único del usuario
    private String username;  // Nombre de usuario
    private String email;  // Correo electrónico del usuario
    private Set<String> roles = new HashSet<String>();  // Roles asignados al usuario

    /**
     * Constructor que inicializa el modelo con datos del objeto UserModel.
     *
     * @param user Objeto UserModel del cual se extraerán los datos públicos.
     */
    public PublicUserModel(UserModel user) {
        username = user.getUsername();
        email = user.getEmail();
        id = user.getId();
    }

    /**
     * Obtiene el ID del usuario.
     *
     * @return String ID del usuario.
     */
    public String getId() {
        return id;
    }

    /**
     * Establece el ID del usuario.
     *
     * @param id El ID del usuario.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de usuario.
     *
     * @return String El nombre de usuario.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Establece el nombre de usuario.
     *
     * @param username El nombre de usuario.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Obtiene el correo electrónico del usuario.
     *
     * @return String El correo electrónico del usuario.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del usuario.
     *
     * @param email El correo electrónico del usuario.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene los roles asignados al usuario.
     *
     * @return Set<String> Los roles del usuario.
     */
    public Set<String> getRoles() {
        return roles;
    }

    /**
     * Establece los roles del usuario.
     *
     * @param roles Los roles a asignar al usuario.
     */
    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
