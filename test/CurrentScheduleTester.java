import ScheduleCreator.Course;
import ScheduleCreator.CurrentSchedule;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class CurrentScheduleTester {

    @Test
    public void addCourseTest(){
        CurrentSchedule cs = new CurrentSchedule(new ArrayList<Course>());
        Course c1 = new Course("MUSI 102", "MUSIC HISTORY II", "9:00 AM", "9:50 AM", 'A', "MWF");
        cs.addCourse(c1);

        ArrayList<Course> courseList = cs.getCourseList();

        // should have 1 course
        Assert.assertEquals(1, courseList.size());

        // should be the same course
        Assert.assertEquals(c1, courseList.get(0));
    }

}
