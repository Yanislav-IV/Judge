package judge.model.service;

import javax.validation.constraints.NotNull;

public class CommentServiceModel extends BaseServiceModel {
    private int score;
    private String textContent;
    private UserServiceModel author;
    private HomeworkServiceModel homework;

    public CommentServiceModel() {
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    @NotNull
    public UserServiceModel getAuthor() {
        return author;
    }

    public void setAuthor(UserServiceModel author) {
        this.author = author;
    }

    @NotNull
    public HomeworkServiceModel getHomework() {
        return homework;
    }

    public void setHomework(HomeworkServiceModel homework) {
        this.homework = homework;
    }
}
