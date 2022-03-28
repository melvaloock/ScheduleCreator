package ScheduleCreator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class UserInterface {
	
	private static HashMap<String, Schedule> recommendedSchedules;
	private static ArrayList<Course> courses;
	protected static Account currentStudent;

	// database for the current session -- will only work if you have
	// an active db on your machine

	// FORGET THAT COMMENT CUZ IT DO BE A CLOUD DATABASE
	protected static Database db = new Database("root", "EnuzPkHDO29J6gCH", "schedule_creator_db");
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

		try {
			result = db.searchByCode(code);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}
	public static ArrayList<Course> searchCoursesByKeyword(String keyword) {
//		ArrayList<Course> result = new ArrayList<>();
//		for (Course c : courses) {
//			if (c.getTitle().contains(keyword)) {
//				result.add(c);
//			}
//		}
//		return result;

		ArrayList<Course> result = new ArrayList<>();

		try {
			result = db.searchByKeyword(keyword);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	public static ArrayList<Course> dayFilter(ArrayList<Course> searchResults, String days) {
		ArrayList<Course> result = new ArrayList<>();
		for (Course c : searchResults) {
			boolean add = false;
			for (char d: c.getDayList()) {
				for (char d2: days.toCharArray()) {
					if (d == d2) {
						add = true;
					}
				}
			}
			if (add) {
				result.add(c);
			}
		}
		return result;
	}

	public static ArrayList<Course> timeFilter(ArrayList<Course> searchResults, ArrayList<String> times) {
		ArrayList<Course> result = new ArrayList<>();
		for (Course c : searchResults) {
			boolean add = false;
			if (!times.contains(c.getStartTime())) {
				add = true;
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
	static boolean addCourses(ArrayList<Course> coursesToAdd) {
		// check if there is a conflict before adding
		CurrentSchedule cs = new CurrentSchedule(currentStudent.getCurrentSchedule().getCourseList());
		boolean hasConflict = false;
		for (Course c : coursesToAdd) {
			if (cs.conflictsWith(c)) {
				hasConflict = true;
			}
		}

		if (!hasConflict) {
			for (Course c : coursesToAdd) {
				cs.addCourse(c);
			}
			currentStudent.setCurrentSchedule(cs);
		}

		return hasConflict;
	}

	public static boolean removeCourse(String courseEntry) {
		boolean didRemove = false;
		ArrayList<Course> courses = currentStudent.getCurrentSchedule().getCourseList();

		for (Course c : courses) {
			if (c.getCode().contains(courseEntry)) {
				courses.remove(c);
				didRemove = true;
			}
		}

		currentStudent.setCurrentSchedule(new CurrentSchedule(courses));
		return didRemove;
	}

	public static void clearSchedule() {
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
		currentStudent = new Guest(); //Guest class inherits from Account class
		CurrentSchedule newSchedule = new CurrentSchedule();
		currentStudent.setCurrentSchedule(newSchedule);

	}

	static boolean createAccount(String userEmail, String userPassword) {
		//adds user to DB
		try {
			db.addAccount(userEmail, userPassword);
		} catch (SQLException | PasswordStorage.CannotPerformOperationException e) {
			System.out.println(e.getMessage());
			return false;
		}

		currentStudent = new Account(userEmail, userPassword);
		CurrentSchedule newSchedule = new CurrentSchedule();
		currentStudent.setCurrentSchedule(newSchedule);

		return true;
	}

	public static boolean loginToAccount(String userEmail, String userPassword) {
		try {
			currentStudent = db.checkLogin(userEmail, userPassword);
		} catch (SQLException e){
			System.out.println(e.getMessage());
			return false;
		}catch (PasswordStorage.InvalidHashException | PasswordStorage.CannotPerformOperationException e) {
			System.out.println("Password Hashing Went Wrong: " + e.getMessage());
			return false;
		}

		return true;
	}



	public int getUserID() {
		return this.userID;
	}

	public int incrementUserID() {
		return this.userID++;
	}

	public static void main(String args[]) {

		//test2();



//		menuNav(0);
//		Schedule tests = new Schedule(courses, "Spring 2023");
//		tests.displaySchedule();
//		testMenu();
		//A suggestion by Dr. Hutchins
		//to have a while loop for navigation.

//		for (Course c : searchCoursesByCode("COMP")) {
//			System.out.println(c);
//		}

		Console.consoleMain();
	}
}
