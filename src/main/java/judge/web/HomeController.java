package judge.web;

import judge.service.AuthenticationService;
import judge.service.CommentService;
import judge.service.ExerciseService;
import judge.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;


@Controller
public class HomeController {

    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final ExerciseService exerciseService;
    private final CommentService commentService;

    public HomeController(UserService userService, AuthenticationService authenticationService, ExerciseService exerciseService, CommentService commentService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.exerciseService = exerciseService;
        this.commentService = commentService;
    }

    @GetMapping("/")
    public String index(HttpSession session){
        if(authenticationService.isAuthorizedUser(session)) {
            return "redirect:/home";
        }
        return "index";
    }

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {

        if(!authenticationService.isAuthorizedUser(session)) {
            return "redirect:/";
        }

        model.addAttribute("users", userService.getTopScored());
        model.addAttribute("exercises", exerciseService.getActiveExercises());
        model.addAttribute("usersCount", userService.getCount());
        model.addAttribute("averageGrade", userService.getAverageGrade());
        model.addAttribute("grades",commentService.getAllGradesCount());
        return "home";
    }
}
