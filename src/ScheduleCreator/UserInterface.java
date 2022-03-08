package ScheduleCreator.ScheduleCreator;

import java.lang.reflect.Array;
import java.util.*;

public class UserInterface {
	
	private static HashMap<String, Schedule> recommendedSchedules;
	private static ArrayList<Course> courses;
	private static Student currentStudent;
	private Scanner searchScan = new Scanner(System.in); //takes user input for now
	
	public static ArrayList<Course> searchCoursesByCode(String code) {
		ArrayList<Course> result = new ArrayList<>();
		for (Course c : courses) {
			if (c.getCode().contains(code)) {
				result.add(c);
			}
		}
		return result;
	}
	public ArrayList<Course> searchCoursesByKeyword(String keyword) {return null;}
	public ArrayList<Course> dayFilter(ArrayList<Course> courses, ArrayList<String> days) {return null;}
	public ArrayList<Course> timeFilter(ArrayList<Course> courses, ArrayList<String> times) {return null;}
	public void login() {};
	public void addRecommendedSchedule(RecommendedSchedule r) {}
	public void createGuest() {}
	public void viewSchedule(Schedule s) {}

	public static void main(String args[]) {

	}
}
