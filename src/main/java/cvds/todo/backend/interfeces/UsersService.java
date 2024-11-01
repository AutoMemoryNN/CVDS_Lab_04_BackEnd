package cvds.todo.backend.interfeces;

import cvds.todo.backend.exceptions.AppException;
import cvds.todo.backend.model.UserModel;

import java.util.List;
import java.util.Set;

/**
 * Interfaz para gestionar operaciones de usuario en el sistema.
 * Proporciona métodos para crear, actualizar, obtener, eliminar y autenticar usuarios.
 */
public interface UsersService {

    /**
     * Crea un usuario con permisos de administrador.
     *
     * @param user  Modelo de usuario a crear.
     * @param role  Rol asignado al usuario.
     * @return UserModel El objeto de usuario creado con los detalles actualizados.
     * @throws AppException Si ocurre un error durante la creación del usuario.
     */
    UserModel createUserAsAdmin(UserModel user, String role) throws AppException;

    /**
     * Crea un usuario con permisos estándar.
     *
     * @param user Modelo de usuario a crear.
     * @return UserModel El objeto de usuario creado con los detalles actualizados.
     * @throws AppException Si ocurre un error durante la creación del usuario.
     */
    UserModel createUserAsUser(UserModel user) throws AppException;

    /**
     * Actualiza la información de un usuario específico.
     *
     * @param id    Identificador único del usuario.
     * @param user  Modelo de usuario con los datos actualizados.
     * @return UserModel El objeto de usuario actualizado.
     * @throws AppException Si ocurre un error durante la actualización del usuario.
     */
    UserModel updateUser(String id, UserModel user) throws AppException;

    /**
     * Recupera una lista de todos los usuarios en el sistema.
     *
     * @return List<UserModel> Lista de objetos de usuario.
     * @throws AppException Si ocurre un error al recuperar la lista de usuarios.
     */
    List<UserModel> getAllUsers() throws AppException;

    /**
     * Recupera un usuario específico por su identificador.
     *
     * @param id Identificador único del usuario.
     * @return UserModel El objeto de usuario, si existe.
     * @throws AppException Si el usuario no existe o si ocurre un error.
     */
    UserModel getUserById(String id) throws AppException;

    /**
     * Elimina un usuario específico por su identificador.
     *
     * @param id Identificador único del usuario.
     * @return UserModel El objeto de usuario eliminado.
     * @throws AppException Si el usuario no existe o si ocurre un error durante la eliminación.
     */
    UserModel deleteUser(String id) throws AppException;

    /**
     * Autentica un usuario basado en su nombre de usuario y contraseña.
     *
     * @param username Nombre de usuario para la autenticación.
     * @param password Contraseña del usuario.
     * @return UserModel El objeto de usuario autenticado.
     * @throws AppException Si las credenciales son inválidas o si ocurre un error de autenticación.
     */
    UserModel loginUser(String username, String password) throws AppException;
}
