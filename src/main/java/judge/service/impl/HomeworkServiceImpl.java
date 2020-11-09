package judge.service.impl;

import judge.constant.Constants;
import judge.exception.AlreadyExistsException;
import judge.model.entity.Homework;
import judge.model.service.CommentServiceModel;
import judge.model.service.ExerciseServiceModel;
import judge.model.service.HomeworkServiceModel;
import judge.model.service.UserServiceModel;
import judge.repository.HomeworkRepository;
import judge.service.CommentService;
import judge.service.ExerciseService;
import judge.service.HomeworkService;
import judge.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

import static judge.constant.Constants.HOMEWORK_NOT_FOUND;
import static judge.constant.Constants.HOMEWORK_TOO_LATE;

@Service
public class HomeworkServiceImpl implements HomeworkService {

    private final HomeworkRepository repository;
    private final ModelMapper mapper;
    private final UserService userService;
    private final CommentService commentService;
    private final ExerciseService exerciseService;

    @Autowired
    public HomeworkServiceImpl(HomeworkRepository repository, ModelMapper mapper, UserService userService, CommentService commentService, ExerciseService exerciseService) {
        this.repository = repository;
        this.mapper = mapper;
        this.userService = userService;
        this.commentService = commentService;
        this.exerciseService = exerciseService;
    }

    @Override
    public void addHomework(HomeworkServiceModel homeworkServiceModel) {

        if(homeworkServiceModel == null) {
            throw new IllegalArgumentException(HOMEWORK_NOT_FOUND);
        }

        UserServiceModel userServiceModel = homeworkServiceModel.getAuthor();
        if (!userService.isAuthorizedUser(userServiceModel, null)) {
            throw new SecurityException(Constants.USER_UNAUTHORIZED);
        }

        if (homeworkServiceModel.getExercise().getDueDate().isBefore(homeworkServiceModel.getAddedOn())) {
            throw new IllegalArgumentException(HOMEWORK_TOO_LATE);
        }

        Homework current = repository
                .findFirstByAuthor_IdAndExercise_Name(
                        userServiceModel.getId(),
                        homeworkServiceModel.getExercise().getName()
                )
                .orElse(null);

        // check git address
        if (repository.findAllByGit(homeworkServiceModel.getGit())
                .stream()
                .anyMatch(homework -> current == null || !homework.getId().equals(current.getId()))) {

            throw new AlreadyExistsException("git", Constants.HOMEWORK_GITHUB_EXISTS_MESSAGE);
        }

        if (current == null) {
            // add
            repository.save(mapper.map(homeworkServiceModel, Homework.class));

        } else {
            // update
            current.setAddedOn(LocalDateTime.now());
            current.setGit(homeworkServiceModel.getGit());
            repository.save(current);
        }
    }

    @Override
    public void addCommentToHomework(CommentServiceModel commentServiceModel) {
        repository.findById(commentServiceModel.getHomework().getId())
                .ifPresent(homework -> commentService.addComment(commentServiceModel, homework));
    }

    @Override
    public HomeworkServiceModel getLastAdded(String authorId) {
        Homework homework = repository.findFirstByAuthor_IdOrderByAddedOnDesc(authorId).orElse(null);
        return homework == null ? null : mapper.map(homework, HomeworkServiceModel.class);
    }

    @Override
    public HomeworkServiceModel getOneToCheck(String currentUserId) {

        ExerciseServiceModel lastExercise = exerciseService.getLast();

        if (lastExercise != null &&
                lastExercise.getDueDate().isAfter(LocalDateTime.now())) {

            Homework homework = repository.findAllByExercise_Id(lastExercise.getId())
                    .stream()
                    // remove current user
                    .filter(h -> !h.getAuthor().getId().equals(currentUserId))
                    // min comments
                    .min(Comparator.comparingLong(commentService::getCountByHomework))
                    .orElse(null);

            if(homework != null) {
                return mapper.map(homework, HomeworkServiceModel.class);
            }
        }
        return null;
    }

    @Override
    public String getHomework(String userId) {
        Collection<Homework> collection = repository.findAllByAuthor_Id(userId);
        String result = collection
                .stream()
                .map(homework -> mapper.map(homework, HomeworkServiceModel.class))
                .map(HomeworkServiceModel::toString)
                .collect(Collectors.joining(System.lineSeparator()));
        return result;
    }
}
