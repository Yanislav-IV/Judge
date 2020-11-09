package judge.service;

import javax.servlet.http.HttpSession;

public interface AuthenticationService {
    String getMessage();
    boolean isAuthorizedUser(HttpSession session);
    boolean isAuthorizedUser(HttpSession session, String role);

    String preAuthorization(HttpSession session, String path);
    String preAuthorization(HttpSession session, String path, String role);
}
