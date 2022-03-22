package ScheduleCreator;

import java.sql.SQLException;
import java.util.*;

public class UserInterface {
	
	private static HashMap<String, Schedule> recommendedSchedules;
	private static ArrayList<Course> courses;
	private static Student currentStudent;

	// database for the current session -- will only work if you have 
	// an active db on your machine

	// FORGET THAT COMMENT CUZ IT DO BE A CLOUD DATABASE
	private static Database db = new Database("root", "EnuzPkHDO29J6gCH", "schedule_creator_db");
	private int userID;

	public static Student getCurrentStudent() {
		return currentStudent;
	}

	//move to time and day class later
	public static String intToTime(int i) {
		return ((i + 7 > 12) ? (i + 7) % 12 : i + 7) + ":00 " +  ((i > 4)? "PM" : "AM");
	}

	public static ArrayList<Course> searchCoursesByCode(String code) {
		ArrayList<Course> result = new ArrayList<>();
		for (Course c : courses) {
			if (c.getCode().contains(code)) {
				result.add(c);
			}
		}
		return result;
	}
	public static ArrayList<Course> searchCoursesByKeyword(String keyword) {
		ArrayList<Course> result = new ArrayList<>();
		for (Course c : courses) {
			if (c.getTitle().contains(keyword)) {
				result.add(c);
			}
		}
		return result;
	}

	public static ArrayList<Course> dayFilter(ArrayList<Course> courses, String days) {
		ArrayList<Course> result = new ArrayList<>();
		for (Course c : courses) {
			boolean add = true;
			for (char d: c.getDayList()) {
				if (!Arrays.asList(days.toCharArray()).contains(d)) {
					add = false;
				}
			}
			if (add) {
				result.add(c);
			}
		}
		return result;
	}

	public static ArrayList<Course> timeFilter(ArrayList<Course> courses, ArrayList<String> times) {

		ArrayList<Course> result = new ArrayList<>();
		for (Course c : courses) {
			boolean add = true;
			if (!times.contains(c.getStartTime())) {
				add = false;
			}
			if (add) {
				result.add(c);
			}
		}
		return result;
	}

	/** MELVA
	 *
	 * @param
	 */
	public void addRecommendedSchedule(RecommendedSchedule r) {}

	/**
	 *
	 * @param coursesToAdd Arraylist of courses needing to be added
	 */
	void addCourses(ArrayList<Course> coursesToAdd) {
		// check if there is a conflict before adding
		CurrentSchedule cs = new CurrentSchedule(currentStudent.getCurrentSchedule().getCourseList());
		boolean hasConflict = false;
		for (Course c : coursesToAdd) {
			if (cs.conflictsWith(c)) {
				hasConflict = true;
			}
		}

		if (hasConflict) {
			System.out.println("The course you selected conflicts with another course in your schedule," +
					" so it cannot be added.");
		} else {
			for (Course c : coursesToAdd) {
				cs.addCourse(c);
			}
			System.out.println("Course Added.");
		}

		currentStudent.setCurrentSchedule(cs);
	}

	public void removeCourse(String courseEntry) {
		CurrentSchedule cs = new CurrentSchedule(currentStudent.getCurrentSchedule().getCourseList());
		boolean didRem = cs.removeCourse(courseEntry);

		// give result
		if (didRem) {
			System.out.println("Course Removed.");
		} else {
			System.out.println("Course not found in your current schedule, so nothing was removed.");
		}

		currentStudent.setCurrentSchedule(cs);
	}

	public void clearSchedule() {
		CurrentSchedule cs = new CurrentSchedule(currentStudent.getCurrentSchedule().getCourseList());
		cs.clearSchedule();
		currentStudent.setCurrentSchedule(cs);
	}

	/**
	 * Method should not return anything or print anything
	 * Should create an instance of the Guest class and assign it to currentStudent
	 * Should create blank schedule and set that schedule to currentStudent's currentSchedule
	 */
	public static void createGuest() {
		currentStudent = new Guest(); //Guest class inherits from Student class
		CurrentSchedule newSchedule = new CurrentSchedule();
		currentStudent.setCurrentSchedule(newSchedule);
//		consoleScheduleChoice();

	}

	static boolean createAccount(String userEmail, String userPassword) {
		//adds user to DB
		try {
			db.addAccount(userEmail, userPassword);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}

		currentStudent = new Account(userEmail, userPassword);
		CurrentSchedule newSchedule = new CurrentSchedule();
		currentStudent.setCurrentSchedule(newSchedule);

		return true;
	}

	public static void testMenu() {
		Console.menuNav(0);
	}

	public int getUserID() {
		return this.userID;
	}

	public int incrementUserID() {
		return this.userID++;
	}

	public static void helpDescriptions(int helpID) {
		switch (helpID) {
			case 1:
				System.out.println("Here you can, save your schedule (if you have an account),");
				System.out.println("change (alter) your schedule, load an existing schedule");
				System.out.println("(if you have an account), send the schedule to an email, ");
				System.out.println("or save this schedule on your computer.");
				break;
			case 2:
				System.out.println("");
				System.out.println("");
				System.out.println("");
				System.out.println("");
				break;
			case 3:
				System.out.println("1");
				System.out.println("");
				System.out.println("");
				System.out.println("");
				break;


		}
	}

	public static void main(String args[]) {

		//test2();



//		menuNav(0);
//		Schedule tests = new Schedule(courses, "Spring 2023");
//		tests.displaySchedule();
//		testMenu();
		//A suggestion by Dr. Hutchins
		//to have a while loop for navigation.

		Console.consoleMain();
	}
}
