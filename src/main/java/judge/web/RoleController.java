package judge.web;

import judge.model.binding.RoleAddBindingModel;
import judge.model.service.UserServiceModel;
import judge.service.AuthenticationService;
import judge.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

import java.util.Arrays;

import static judge.constant.Constants.*;


@Controller
@RequestMapping("/users/roles")
public class RoleController {

    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final ModelMapper mapper;

    @Autowired
    public RoleController(UserService userService, AuthenticationService authenticationService, ModelMapper mapper) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.mapper = mapper;
    }


    @GetMapping("/add")
    public String add(Model model, HttpSession session) {

        if (authenticationService.isAuthorizedUser(session, ROLE_ADMIN)) {
            if (!model.containsAttribute("users")) {

                RoleAddBindingModel[] bindingModels = Arrays.stream(userService.getUsers())
                        .map(u -> new RoleAddBindingModel(u.getUsername(), u.getRole().getName()))
                        .toArray(RoleAddBindingModel[]::new);

                model.addAttribute("users", bindingModels);
            }
            return "role-add";
        }
        return "redirect:/";
    }

    @PostMapping("/add")
    public String addConfirm(@ModelAttribute("roleAddBindingModel") RoleAddBindingModel bindingModel,
                             HttpSession session,
                             RedirectAttributes attributes) {

        if(bindingModel.getUsername().isBlank() || bindingModel.getUsername().isEmpty() || bindingModel.getRole() == null){
            attributes.addFlashAttribute("errors", SET_USERNAME_ROLE_MESSAGE);
            return "redirect:/users/roles/add";
        }

        try {
            UserServiceModel admin = (UserServiceModel) session.getAttribute("user");
            userService.grantRoleToUser(admin, bindingModel.getUsername(), bindingModel.getRole());
            attributes.addFlashAttribute("success", String.format(USER_ROLE_ASSIGNED_MESSAGE, bindingModel.getRole()));

        } catch (Exception ex) {
            return "redirect:/";
        }
        return "redirect:/users/roles/add";
    }
}
