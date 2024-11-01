package cvds.todo.backend.model;

/**
 * Modelo que representa las credenciales de inicio de sesión de un usuario.
 * Contiene el nombre de usuario y la contraseña que se utilizarán para autenticarse.
 */
public class LoginModel {
    private String username; // Nombre de usuario
    private String password; // Contraseña

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
     * Obtiene la contraseña.
     *
     * @return String La contraseña del usuario.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña.
     *
     * @param password La contraseña del usuario.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
