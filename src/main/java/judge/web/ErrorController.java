package judge.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class ErrorController {

    @GetMapping("/app/error")
    @ResponseBody
    public String error(HttpSession session){
        String msg = session == null ? "There was an unexpected error" : (String)session.getAttribute("error");
        return "<h2>" + msg + "</h2>";
    }
}
