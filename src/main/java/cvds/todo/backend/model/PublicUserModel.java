package cvds.todo.backend.model;

import org.springframework.data.annotation.Id;

public class PublicUserModel {
    @Id
    private String id;
    private String username;
    private String email;
    private String role;

    public PublicUserModel(UserModel user) {
        username = user.getUsername();
        email = user.getEmail();
        id = user.getId();
        role = user.getRole();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
