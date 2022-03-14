package ScheduleCreator;

import java.util.ArrayList;

public class Test extends UserInterface {
  private static ArrayList<Course> courses;

  //paste method into UserInterface to test
  public static void test1() {
    courses = new ArrayList<Course>();

    Course c1 = new Course("MUSI 102", "MUSIC HISTORY II", "9:00 AM", "9:50 AM", 'A', "MWF");
    Course c2 = new Course("MUSI 102", "MUSIC HISTORY II", "9:00 AM", "9:50 AM", 'B', "MWF");
    Course c3 = new Course("COMP 141", "INTRO TO PROGRAMMING", "11:00 AM", "11:50 AM", 'A', "MWF");

    System.out.println(c1);

    courses.add(c1);
    courses.add(c2);
    courses.add(c3);


//
//    System.out.println(courses);
//    System.out.println(searchCoursesByCode("COMP") + "\n");
//
//    consoleSearch();
  }

}
