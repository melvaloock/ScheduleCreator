package sleeplessdevelopers.schedulecreator;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserInterface {
	
	protected static ArrayList<String> recMajors = new ArrayList<String>() {
		{
			add("Computer Science (BS)");
		}
	};
	static ArrayList<Course> courses;
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
//		return ((i + 7 > 12) ? (i + 7) % 12 : i + 7) + ":00 " +  ((i > 4)? "PM" : "AM");
		return ((i + 7) + ":00:00");
	}

	public static ArrayList<Course> searchCoursesByCode(String code) {

		ArrayList<Course> result = new ArrayList<>();

		try {
			result = db.searchByCode(code);
		} catch (SQLException e) {
			System.out.println("Error!");
			//e.printStackTrace();
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
			System.out.println("Error!");
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
			if (times.contains(c.getStartTime())) {
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
	 * adds courses to schedule if they do not have conflicts
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

	static boolean addActivity(ArrayList<Course> activity) {
		// check if there is a conflict before adding
		CurrentSchedule cs = new CurrentSchedule(currentStudent.getCurrentSchedule().getCourseList());
		boolean hasConflict = false;

		for (int i = 0; i < cs.courseList.size(); i++){
			boolean refNumDiff = false;
			Random ran = new Random();
			while (!refNumDiff){
				if (activity.get(0).getReferenceNum() == cs.courseList.get(i).getReferenceNum()){
					activity.get(0).setReferenceNum(ran.nextInt(0,100));
				}
				else {
					break;
				}
			}
		}

		for (Course c : activity) {
			if (cs.conflictsWith(c)) {
				hasConflict = true;
			}
		}


		if (!hasConflict) {
			cs.addCourse(activity.get(0));
			currentStudent.setCurrentSchedule(cs);
		}

		return hasConflict;
	}





	/**
	 * clears the current Student's current schedule
	 */
	public static void clearSchedule() {
		currentStudent.currentSchedule.clearSchedule();
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

	/**
	 * Method used to check if the password meets the acceptable
	 * requirements: A symbol, an uppercase letter, a number, no spaces,
	 * and be at least eight characters long.
	 *
	 */
	public static boolean passwordCheck(String pw){
		Pattern symbolReg = Pattern.compile("[^a-zA-Z0-9]");
		Matcher symMatch = symbolReg.matcher(pw);
		boolean checkTrue = symMatch.find();
		//string of symbols and else if for checking for said symbols
		//provided by
		// https://codingface.com/how-to-check-string-contains-special-characters-in-java/#What_is_a_Special_Character
		boolean hasNum = false;
		boolean hasUpper = false;
		boolean hasSym = false;
		if (pw.length() < 8){
			return false;
		}
		char[] pwArray = pw.toCharArray();
		for (int i = 0; i < pwArray.length; i++){
			if (Character.isSpaceChar(i)){
				return false;
			}
			else if (Character.isDigit(pw.charAt(i))){ //numbers
				hasNum = true;
			}
			else if(Character.isUpperCase(pw.charAt(i))) { //uppercase letters
				hasUpper = true;
			}
		}

		if (checkTrue){ //symbols, ascii range or list of symbols to check
			hasSym = true;
		}

		if (hasNum == true && hasSym == true && hasUpper == true){
			return true;
		}

		return false;
	}

	public static boolean loginToAccount(String userEmail, String userPassword) {
		try {
			currentStudent = db.getAccount(userEmail, userPassword);
		} catch (SQLException e) {
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

	public static void optionSetMajor(String m){
		currentStudent.setMajor(m);

	}

	public static void optionSetYear(int y){
		currentStudent.setYear(y);
	}

	public static void main(String args[])throws ParseException {

		Console.consoleMain();
	}
}
