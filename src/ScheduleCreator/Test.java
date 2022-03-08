package ScheduleCreator.ScheduleCreator;

import java.util.ArrayList;

public class Test extends UserInterface {

    public static void test1() {
      ArrayList<Course> courses = new ArrayList<Course>();

        Course c1 = new Course("MUSI 102", "MUSIC HISTORY II", "9:00 AM", "9:50 AM", 'A', "MWF");
        Course c2 = new Course("MUSI 102", "MUSIC HISTORY II", "9:00 AM", "9:50 AM", 'B', "MWF");
        Course c3 = new Course("COMP 141", "INTRO TO PROGRAM", "11:00 AM", "11:50 AM", 'A', "MWF");

        courses.add(c1);
        courses.add(c2);
        courses.add(c3);

        System.out.println(courses);
        System.out.println(searchCoursesByCode("141"));
    }

}
