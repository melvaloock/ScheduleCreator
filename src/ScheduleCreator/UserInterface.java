package ScheduleCreator;

import java.sql.SQLException;
import java.util.*;

public class UserInterface {
	
	private static HashMap<String, Schedule> recommendedSchedules;
	private static ArrayList<Course> courses;
	private static Student currentStudent;
	private static Scanner scn = new Scanner(System.in);

	// database for the current session -- will only work if you have 
	// an active db on your machine
	private static Database db = new Database("root", "password", "sys");


	public static ArrayList<Course> consoleSearch() {

		ArrayList<Course> searchResults = new ArrayList<>();
		Scanner searchScan = new Scanner(System.in); //takes user input for now
		System.out.println("Would you like to search by code (1) or keyword (2)?");
		int searchType = intEntry(1, 2, searchScan);
		searchScan.nextLine(); //clears scanner

		if (searchType == 1) {
			System.out.println("Searching by course code \nEnter the course code you would like to search for");

			String code = searchScan.nextLine().toUpperCase(); //matches all to the format in the database
			//check for valid user input
			searchResults = searchCoursesByCode(code);
			System.out.println("Search results:");
			for (int i = 1; i <= searchResults.size(); i++) {
				System.out.println(i + ". " +searchResults.get(i - 1));
			}
		}
		else if (searchType == 2) {
			System.out.println("Searching by keyword \nEnter the keyword you would like to search for");

			String keyword = searchScan.nextLine().toUpperCase(); //matches all to the format in the database
			//check for valid user input
			searchResults = searchCoursesByKeyword(keyword);
			System.out.println("Search results:");
			for (int i = 1; i <= searchResults.size(); i++) {
				System.out.println(i + ". " +searchResults.get(i - 1));
			}
		}
		if (searchResults.isEmpty()) {
			System.out.println("You search returned no courses-- search again? (y/n)");
			if (ynEntry(searchScan) == 'Y') {
				consoleSearch();
			}
		}

		ArrayList<Course> filteredResults = consoleFilter(searchResults);


		return searchResults;
	}

