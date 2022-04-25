package sleeplessdevelopers.schedulecreator;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
public class ApplicationController extends UserInterface {


    
    @GetMapping("/")
    public String index() {
        return "LandingPage.html";
    }

    @GetMapping("/schedule")
    public String getSchedule(Model model) {
        if (currentStudent == null) {
            return "redirect:/login";
            //TODO: add error message
        } else {
            //next 3 lines for testing purposes
            RecommendedSchedule rs = new RecommendedSchedule("Computer Science (BS)", 2024, db);
            currentStudent.setCurrentSchedule(rs.makeCurrentSchedule());
            currentStudent.addRecommendedSchedule();
            model.addAttribute("schedule", currentStudent.getCurrentSchedule());
            return "ScheduleView.html";
        }
    }

    @GetMapping("/schedule/edit")
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
        if (currentStudent == null) {
            createGuest();
        }
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
            if (loginToAccount(loginForm.getUsername(), loginForm.getPassword())) {
                return "redirect:/schedule";
            }
            else return "login.html";
        }
    }

    @GetMapping("/create-account")
    public String getCreateAccount(Model model) {
        model.addAttribute("accountCreationForm", new AccountCreationForm());
        return "CreateAccount.html";
    }

    @PostMapping("/create-account")
    public String postCreateAccount(@Valid @ModelAttribute("accountCreationForm") AccountCreationForm accountCreationForm,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "CreateAccount.html";
        } else {
            createAccount(accountCreationForm.getUsername(), accountCreationForm.getPassword());
            return "redirect:/"; // TODO: update with proper path
        }
    }

    @GetMapping("/search")
    public String getSearch(Model model) {
        return "CourseSearch.html";
    }

    @ResponseBody
    @GetMapping("/api/get/courses")
    public String getCourses() {
        try {
            String json = db.getJSONCourses(); 
            return json;
        } catch (Exception e) {
            e.getMessage();
            return "";
        }
       
    }

    @GetMapping("/schedule/export")
    public String getExport() {
        return "ScheduleView.html";
    }

    @GetMapping("/schedule/email")
    public String getEmail() {
        return "ScheduleView.html";
    }



    // TODO: @GetMapping("/error")

}
