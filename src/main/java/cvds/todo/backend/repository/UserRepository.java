package cvds.todo.backend.repository;

import cvds.todo.backend.model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la gestión de usuarios en la base de datos MongoDB.
 * Proporciona métodos para realizar consultas en la colección "users" basada en MongoDB.
 */
@Repository
public interface UserRepository extends MongoRepository<UserModel, String> {

    /**
     * Encuentra un usuario por su nombre de usuario.
     *
     * @param username Nombre de usuario del usuario.
     * @return UserModel El usuario que coincide con el nombre de usuario, o null si no se encuentra.
     */
    UserModel findByUsername(String username);

    /**
     * Encuentra un usuario por su correo electrónico.
     *
     * @param email Correo electrónico del usuario.
     * @return UserModel El usuario que coincide con el correo electrónico, o null si no se encuentra.
     */
    UserModel findByEmail(String email);
}
