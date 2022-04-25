package sleeplessdevelopers.schedulecreator;

import java.util.ArrayList;

import javax.validation.constraints.NotEmpty;

// import org.hibernate.validator.constraints.Length;

public class SearchForm {

    // @Length(min = 1, max = 256)
    @NotEmpty
    private ArrayList<String> courses;

    public ArrayList<String> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<String> courses) {
        this.courses = courses;
    }

    public String toString() {
        return "SearchForm(courses: " + this.courses.toString() + ")";
    }
}
