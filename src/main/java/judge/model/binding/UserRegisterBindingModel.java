package judge.model.binding;

import javax.validation.constraints.*;

import static judge.constant.Constants.*;

public class UserRegisterBindingModel {

    private String username;
    private String password;
    private String confirmPassword;
    private String email;
    private String git;

    public UserRegisterBindingModel() {
    }

    @NotEmpty(message = USER_NAME_CANNOT_BE_EMPTY_MESSAGE)
    @Size(min = USERNAME_LENGTH_MIN, max = USERNAME_LENGTH_MAX, message = USERNAME_LENGTH_MESSAGE)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NotEmpty(message = USER_PASSWORD_CANNOT_BE_EMPTY_MESSAGE)
    @Size(min = USER_PASSWORD_LENGTH_MIN, max = USER_PASSWORD_LENGTH_MAX, message = USER_PASSWORD_LENGTH_MESSAGE)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NotEmpty(message = USER_PASSWORD_CANNOT_BE_EMPTY_MESSAGE)
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @NotEmpty(message = USER_EMAIL_CANNOT_BE_EMPTY_MESSAGE)
    @Email(regexp = USER_EMAIL_REGEX, message = USER_EMAIL_MESSAGE)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotEmpty(message = USER_GITHUB_CANNOT_BE_EMPTY_MESSAGE)
    @Pattern(regexp = USER_GITHUB_PATTERN, message = USER_GITHUB_MESSAGE2)
    public String getGit() {
        return git;
    }

    public void setGit(String git) {
        this.git = git;
    }
}
