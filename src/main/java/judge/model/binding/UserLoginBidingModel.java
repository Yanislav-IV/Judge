package judge.model.binding;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import static judge.constant.Constants.*;

public class UserLoginBidingModel {
    private String username;
    private String password;

    public UserLoginBidingModel() {
    }

    @NotEmpty(message = USER_NAME_CANNOT_BE_EMPTY_MESSAGE)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NotEmpty(message = USER_PASSWORD_CANNOT_BE_EMPTY_MESSAGE)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
