package sleeplessdevelopers.schedulecreator;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ApplicationController extends UserInterface {
    
    @GetMapping("/")
    public String index() {
        return "LandingPage.html";
    }

    @GetMapping("/login")
    public String getLogin() {
        return "Login.html";
    }

    @GetMapping("/schedule")
    public String getSchedule() {
        return "ScheduleView.html";
    }

    @GetMapping("/schedule-edit")
    public String getScheduleEdit() {
        return "ScheduleEdit.html";
    }

    @GetMapping("/about")
    public String getAbout() {
        return "AboutPage.html";
    }

    @GetMapping("/contact")
    public String getContact() {
        return "Contact.html";
    }

    @GetMapping("/guest")
    public String getGuest() {
        return "Guest.html";
    }

    @GetMapping("/auto-schedule")
    public String getAutoSchedule() {
        return "AutoSchedule.html";
    }
    
    @GetMapping("/login")
    public String getLogin(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "Login.html";
    }

    @PostMapping("/login")
    public String postLogin(@Valid @ModelAttribute("loginForm") LoginForm loginForm,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "Login.html";
        } else {
            loginToAccount(loginForm.getUsername(), loginForm.getPassword());
            return "redirect:/"; // TODO: update with proper path
        }
    }


    // TODO: @GetMapping("/error")

}
