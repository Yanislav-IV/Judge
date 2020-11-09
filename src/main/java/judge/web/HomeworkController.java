package judge.web;

import judge.exception.AlreadyExistsException;
import judge.model.binding.CommentBindingModel;
import judge.model.binding.HomeworkBindingModel;
import judge.model.service.CommentServiceModel;
import judge.model.service.HomeworkServiceModel;
import judge.model.service.UserServiceModel;
import judge.service.AuthenticationService;
import judge.service.ExerciseService;
import judge.service.HomeworkService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/homework")
public class HomeworkController {
    private static final String BINDING_MODEL = "homeworkBindingModel";
    private static final String BINDING_RESULT = "org.springframework.validation.BindingResult.homeworkBindingModel";
    private static final String COMMENT_BINDING_MODEL = "commentBindingModel";
    private static final String COMMENT_BINDING_RESULT = "org.springframework.validation.BindingResult.commentBindingModel";

    private final HomeworkService homeworkService;
    private final ModelMapper mapper;
    private final AuthenticationService authenticationService;
    private final ExerciseService exerciseService;

    @Autowired
    public HomeworkController(HomeworkService homeworkService, ModelMapper mapper, AuthenticationService authenticationService, ExerciseService exerciseService) {
        this.homeworkService = homeworkService;
        this.mapper = mapper;
        this.authenticationService = authenticationService;
        this.exerciseService = exerciseService;
    }


    @ModelAttribute
    public void addAttributes(Model model) {
        if(!model.containsAttribute("exercises")) {
            model.addAttribute("exercises", exerciseService.getExercises());
        }
    }

    @GetMapping("/add")
    public String add(Model model, HttpSession session) {

        if(!authenticationService.isAuthorizedUser(session)) {
            return "redirect:/";
        }

        if (session.getAttribute(BINDING_MODEL) == null) {
            // first time or success
            model.addAttribute(BINDING_MODEL, new HomeworkBindingModel());
            model.addAttribute("success", session.getAttribute("success"));

            HomeworkServiceModel lastAdded = homeworkService.getLastAdded((String)session.getAttribute("userId"));
            model.addAttribute("lastAdded", lastAdded == null ? null : lastAdded.toString());

        } else {
            // if errors
            model.addAttribute("errors", session.getAttribute("errors"));
            model.addAttribute(BINDING_MODEL, session.getAttribute(BINDING_MODEL));
            model.addAttribute(BINDING_RESULT, session.getAttribute(BINDING_RESULT));
        }
        return "homework-add";
    }

    @PostMapping("/add")
    public String addConfirm(@Valid @ModelAttribute(BINDING_MODEL) HomeworkBindingModel bindingModel,
                             BindingResult result,
                             HttpSession session) {

        session.setAttribute("errors", null);
        session.setAttribute("success", false);

        if(!result.hasErrors()) {
            try {
                bindingModel.setAuthor((UserServiceModel) session.getAttribute("user"));
                homeworkService.addHomework(mapper.map(bindingModel, HomeworkServiceModel.class));

                session.removeAttribute(BINDING_MODEL);
                session.removeAttribute(BINDING_RESULT);
                session.setAttribute("success", true);

            } catch (AlreadyExistsException e) {
                result.rejectValue(e.getField(), "error." + BINDING_MODEL, e.getMessage());
            } catch (Exception ex) {
                session.setAttribute("errors", ex.getMessage());
            }
        }

        if(result.hasErrors() || session.getAttribute("errors") != null) {
            session.setAttribute(BINDING_MODEL, bindingModel);
            session.setAttribute(BINDING_RESULT, result);
        }

        return "redirect:/homework/add";
    }

    @GetMapping("/check")
    public String checkHomeworkForm(Model model,
                            HttpSession session) {

        if(!authenticationService.isAuthorizedUser(session)) {
            return "redirect:/";
        }

        if (session.getAttribute(COMMENT_BINDING_MODEL) == null) {
            // first time
            // least commented homework
            HomeworkServiceModel homework = homeworkService
                    .getOneToCheck(((UserServiceModel) session.getAttribute("user")).getId());

            session.setAttribute("homework", homework);
            session.setAttribute("git", homework == null ? null : homework.getGit());

            model.addAttribute(COMMENT_BINDING_MODEL, new CommentBindingModel());
            model.addAttribute("git", session.getAttribute("git"));

        } else {
            // if errors
            model.addAttribute(COMMENT_BINDING_MODEL, session.getAttribute(COMMENT_BINDING_MODEL));
            model.addAttribute(COMMENT_BINDING_RESULT, session.getAttribute(COMMENT_BINDING_RESULT));
            model.addAttribute("git", session.getAttribute("git"));
        }
        return "homework-check";
    }

    @PostMapping("/check")
    public String checkHomework(@Valid @ModelAttribute(COMMENT_BINDING_MODEL) CommentBindingModel bindingModel,
                                BindingResult result,
                                HttpSession session) {

        if(!result.hasErrors()) {

            CommentServiceModel commentServiceModel = mapper.map(bindingModel, CommentServiceModel.class);

            commentServiceModel.setAuthor((UserServiceModel) session.getAttribute("user"));
            commentServiceModel.setHomework((HomeworkServiceModel) session.getAttribute("homework"));
            homeworkService.addCommentToHomework(commentServiceModel);

            session.removeAttribute(COMMENT_BINDING_MODEL);
            session.removeAttribute(COMMENT_BINDING_RESULT);
            session.removeAttribute("homework");
            session.removeAttribute("git");
        }

        if(result.hasErrors()) {
            session.setAttribute(COMMENT_BINDING_MODEL, bindingModel);
            session.setAttribute(COMMENT_BINDING_RESULT, result);
            return "redirect:/homework/check";
        }
        return "redirect:/home";
    }
}
