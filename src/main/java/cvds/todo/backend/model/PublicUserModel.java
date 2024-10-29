package cvds.todo.backend.model;

import org.springframework.data.annotation.Id;

import java.util.HashSet;
import java.util.Set;

public class PublicUserModel {
    @Id
    private String id;
    private String username;
    private String email;
    private Set<String> roles = new HashSet<String>();

    public PublicUserModel(UserModel user) {
        username = user.getUsername();
        email = user.getEmail();
        id = user.getId();
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

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
