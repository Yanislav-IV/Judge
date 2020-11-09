package judge.model.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HomeworkServiceModel extends BaseServiceModel {

    private LocalDateTime addedOn = LocalDateTime.now();
    private String git;
    private UserServiceModel author;
    private ExerciseServiceModel exercise;

    public HomeworkServiceModel() {
    }

    public LocalDateTime getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDateTime addedOn) {
        this.addedOn = addedOn;
    }

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

    public ExerciseServiceModel getExercise() {
        return exercise;
    }

    public void setExercise(ExerciseServiceModel exercise) {
        this.exercise = exercise;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(this.exercise.toString())
                .append(System.lineSeparator())
                .append(String.format("added on %s",
                        this.addedOn.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                );
        return result.toString();
    }
}