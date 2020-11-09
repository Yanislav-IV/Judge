package judge.service;

import judge.model.entity.User;
import judge.model.service.UserServiceModel;
import judge.model.view.UserViewModel;

import java.util.Collection;


public interface UserService {
    UserServiceModel registerUser(UserServiceModel userServiceModel);
    UserServiceModel login(String username, String password);
    boolean isAuthorizedUser(UserServiceModel userServiceModel, String role);
    void grantRoleToUser(UserServiceModel admin, String username, String role);
    UserServiceModel[] getUsers();
    Collection<UserServiceModel> getTopScored();
    UserViewModel getById(String id);

    long getCount();
    double getAverageGrade();
    double getAverageGradeByUser(User user);
}
