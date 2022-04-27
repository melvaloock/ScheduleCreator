package sleeplessdevelopers.schedulecreator;

import javax.validation.constraints.NotNull;

public class NewScheduleForm {

    @NotNull(message = "Please select a semester")
    private String semester;

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
}
