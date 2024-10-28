package cvds.todo.backend.interfeces;

import cvds.todo.backend.model.UserModel;

import java.util.List;
import java.util.Set;

public interface UsersService {
    public UserModel createUserAsAdmin(UserModel user, Set<String> role);
    public UserModel createUserAsUser(UserModel user);
    public UserModel updateUser(String id, UserModel user);
    public List<UserModel> getAllUsers();
    public UserModel getUserById(String id);
    public UserModel deleteUser(String id);
    public UserModel loginUser(String username, String password);
}
