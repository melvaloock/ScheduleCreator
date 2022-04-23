package sleeplessdevelopers.schedulecreator;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApplicationController {
    
    @GetMapping("/")
    public String index() {
        return "/html/LandingPage.html";
    }

    @GetMapping("/login")
    public String login() {
        return "/html/Login.html";
    }
}
