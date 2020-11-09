package judge.service.impl;

import judge.model.entity.Comment;
import judge.model.entity.Homework;
import judge.model.service.CommentServiceModel;
import judge.repository.CommentRepository;
import judge.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository repository;
    private final ModelMapper mapper;

    @Autowired
    public CommentServiceImpl(CommentRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void addComment(CommentServiceModel commentServiceModel, Homework homework) {
        Comment comment = mapper.map(commentServiceModel, Comment.class);
        comment.setHomework(homework);
        repository.saveAndFlush(comment);
    }

    @Override
    public long getCountByHomework(Homework homework) {
        return repository.getCountByHomework(homework);
    }

    @Override
    public Map<Integer, Integer> getAllGradesCount() {
        Map<Integer, Integer> grades = new HashMap<>();
        grades.put(2, 0);
        grades.put(3, 0);
        grades.put(4, 0);
        grades.put(5, 0);
        grades.put(6, 0);
        repository.findAll().forEach(comment -> grades.put(comment.getScore(), grades.get(comment.getScore()) + 1));
        return grades;
    }
}
