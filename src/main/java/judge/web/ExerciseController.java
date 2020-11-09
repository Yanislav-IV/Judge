package judge.web;

import judge.constant.Constants;
import judge.exception.AlreadyExistsException;
import judge.model.binding.ExerciseBindingModel;
import judge.model.service.ExerciseServiceModel;
import judge.service.AuthenticationService;
import judge.service.ExerciseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/exercises")
public class ExerciseController {
    private static final String ERROR_USER_REG_BINDING_MODEL = "error.userRegisterBindingModel";
    private static final String ORG_BINDING_RESULT_EXERCISE = "org.springframework.validation.BindingResult.exercise";

    private final ExerciseService exerciseService;
    private final ModelMapper modelMapper;
    private final AuthenticationService authenticationService;

    @Autowired
    public ExerciseController(ExerciseService exerciseService, ModelMapper modelMapper, AuthenticationService authenticationService) {
        this.exerciseService = exerciseService;
        this.modelMapper = modelMapper;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/add")
    public String add(Model model, HttpSession session) {

        if(authenticationService.isAuthorizedUser(session, Constants.ROLE_ADMIN)) {
            if (!model.containsAttribute("exercise")) {
                model.addAttribute("exercise", new ExerciseBindingModel());
            }
            return "exercise-add";
        }
        return "redirect:/";
    }

    @PostMapping("/add")
    public String addConfirm(@Valid @ModelAttribute("exercise") ExerciseBindingModel bindingModel,
                             BindingResult result,
                             RedirectAttributes attributes) {

        if(!result.hasErrors()) {
            try {
                exerciseService.addExercise(modelMapper.map(bindingModel, ExerciseServiceModel.class));

            } catch (AlreadyExistsException ex) {
                result.rejectValue(ex.getField(), ERROR_USER_REG_BINDING_MODEL, ex.getMessage());
            }
        }

        if(result.hasErrors()) {
            attributes.addFlashAttribute("exercise", bindingModel);
            attributes.addFlashAttribute(ORG_BINDING_RESULT_EXERCISE, result);
        }

        return "redirect:/exercises/add";
    }
}
