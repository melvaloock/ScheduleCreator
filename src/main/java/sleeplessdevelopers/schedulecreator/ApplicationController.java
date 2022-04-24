package sleeplessdevelopers.schedulecreator;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApplicationController {
    
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
    
    // TODO: @GetMapping("/error")

}
