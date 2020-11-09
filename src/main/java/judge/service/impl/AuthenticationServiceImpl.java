package judge.service.impl;

import judge.model.service.UserServiceModel;
import judge.service.AuthenticationService;
import judge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private String message;
    private final UserService userService;

    @Autowired
    public AuthenticationServiceImpl(UserService userService) {
        this.userService = userService;
    }


    @Override
    public String getMessage(){
        return this.message;
    }

    @Override
    public boolean isAuthorizedUser(HttpSession session) {
        return isAuthorizedUser(session, null);
    }

    @Override
    public boolean isAuthorizedUser(HttpSession session, String role) {

        if(session == null) {
            return false;
        }

        Object sessionUser = session.getAttribute("user");
        this.message = null;

        if(sessionUser != null) {
            UserServiceModel user = (UserServiceModel) sessionUser;
            try {
                return userService.isAuthorizedUser(user, role);

            } catch (Exception e) {
                this.message = e.getMessage();
            }
        }
        return false;
    }

    @Override
    public String preAuthorization(HttpSession session, String path) {
        return preAuthorization(session, path, null);
    }

    @Override
    public String preAuthorization(HttpSession session, String path, String role) {
        return this.isAuthorizedUser(session, role) ? path : "redirect:/";
    }
}
