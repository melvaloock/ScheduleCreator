package sleeplessdevelopers.schedulecreator;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
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
	protected static boolean isLoggedIn = false;
	private static final String fileSavePath = "src/main/resources/static/files/";

	// database for the current session -- will only work if you have
	// an active db on your machine

	// FORGET THAT COMMENT CUZ IT DO BE A CLOUD DATABASE
	protected static Database db = new Database("root", "EnuzPkHDO29J6gCH", "schedule_creator_db");
	private int userID;

	public static Student getCurrentStudent() {
		return currentStudent;
	}

	// move to time and day class later
	public static String intToTime(int i) {
		// return ((i + 7 > 12) ? (i + 7) % 12 : i + 7) + ":00 " + ((i > 4)? "PM" :
		// "AM");
		return ((i + 7) + ":00:00");
	}

	public static ArrayList<String> getSchedules() {
		ArrayList<String> semesters = new ArrayList<String>();
		try {
			semesters = (db.getSemesters(currentStudent.getEmail()));
		} catch (SQLException e) {
			e.getMessage();
		}

		System.out.println(semesters); // TODO: debugging

		if (semesters.contains(currentStudent.currentSchedule.semester)) {
			semesters.remove(currentStudent.currentSchedule.semester);
		}

		System.out.println(semesters); // TODO: debugging
		System.out.println(semesters.isEmpty());

		return semesters;
	}

	public static CurrentSchedule getNewCurrentSchedule(String semester, String email) {
		Schedule temp = new Schedule();
		try {
			temp = db.getSchedule(email, semester);
		} catch (SQLException e) {
			e.getMessage();
		}
		return new CurrentSchedule(temp);
	}

	public static ArrayList<Course> searchCoursesByCode(String code) {

		ArrayList<Course> result = new ArrayList<>();

		try {
			result = db.searchByCode(code);
		} catch (SQLException e) {
			System.out.println("Error!");
			// e.printStackTrace();
		}

		return result;
	}

	public static ArrayList<Course> searchCoursesByKeyword(String keyword) {
		// ArrayList<Course> result = new ArrayList<>();
		// for (Course c : courses) {
		// if (c.getTitle().contains(keyword)) {
		// result.add(c);
		// }
		// }
		// return result;

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
			for (char d : c.getDayList()) {
				for (char d2 : days.toCharArray()) {
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

	/**
	 * MELVA
	 *
	 * @param
	 */
	public void addRecommendedSchedule(RecommendedSchedule r) {
	}

	/**
	 * adds courses to schedule if they do not have conflicts
	 * 
	 * @param coursesToAdd Arraylist of courses needing to be added
	 */
	public static ArrayList<Course> addCourses(ArrayList<Course> coursesToAdd) {
		ArrayList<Course> conflicts = new ArrayList<>();

		CurrentSchedule cs = new CurrentSchedule(currentStudent.getCurrentSchedule().getCourseList(), currentStudent.getCurrentSchedule().semester);

		for (Course c : coursesToAdd) {
			if (cs.conflictsWith(c)) {
				conflicts.add(c);
			} else {
				cs.addCourse(c);
			}
		}

		currentStudent.setCurrentSchedule(cs);

		return conflicts;
	}

	public ArrayList<Course> getConflicts(ArrayList<Course> coursesToCheck) {
		ArrayList<Course> conflicts = new ArrayList<>();
		 CurrentSchedule cs = currentStudent.getCurrentSchedule();
		for (Course c : coursesToCheck) {
			 conflicts.addAll(cs.getConflicts(c));
		}

		return conflicts;
	}

	public static ArrayList<Course> addActivity (ArrayList<Course> activity) {
		//Same as addCourse but deals with activities.
		ArrayList<Course> conflicts = new ArrayList<>();
		CurrentSchedule cs  = new CurrentSchedule(currentStudent.getCurrentSchedule().getCourseList());
		// check this after merge
		for (Course c : activity) {
			if (cs.conflictsWith(c)) {
				conflicts.add(c);
			}
			else{
				cs.addCourse(c);
			}
		}
		currentStudent.setCurrentSchedule(cs);
		return conflicts;
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
	 * Should create blank schedule and set that schedule to currentStudent's
	 * currentSchedule
	 */
	public static void createGuest() {
		currentStudent = new Guest(); // Guest class inherits from Account class
		CurrentSchedule newSchedule = new CurrentSchedule();
		currentStudent.setCurrentSchedule(newSchedule);
		isLoggedIn = false;

	}

	static boolean createAccount(String userEmail, String userPassword) {
		// adds user to DB
		try {
			db.addAccount(userEmail, userPassword);
		} catch (SQLException | PasswordStorage.CannotPerformOperationException e) {
			System.out.println(e.getMessage());
			return false;
		}

		CurrentSchedule newSchedule = new CurrentSchedule();
		if (currentStudent != null) {
			newSchedule = currentStudent.currentSchedule;
		}
		currentStudent = new Account(userEmail, userPassword);
		currentStudent.setCurrentSchedule(newSchedule);
		currentStudent.saveCurrentSchedule(db);

		isLoggedIn = true;
		return true;
	}

	/**
	 * Method used to check if the password meets the acceptable
	 * requirements: A symbol, an uppercase letter, a number, no spaces,
	 * and be at least eight characters long.
	 */
	public static boolean passwordCheck(String pw) {
		Pattern symbolReg = Pattern.compile("[^a-zA-Z0-9]");
		Matcher symMatch = symbolReg.matcher(pw);
		boolean checkTrue = symMatch.find();
		// string of symbols and else if for checking for said symbols
		// provided by
		// https://codingface.com/how-to-check-string-contains-special-characters-in-java/#What_is_a_Special_Character
		boolean hasNum = false;
		boolean hasUpper = false;
		boolean hasSym = false;
		if (pw.length() < 8) {
			return false;
		}
		char[] pwArray = pw.toCharArray();
		for (int i = 0; i < pwArray.length; i++) {
			if (Character.isSpaceChar(i)) {
				return false;
			} else if (Character.isDigit(pw.charAt(i))) { // numbers
				hasNum = true;
			} else if (Character.isUpperCase(pw.charAt(i))) { // uppercase letters
				hasUpper = true;
			}
		}

		if (checkTrue) { // symbols, ascii range or list of symbols to check
			hasSym = true;
		}

		if (hasNum == true && hasSym == true && hasUpper == true) {
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
		} catch (PasswordStorage.InvalidHashException | PasswordStorage.CannotPerformOperationException e) {
			System.out.println("Password Hashing Went Wrong: " + e.getMessage());
			return false;
		}

		isLoggedIn = true;
		return true;
	}

	public int getUserID() {
		return this.userID;
	}

	public int incrementUserID() {
		return this.userID++;
	}

	public static void optionSetMajor(String m) {
		currentStudent.setMajor(m);

	}

	public static void optionSetYear(int y) {
		currentStudent.setYear(y);
	}

	public static String makeJSONFile() throws FileNotFoundException {
		Random rand = new Random();
		CurrentSchedule cs = currentStudent.getCurrentSchedule();
		/*
		 * uses a random number in file name
		 * this prevents an export schedule file from being overwritten if 2 users are
		 * importing/exporting with the same file name at the same time
		 */
		String semester = cs.semester.replaceAll(" ", "");
		String fileName = semester + "_" + currentStudent.getEmail() + "_"
				+ rand.nextInt(100000) + ".json";

		FileOutputStream fos = new FileOutputStream(fileSavePath + fileName);
		PrintWriter pw = new PrintWriter(fos);

		String json = currentStudent.getCurrentSchedule().toJSON();

		pw.print(json);

		// close things
		pw.flush();
		pw.close();

		return fileName;
	}

	public static void importFromJSONFile(String fileName) throws FileNotFoundException {
		if (!fileName.substring(fileName.length() - 5).equals(".json")) {
			throw new FileNotFoundException(fileName + " is not a JSON file.");
		}

		FileInputStream fis = new FileInputStream(fileName);
		Scanner scn = new Scanner(fis);

		StringBuilder sb = new StringBuilder();

		while (scn.hasNextLine()) {
			sb.append(scn.nextLine());
		}

		// close things
		scn.close();

		CurrentSchedule fromJSON = CurrentSchedule.fromJSON(sb.toString());
		currentStudent.setCurrentSchedule(fromJSON);

	}

	public static void importFromJSONString(String json) {
		CurrentSchedule fromJSON = CurrentSchedule.fromJSON(json);
		currentStudent.setCurrentSchedule(fromJSON);
	}

	public static boolean deleteFile(String fileName) {
		File f = new File(fileName);
		return f.delete();
	}

	public static String generatePDF() {
		Random rand = new Random();
		String semester = currentStudent.getCurrentSchedule().semester.replaceAll(" ", "");
		String filename = semester + "_" + currentStudent.getEmail()
				+ "_" + rand.nextInt(100000) + ".pdf";
		Document doc = new Document();
		try {
			PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("./" + fileSavePath + filename));
			doc.open();
			doc.add(new Paragraph(currentStudent.getCurrentSchedule().toString()));
			doc.close();
			writer.close();
		} catch (Exception e) {
			System.out.println("Error generating PDF");
		}
		return filename;
	}

	public static void main(String args[]) throws ParseException {

		Console.consoleMain();
	}
}
