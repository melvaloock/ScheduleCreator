package sleeplessdevelopers.schedulecreator;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class UserInterfaceTester {

    /**
     * non functioning.
     * maybe because everything is static. not sure.
     * tries to use currentStudent but it isn't initialized yet.
     * tried creating a guest to initialize it but doesn't work.
     */
    @Test
    public void addCourseTest(){
//        UserInterface ui = new UserInterface();
//        UserInterface.createGuest();
        ArrayList<Course> list = new ArrayList<>();
        Course c1 = new Course("MUSI 102", "MUSIC HISTORY II", "9:00", "9:50", 'A', "MWF");
        Course c2 = new Course("ACCT 202", "PRIN OF ACCOUNT", "8:00", "8:50", 'A', "MWF");
        Course c3 = new Course("some course", "dont wanna look up the info", "9:00", "9:50", 'B', "MWF");

        // no problems adding course to empty schedule
        list.add(c1);
        Assert.assertFalse(UserInterface.addCourses(list));

        // add non conflicting course
        list = new ArrayList<>();
        list.add(c2);
        Assert.assertFalse(UserInterface.addCourses(list));

        // add conflicting course
        list = new ArrayList<>();
        list.add(c3);
        Assert.assertFalse(UserInterface.addCourses(list));

    }



}
