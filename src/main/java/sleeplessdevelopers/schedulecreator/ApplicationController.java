package sleeplessdevelopers.schedulecreator;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.FileNotFoundException;
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
            model.addAttribute("loggedIn", isLoggedIn);
            return "ScheduleView.html";
        }
    }

    @GetMapping("/schedule-edit")
    public String getScheduleEdit(Model model) {
        if (currentStudent == null) {
            return "redirect:/login";
            //TODO: add error message
        } else {
            //next 3 lines for testing purposes
//            RecommendedSchedule rs = new RecommendedSchedule("Computer Science (BS)", 2024, db);
//            currentStudent.setCurrentSchedule(rs.makeCurrentSchedule());
//            currentStudent.addRecommendedSchedule();
            model.addAttribute("schedule", currentStudent.getCurrentSchedule());
            model.addAttribute("scheduleForm", new ScheduleForm());
            return "ScheduleEdit.html";
        }
    }

    @PostMapping("/schedule-edit")
    public String postScheduleEdit(@Valid @ModelAttribute("scheduleForm") ScheduleForm scheduleForm,
            BindingResult result) {
        if (result.hasErrors()) {
            System.out.println(scheduleForm.toString());
            return "redirect:/schedule-edit";
        } else {
            for (String courseCode : scheduleForm.getCourseCodes()) {
                currentStudent.currentSchedule.removeCourse(courseCode);
            }
            return "redirect:/schedule";
        }
    }

    @GetMapping("/schedule/save")
    public String postSchedule() {
        currentStudent.saveCurrentSchedule(db);
        return "redirect:/schedule";
    }

    // @GetMapping("/schedule/edit")
    // public String getScheduleEdit() {
    //     return "ScheduleEdit.html";
    // }

    @GetMapping("/about")
    public String getAbout(Model model) {
        model.addAttribute("loggedIn", isLoggedIn);
        return "AboutPage.html";
    }

    @GetMapping("/contact")
    public String getContact(Model model) {
        model.addAttribute("loggedIn", isLoggedIn);
        return "Contact.html";
    }

    @GetMapping("/schedule-new")
    public String getGuest(Model model) {
        if (currentStudent == null) {
            createGuest();
        }
        model.addAttribute("loggedIn", isLoggedIn);
        return "Guest.html";
    }


    @GetMapping("/auto-schedule")
    public String getAutoSchedule(Model model) {
        if (currentStudent == null) {
            return "redirect:/login";
            //TODO: add error message
        } else {
            model.addAttribute("autoScheduleForm", new AutoScheduleForm());
            return "AutoSchedule.html";
        }
    }

    @PostMapping("/auto-schedule")
    public String postAutoSchedule(@Valid @ModelAttribute("autoScheduleForm") AutoScheduleForm autoScheduleForm,
            BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/auto-schedule";
        } else {
            currentStudent.setCurrentSchedule(getRecommendedSchedule(autoScheduleForm.getMajor(),
                    autoScheduleForm.getYear()).makeCurrentSchedule());
            return "redirect:/schedule";
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
            model.addAttribute("loggedIn", isLoggedIn);
            return "CourseSearch.html";
        }
    }

    private ArrayList<Course> conflictingAdds;
    private ArrayList<Course> conflictsInSchedule;

    @PostMapping("/search")
    public String postSearch(@Valid @ModelAttribute("searchForm") SearchForm searchForm,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "CourseSearch.html";
        } else {
            ArrayList<String> courses = searchForm.getCourses();

            // debugging
            for (String c : courses) {
                System.out.println(c);
            }

            conflictingAdds = addCourses(getCoursesFromJSON(courses));
            conflictsInSchedule = getConflicts(conflictingAdds);

            if (conflictingAdds.size() == 0) {
                return "redirect:/schedule";
            } else {
                return "redirect:/conflicting-courses";
            }
        }
    }

    @GetMapping("/conflicting-courses")
    public String getConflictingCourses(Model model) {
        if (currentStudent == null) {
            return "redirect:/login";
            //TODO: add error message
        } else {
//            ConflictForm cf = new ConflictForm();
//            cf.setCoursesToAdd(conflictingAdds);
//            ArrayList<Course> conflicts = getConflicts(conflictingAdds);
//            cf.setConflictsInSchedule(conflicts);
            model.addAttribute("conflictForm", new ConflictForm());
            model.addAttribute("adding", conflictingAdds);
            model.addAttribute("conflicts", conflictsInSchedule);
            return "ConflictingCourses.html";
        }
    }

    @PostMapping(value = "/conflicting-courses", params = "remove")
    public String removeConflictingCourses(@Valid @ModelAttribute("conflictForm") ConflictForm conflictForm,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println("remove reached with errors");
            for (ObjectError e: bindingResult.getAllErrors()){
                System.out.println(e.toString());
            }
            return "redirect:/schedule";
        } else {
            System.out.println("remove reached");
//                ArrayList<String> conflictsInSchedule = conflictForm.conflictsInSchedule;
//                ArrayList<String> coursesToAdd = conflictForm.coursesToAdd;
                CurrentSchedule cs = currentStudent.getCurrentSchedule();

                for (Course c: conflictsInSchedule) {
                    cs.removeCourse(c);
                }

                for (Course c: conflictingAdds) {
                    cs.addCourse(c);
                }

                currentStudent.setCurrentSchedule(cs);

            return "redirect:/schedule";
        }
    }

    @PostMapping(value = "/conflicting-courses", params = "no-change")
    public String noRemoveConflictingCourses(@Valid @ModelAttribute("conflictForm") ConflictForm conflictForm,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println("no remove reached with errors");
            return "redirect:/schedule";
        } else {
            System.out.println("no remove reached");
            return "redirect:/schedule";
        }
    }

    @GetMapping("/remove-course")
    public String getRemoveCourse(Model model) {
        if (currentStudent == null) {
            return "redirect:/login";
            //TODO: add error message
        } else {
            //next 3 lines for testing purposes
//            RecommendedSchedule rs = new RecommendedSchedule("Computer Science (BS)", 2024, db);
//            currentStudent.setCurrentSchedule(rs.makeCurrentSchedule());
//            currentStudent.addRecommendedSchedule();
            model.addAttribute("scheduleForm", new ScheduleForm());
            model.addAttribute("courseList", currentStudent.getCurrentSchedule().getCourseList());
            return "CourseRemove.html";
        }
    }

    @PostMapping("/remove-course")
    public String postSearch(@Valid @ModelAttribute("scheduleForm") ScheduleForm scheduleForm,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("courseList", currentStudent.getCurrentSchedule().getCourseList());
            return "CourseRemove.html";
        } else {
            ArrayList<String> courses = scheduleForm.getCourseCodes();
            for (String code: courses) {
                currentStudent.currentSchedule.removeCourse(code);
            }
            return "redirect:/schedule";
        }
    }

    @ResponseBody
    @GetMapping("/api/get/courses")
    public String getCourses() {
        try {;
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

            if (courseID != 99999) {
                courses.add(new Course(courseCode, courseName, startTime, endTime, courseCode.charAt(courseCode.length() - 1), weekday, courseID)); 
            }
        }
        return courses;
    }

    public RecommendedSchedule getRecommendedSchedule(String major, int year) {
        return new RecommendedSchedule(major, year, db);
    }
    
    @GetMapping("/schedule/export")
    public String getExport(Model model) {
        String pdfFileName = currentStudent.getCurrentSchedule().semester;
        try {
            String jsonFileName = makeJSONFile();
            model.addAttribute("jsonExportFileName", jsonFileName);
        } catch (FileNotFoundException e) {
            System.out.println("making JSON file error");
        }
        model.addAttribute("pdfExportFileName", pdfFileName);
        return "Export.html";
    }

    @PostMapping("/uploadJSON")
    public String importJSON(@RequestParam("jsonImportFile") MultipartFile file) {
            if (file.isEmpty()) {
                System.out.println("empty file");
                return "redirect:/schedule/export";
            }

            try {
                String content = new String(file.getBytes());
                importFromJSONString(content);
            } catch (Exception e) {
                System.out.println("error with getting file data");
            }

            return "redirect:/schedule";
    }

    @GetMapping("/schedule/email")
    public String getEmail() {
        return "redirect:/schedule";
    }

    @GetMapping("/logout")
    public String getLogout() {
        currentStudent = null;
        isLoggedIn = false;
        return "redirect:/";
    }



    // TODO: @GetMapping("/error")

}
