package judge.service.impl;

import judge.exception.AlreadyExistsException;
import judge.exception.EntityNotFoundException;
import judge.model.entity.Comment;
import judge.model.view.UserViewModel;
import judge.service.RoleService;
import judge.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import judge.model.entity.User;
import judge.model.service.UserServiceModel;
import judge.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static judge.constant.Constants.*;
import static judge.constant.Constants.ROLE_ADMIN;
import static judge.constant.Constants.ROLE_USER;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final RoleService roleService;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository repository, RoleService roleService, ModelMapper mapper, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.roleService = roleService;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserServiceModel registerUser(UserServiceModel userServiceModel) {

        // first run -> ADMIN
        userServiceModel.setRole(roleService
                        .findByName(repository.count() == 0 ? ROLE_ADMIN : ROLE_USER));

        if (repository.existsByUsername(userServiceModel.getUsername())) {
            throw new AlreadyExistsException(USER_NAME_FIELD, USER_NAME_EXISTS_MESSAGE);
        }
        if (repository.existsByEmail(userServiceModel.getEmail())) {
            throw new AlreadyExistsException(USER_EMAIL_FIELD, USER_EMAIL_EXISTS_MASSAGE);
        }
        if (repository.existsByGit(userServiceModel.getGit())) {
            throw new AlreadyExistsException(USER_GITHUB_FIELD, USER_GITHUB_EXISTS_MASSAGE);
        }

        User user = this.mapper.map(userServiceModel, User.class);

        user.setPassword(passwordEncoder.encode(userServiceModel.getPassword()));

        return this.mapper.map(repository.saveAndFlush(user), UserServiceModel.class);
    }

    @Override
    public UserServiceModel login(String username, String password) {
        User user = repository.findByUsername(username).orElse(null);

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new NoSuchElementException(USER_INVALID);
        }

        return mapper.map(user, UserServiceModel.class);
    }

    @Override
    public boolean isAuthorizedUser(UserServiceModel userServiceModel, String role) {

        // is logged
        if (userServiceModel == null) {
            return false;
        }

        // exists
        User user = repository.findById(userServiceModel.getId()).orElse(null);

        if (user == null) {
            return false;
        }

        // username
        if (!user.getUsername().equals(userServiceModel.getUsername())) {
            return false;
        }

        // password
        if (!userServiceModel.getPassword().equals(user.getPassword())) {
            return false;
        }

        // role (Optional)
        return role == null || user.getRole().getName().equals(role);
    }

    @Override
    public void grantRoleToUser(UserServiceModel admin, String username, String role) {
        if (isAuthorizedUser(admin, ROLE_ADMIN)) {
            try {
                User user = repository.findByUsername(username).orElseThrow();
                UserServiceModel userServiceModel = mapper.map(user, UserServiceModel.class);
                userServiceModel.setRole(roleService.findByName(role));
                repository.save(mapper.map(userServiceModel, User.class));

            } catch (Exception ex) {
                throw new EntityNotFoundException("User not found. Unable to grant authority.");
            }

        } else {
            throw new SecurityException(USER_UNAUTHORIZED);
        }
    }

    @Override
    public UserServiceModel[] getUsers() {

        return repository.findAll()
                .stream()
                .map(user -> mapper.map(user, UserServiceModel.class))
                .toArray(UserServiceModel[]::new);
    }

    @Override
    public Collection<UserServiceModel> getTopScored() {

        return repository.findAllByScore()
                .stream()
                .map(user -> mapper.map(user, UserServiceModel.class))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public UserViewModel getById(String id) {

       return this.repository
               .findById(id)
               .map(user -> mapper.map(user, UserViewModel.class))
               .orElse(null);
    }

    @Override
    public long getCount() {
        return repository.count();
    }

    @Override
    public double getAverageGrade() {

        return repository.findAll()
                .stream()
                .mapToDouble(this::getAverageGradeByUser)
                .average()
                .orElse(0.0);
    }

    @Override
    public double getAverageGradeByUser(User user) {
        
        return user.getComments().stream().mapToDouble(Comment::getScore).average().orElse(0.0);
    }
}
