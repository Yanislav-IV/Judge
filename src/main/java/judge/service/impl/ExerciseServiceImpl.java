package judge.service.impl;

import judge.constant.Constants;
import judge.exception.AlreadyExistsException;
import judge.model.entity.Exercise;
import judge.model.service.ExerciseServiceModel;
import judge.repository.ExerciseRepository;
import judge.service.ExerciseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

import static judge.constant.Constants.EXERCISE_NAME_EXISTS_MESSAGE;
import static judge.constant.Constants.EXERCISE_NAME_FIELD;

@Service
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository repository;
    private final ModelMapper mapper;

    @Autowired
    public ExerciseServiceImpl(ExerciseRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }


    @Override
    public void addExercise(ExerciseServiceModel exerciseServiceModel) {

        if(repository.existsByName(exerciseServiceModel.getName())) {
            throw new AlreadyExistsException(EXERCISE_NAME_FIELD, EXERCISE_NAME_EXISTS_MESSAGE);
        }

        repository.saveAndFlush(mapper.map(exerciseServiceModel, Exercise.class));
    }

    @Override
    public ExerciseServiceModel[] getExercises() {
        return repository.findAll()
                .stream()
                .map(exercise -> mapper.map(exercise, ExerciseServiceModel.class))
                .toArray(ExerciseServiceModel[]::new);
    }

    @Override
    public ExerciseServiceModel[] getActiveExercises() {
        return Arrays.stream(this.getExercises())
                .filter(ex -> ex.getDueDate().isAfter(LocalDateTime.now()))
                .toArray(ExerciseServiceModel[]::new);
    }

    @Override
    public ExerciseServiceModel getExerciseByName(String name) {
        Exercise exercise = repository.findByName(name).orElse(null);
        return exercise == null ? null : mapper.map(exercise, ExerciseServiceModel.class);
    }

    @Override
    public ExerciseServiceModel getExerciseById(String id) {
        Exercise exercise = repository.findById(id).orElse(null);
        return exercise == null ? null : mapper.map(exercise, ExerciseServiceModel.class);
    }

    @Override
    public ExerciseServiceModel getLast() {
        Exercise exercise = repository.getFirstByOrderByDueDateDesc().orElse(null);
        return exercise == null ? null : mapper.map(exercise, ExerciseServiceModel.class);
    }
}
