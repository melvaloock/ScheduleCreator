package sleeplessdevelopers.schedulecreator;

import javax.validation.constraints.NotNull;

public class NewScheduleForm {

    @NotNull
    private String semester;

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
}
