package judge.service;

import judge.model.service.ExerciseServiceModel;

public interface ExerciseService {
    void addExercise(ExerciseServiceModel exerciseServiceModel);
    ExerciseServiceModel[] getExercises();
    ExerciseServiceModel[] getActiveExercises();
    ExerciseServiceModel getExerciseByName(String name);
    ExerciseServiceModel getExerciseById(String id);
    ExerciseServiceModel getLast();
}
