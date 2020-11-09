package judge.convert;

import judge.model.service.ExerciseServiceModel;
import judge.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class StringToModelConverter implements Converter<String, ExerciseServiceModel> {

    private final ExerciseService exerciseService;

    @Autowired
    public StringToModelConverter(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @Override
    public ExerciseServiceModel convert(String from) {
        if(from != null && from.length() > 0) {
            return exerciseService.getExerciseById(from);
        }
        return null;
    }
}
