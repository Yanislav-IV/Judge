package judge.model.binding;

import javax.validation.constraints.NotEmpty;

public class RoleAddBindingModel {

    private String username;
    private String role;

    public RoleAddBindingModel() {
    }

    public RoleAddBindingModel(String username, String role) {
        this.username = username;
        this.role = role;
    }

    @NotEmpty
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NotEmpty
    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
