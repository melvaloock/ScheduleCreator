package sleeplessdevelopers.schedulecreator;

import java.util.ArrayList;

import javax.validation.constraints.NotEmpty;

public class ScheduleForm {

    @NotEmpty
    ArrayList<String> courseCodes;

    public ArrayList<String> getCourseCodes() {
        return courseCodes;
    }

    public void setCourseCodes(ArrayList<String> courseCodes) {
        this.courseCodes = courseCodes;
    }

    public String toString() {
        return "ScheduleForm: " + courseCodes;
    }
}
