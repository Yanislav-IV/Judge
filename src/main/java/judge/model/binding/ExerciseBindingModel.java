package judge.model.binding;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

import static judge.constant.Constants.*;

public class ExerciseBindingModel {
    private String name;
    private LocalDateTime startedOn;
    private LocalDateTime dueDate;

    public ExerciseBindingModel() {
    }

    @NotEmpty(message = EXERCISE_NAME_CANNOT_BE_EMPTY_MESSAGE)
    @Size(min = EXERCISE_NAME_LENGTH_MIN, message = EXERCISE_NAME_LENGTH_MESSAGE)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    @PastOrPresent(message = DATE_CANNOT_BE_IN_FUTURE)
    public LocalDateTime getStartedOn() {
        return startedOn;
    }

    public void setStartedOn(LocalDateTime startedOn) {
        this.startedOn = startedOn;
    }

    @NotNull
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    @FutureOrPresent(message = DATE_CANNOT_BE_IN_PAST)
    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }
}
