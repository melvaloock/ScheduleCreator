package sleeplessdevelopers.schedulecreator;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.ArrayList;

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
//            RecommendedSchedule rs = new RecommendedSchedule("Computer Science (BS)", 2024, db);
//            currentStudent.setCurrentSchedule(rs.makeCurrentSchedule());
            model.addAttribute("schedule", currentStudent.getCurrentSchedule());
            return "ScheduleView.html";
        }
    }

    @GetMapping("/schedule/save")
    public String postSchedule() {
        currentStudent.saveCurrentSchedule(db);
        return "redirect:/schedule";
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
        if (currentStudent == null) {
            return "redirect:/login";
            //TODO: add error message
        } else {
            return "AutoSchedule.html";
        }
    }
    
    @GetMapping("/login")
    public String getLogin(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "Login.html";
    }

    @PostMapping("/login")
    public String postLogin(@Valid @ModelAttribute("loginForm") LoginForm loginForm,
            BindingResult bindingResult) throws SQLException {
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
        if (currentStudent == null) {
            return "redirect:/login";
            //TODO: add error message
        } else {
            model.addAttribute("searchForm", new SearchForm());
            return "CourseSearch.html";
        }
    }

    @PostMapping("/search")
    public String postSearch(@Valid @ModelAttribute("searchForm") SearchForm searchForm,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "CourseSearch.html";
        } else {
            ArrayList<String> courses = searchForm.getCourses();
            addCourses(getCoursesFromJSON(courses));
            return "redirect:/schedule";
        }
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

    public ArrayList<Course> getCoursesFromJSON(ArrayList<String> json) {
        ArrayList<Course> courses = new ArrayList<Course>();
        for (String c : json) {
            JSONObject jsonObject = new JSONObject(c);

            int courseID = (int) jsonObject.get("CourseID");
            String courseCode = (String) jsonObject.get("CourseCode");
            String courseName = (String) jsonObject.get("CourseName");
            String weekday = (String) jsonObject.get("Weekday");
            String startTime = (String) jsonObject.get("StartTime");
            String endTime = (String) jsonObject.get("EndTime");
            // String enrollment = (String) jsonObject.get("Enrollment");
            // String capacity = (String) jsonObject.get("Capacity");

            courses.add(new Course(courseCode, courseName, startTime, endTime, courseCode.charAt(courseCode.length() - 1), weekday, courseID)); 
        }

        return courses;
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
