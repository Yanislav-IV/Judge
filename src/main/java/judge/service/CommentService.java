package judge.service;

import judge.model.entity.Homework;
import judge.model.service.CommentServiceModel;

import java.util.Map;

public interface CommentService {
    void addComment(CommentServiceModel commentServiceModel, Homework homework);
    long getCountByHomework(Homework homework);
    Map<Integer, Integer> getAllGradesCount();
}
