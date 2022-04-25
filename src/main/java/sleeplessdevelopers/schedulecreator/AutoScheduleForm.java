package sleeplessdevelopers.schedulecreator;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.NumberFormat;

public class AutoScheduleForm {
    
    @NotNull
    private String major;

    @NumberFormat
    private int year;

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String toString() {
        return "AutoScheduleForm(major=" + major + ", year=" + year + ")";
    }
    
}