	public static ArrayList<Course> consoleFilter(ArrayList<Course> searchResults) {

		Scanner filterScan = new Scanner(System.in); //takes user input for now
		ArrayList<Course> filterResults = new ArrayList<>();
		System.out.println("Would you like to filter by day (1) or time of day (2)?");
		int filterType = intEntry(1, 2, filterScan);
		filterScan.nextLine();

		if (filterType == 1) {
			System.out.println("Enter all the days that you want to see results for");
			String filterDays = filterScan.nextLine();
			filterResults = dayFilter(searchResults, filterDays);
		}
		System.out.println("Filtered results:");
		if (filterResults.isEmpty()) {
			System.out.println("You search returned no courses-- search again? (y/n)");
			if (ynEntry(filterScan) == 'Y') {
				consoleFilter(searchResults);
			}
		}
		for (int i = 1; i <= filterResults.size(); i++) {
			System.out.println(i + ". " +filterResults.get(i - 1));
		}

		return filterResults;
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

	public static ArrayList<Course> timeFilter(ArrayList<Course> courses, ArrayList<String> times) {return null;}

	public static int intEntry(int min, int max, Scanner scanner) {
		int entry;
		while(true){
			try {
				entry = scanner.nextInt();
				if (entry >= min && entry <= max){
					break;
				} else {
					System.out.println("Enter an integer between " + min + " and "+ max);
				}
			} catch (Exception e){
				System.out.println("Enter an integer between " + min + " and "+ max);
				scanner.next();
			}
		}
		return entry;
	}

	public static char ynEntry(Scanner scanner) {
		char yn;
		while(true){
			try {
				yn = scanner.next().toUpperCase().charAt(0);
				if (yn == 'Y' || yn == 'N'){
					break;
				} else {
					System.out.println("Enter 'Y' or 'N'");
				}
			} catch (Exception e){
				scanner.next();
				System.out.println("Enter 'Y' or 'N'");
			}
		}
		return yn;
	}

	/** MELVA
	 *
	 * @param r
	 */
	public void addRecommendedSchedule(RecommendedSchedule r) {}

	/**
	 * Method should not return anything or print anything
	 * Should create an instance of the Guest class and assign it to currentStudent
	 * Should create blank schedule and set that schedule to currentStudent's currentSchedule
	 */
	public static void consoleCreateGuest() {
		currentStudent = new Guest(); //Guest class inherits from Student class
		CurrentSchedule newSchedule = new CurrentSchedule();
		currentStudent.setCurrentSchedule(newSchedule);
		consoleScheduleChoice();

	}

	/** - provides the user with a menu to choose between manual creation (1) or recommended schedule (2)
	 * if the user chooses 1, call the consoleSearch method
	 * if the user chooses 2, get the input for their major and year
	 * for now, stop here
	 */
	public static void consoleScheduleChoice(){
		int yn;
		int year;
		String major;
		System.out.println("Do you want automatic scheduling?");
		System.out.println("1) No, I want to manually set up my schedule.");
		System.out.println("2) Yes, set up my schedule for me.");
		yn = intEntry(1,2,scn);
		if (yn == 1){
			consoleSearch();
		}
		else{
			System.out.print("What is your major: ");
			major = scn.next();
			System.out.print("\nWhat is your year: ");
			year = intEntry(2000, 2030, scn);

		}

	}

	/** TYLER
	 * shows the schedule page (can be either current or saved schedule)
	 * should call the displaySchedule task from Schedule class
	 * @param s
	 */
	public void viewSchedule(Schedule s) {
	}

	/**
	 * John's Task --
	 * implements console interaction with the schedule using methods in currentSchedule
	 * @param s CurrentSchedule
	 */
	public void consoleAlterSchedule(CurrentSchedule s){
		int choice;
		String courseEntry;
		String sectionEntry;
		while(true) {
			viewSchedule(s);
			System.out.println("Alter Schedule Choices: ");
			System.out.println("1. Add Course\n2. Remove Course\n3. Return to Main Menu");
			choice = intEntry(1, 3, scn);
			if (choice == 1){ // add course
				// course search
				ArrayList<Course> results = consoleSearch();

				// ask for course to add (option to add none and/or search again)
				System.out.println("Which course would you like to add? (case sensitive)");
				System.out.println("(enter the course code without the section to add; enter NONE to add none)");
				System.out.print("Course Entry: ");
				courseEntry = scn.next();

				// check if none
				if (courseEntry.equals("NONE")){
					continue;
				}

				System.out.println("Which section? ");
				sectionEntry = scn.next();

				/* find which course(s) they entered
				(some course sections have 2 course objects if the times differ across days)
				 */
				ArrayList<Course> coursesToAdd = new ArrayList<>();
				for (Course c: results){
					if (c.getCode().equals(courseEntry) && c.getSection() == sectionEntry.charAt(0)){
						coursesToAdd.add(c);
					}
				}

				// check if there is a conflict before adding
				CurrentSchedule cs = new CurrentSchedule(currentStudent.getCurrentSchedule().getCourseList());
				boolean hasConflict = false;
				for (Course c: coursesToAdd){
					if (cs.conflictsWith(c)){
						hasConflict = true;
					}
				}

				if (hasConflict){
					System.out.println("The course you selected conflicts with another course in your schedule," +
							" so it cannot be added.");
				} else {
					for (Course c: coursesToAdd){
						cs.addCourse(c);
					}
					System.out.println("Course Added.");
				}

				currentStudent.setCurrentSchedule(cs);

			}else if (choice == 2) { // remove course
				System.out.println("Which course would you like to add? (case sensitive)");
				System.out.println("(enter the course code without the section to remove; enter NONE to remove none)");
				System.out.print("Course Entry: ");
				courseEntry = scn.next();

				// check if none
				if (courseEntry.equals("NONE")){
					continue;
				}

				// remove course
				CurrentSchedule cs = new CurrentSchedule(currentStudent.getCurrentSchedule().getCourseList());
				boolean didRem = cs.removeCourse(courseEntry);

				// give result
				if (didRem){
					System.out.println("Course Removed.");
				} else {
					System.out.println("Course not found in your current schedule, so nothing was removed.");
				}

				currentStudent.setCurrentSchedule(cs);

			} else if (choice == 3) { // return
				break;
			} else { // invalid choice (shouldn't reach this)
				System.out.println("Invalid choice, try again.");
			}
		}
		mainMenu();
	}

	public static void menuNav(int menuId){
		switch(menuId){
			case 0:
				mainMenu();
				break;
			case 1:
				consoleCreateAccount();
				break;
			case 2:
				consoleLogin();
				break;
			case 3:
				consoleCreateGuest();
				break;
			default:
				System.out.println("Invalid Selection");
				mainMenu();
				break;
		}
	}

	public static void mainMenu(){
		int selection = -1;
		//Scanner menu = new Scanner(System.in);
		System.out.println("Welcome!");
		System.out.println("1) Create an account");
		System.out.println("2) Log into an account");
		System.out.println("3) Continue as guest");
//		while(true){
//            try {
//                selection = menu.nextInt();
//                if (selection > 0 && selection < 4){
//                    break;
//                } else {
//                    System.out.println("Invalid, try again.");
//                }
//            } catch (Exception e){
//                menu.next();
//                System.out.println("Invalid, try again.");
//            }
//        }
//		menuNav(selection);
	}

	/** JOHN
	 *
	 */
	public static void consoleLogin() {
		//Scanner input = new Scanner(System.in);
		System.out.println("Username: ");
		String username = scn.next();
		System.out.println("Password: ");
		String password = scn.next();

	}
	/** JOHN
	 *
	 */
	public static void consoleCreateAccount(){
		//Scanner input = new Scanner(System.in);
		String userEmail;
		String userPassword;

		while (true) {
			System.out.println("Email: ");
			userEmail = scn.next();
			System.out.println("Password: ");
			userPassword = scn.next();

			try {
				// TODO: a blank schedule for the current semester will be created by default
				db.addAccount(userEmail, userPassword, "SPR2022");
				break;
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}

		currentStudent = new Account(userEmail, userPassword);
		CurrentSchedule newSchedule = new CurrentSchedule();
		currentStudent.setCurrentSchedule(newSchedule);
		consoleScheduleChoice();
	}

	public static void testMenu() {
		menuNav(0);
	}

	public static void main(String args[]) {
//		menuNav(0);
//		Schedule tests = new Schedule(courses, "Spring 2023");
//		tests.displaySchedule();
//		testMenu();
		//A suggestion by Dr. Hutchins
		//to have a while loop for navigation.
		int pageID = 0;
		while (true){
			if (pageID == 0){
				menuNav(0);
				pageID = intEntry(1, 3, scn);
			}
			else if (pageID == 1){
				menuNav(pageID);
				//lead to scheduling page
			}
			else if (pageID == 2){
				menuNav(pageID);
				//lead to scheduling page
			}
			else if (pageID == 3){
				menuNav(pageID);
				//lead to scheduling page
			}
			else if (pageID == 4){
				menuNav(pageID);
				//lead to scheduling page
			} 

		}

	}





}
