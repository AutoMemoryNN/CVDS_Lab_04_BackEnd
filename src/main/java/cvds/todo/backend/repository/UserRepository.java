package cvds.todo.backend.repository;
import cvds.todo.backend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
/**
 * Método de búsqueda que utiliza el nombre de usuario para encontrar al usuario en MongoDB.
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
}