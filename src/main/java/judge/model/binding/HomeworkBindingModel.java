package judge.model.binding;

import judge.constant.Constants;
import judge.model.service.ExerciseServiceModel;
import judge.model.service.UserServiceModel;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

public class HomeworkBindingModel {

    private LocalDateTime addedOn = LocalDateTime.now();
    private String git;
    private UserServiceModel author;
    private ExerciseServiceModel exercise;

    public HomeworkBindingModel() {
    }

    public LocalDateTime getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDateTime addedOn) {
        this.addedOn = addedOn;
    }

    @NotEmpty
    @Pattern(regexp = Constants.USER_GITHUB_PATTERN, message = Constants.USER_GITHUB_MESSAGE)
    public String getGit() {
        return git;
    }

    public void setGit(String git) {
        this.git = git;
    }

    public UserServiceModel getAuthor() {
        return author;
    }

    public void setAuthor(UserServiceModel author) {
        this.author = author;
    }

    @NotNull
    public ExerciseServiceModel getExercise() {
        return exercise;
    }

    public void setExercise(ExerciseServiceModel exercise) {
        this.exercise = exercise;
    }
}
