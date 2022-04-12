package sleeplessdevelopers.schedulecreator;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CurrentScheduleTester {

    @Test
    public void add(){
        CurrentSchedule cs = new CurrentSchedule(new ArrayList<>());
        Course c1 = new Course("MUSI 102", "MUSIC HISTORY II", "9:00", "9:50", 'A', "MWF");
        cs.addCourse(c1);

        ArrayList<Course> courseList = cs.getCourseList();

        // should have 1 course
        assertEquals(1, courseList.size());

        // should be the same course
        assertEquals(c1, courseList.get(0));
    }

    @Test
    public void removeOne() {
        CurrentSchedule cs = new CurrentSchedule(new ArrayList<>());
        Course c1 = new Course("MUSI 102", "MUSIC HISTORY II", "9:00", "9:50", 'A', "MWF");
        cs.addCourse(c1);

        cs.removeCourse(c1);

        ArrayList<Course> courseList = cs.getCourseList();
        //should be removed
        assertEquals(0, courseList.size());
    }

    // remove function no longer provides this functionality
//    @Test
//    public void removeMult() {
//        CurrentSchedule cs = new CurrentSchedule(new ArrayList<>());
//        Course c1 = new Course("ACCT 202", "PRIN OF ACCOUNT", "8:00", "8:50", 'A', "MWF");
//        Course c2 = new Course("ACCT 202", "PRIN OF ACCOUNT", "16:00", "17:50", 'A', "R");
//        cs.addCourse(c1);
//        cs.addCourse(c2);
//
//        boolean didRem = cs.removeCourse("ACCT 202");
//
//        ArrayList<Course> courseList = cs.getCourseList();
//        // both should be removed
//        assertTrue(didRem);
//        assertEquals(0, courseList.size());
//    }

    // remove function no longer provides this functionality
//    @Test
//    public void removeNone() {
//        CurrentSchedule cs = new CurrentSchedule(new ArrayList<>());
//        Course c1 = new Course("MUSI 102", "MUSIC HISTORY II", "9:00", "9:50", 'A', "MWF");
//        cs.addCourse(c1);
//
//        boolean didRem = cs.removeCourse("ACCT 202");
//
//        ArrayList<Course> courseList = cs.getCourseList();
//        //should not be removed
//        assertFalse(didRem);
//        assertEquals(1, courseList.size());
//    }

    @Test
    public void noConflict() {
        CurrentSchedule cs = new CurrentSchedule(new ArrayList<>());
        // same days, different times
        Course c1 = new Course("MUSI 102", "MUSIC HISTORY II", "9:00", "9:50", 'A', "MWF");
        Course c2 = new Course("ACCT 202", "PRIN OF ACCOUNT", "8:00", "8:50", 'A', "MWF");
        cs.addCourse(c1);
        assertFalse(cs.conflictsWith(c2));

        // different days, same time
        cs.clearSchedule();
        c1 = new Course("MUSI 102", "MUSIC HISTORY II", "9:00", "9:50", 'A', "MWF");
        c2 = new Course("ACCT 202", "PRIN OF ACCOUNT", "9:00", "9:50", 'A', "TR");
        cs.addCourse(c1);
        assertFalse(cs.conflictsWith(c2));
    }

    @Test
    public void yesConflict() {
        CurrentSchedule cs = new CurrentSchedule(new ArrayList<>());
        Course c1 = new Course("MUSI 102", "MUSIC HISTORY II", "9:00", "9:50", 'A', "MWF");
        Course c2 = new Course("ACCT 202", "PRIN OF ACCOUNT", "9:00", "9:50", 'A', "MWF");
        cs.addCourse(c1);
        assertTrue(cs.conflictsWith(c2));
    }


}
