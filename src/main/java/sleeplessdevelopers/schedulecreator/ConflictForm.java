package sleeplessdevelopers.schedulecreator;

import java.util.ArrayList;

public class ConflictForm {

    ArrayList<String> conflictsInSchedule;

    ArrayList<String> coursesToAdd;

    public ArrayList<String> getConflictsInSchedule() {
        return conflictsInSchedule;
    }

    public void setConflictsInSchedule(ArrayList<String> conflictsInSchedule) {
        this.conflictsInSchedule = conflictsInSchedule;
    }

    public ArrayList<String> getCoursesToAdd() {
        return coursesToAdd;
    }

    public void setCoursesToAdd(ArrayList<String> coursesToAdd) {
        this.coursesToAdd = coursesToAdd;
    }

    public String toString() {
        return "ConflictForm: \nConflicts: " + conflictsInSchedule
                + "\nTo Add: " + coursesToAdd;
    }
}
