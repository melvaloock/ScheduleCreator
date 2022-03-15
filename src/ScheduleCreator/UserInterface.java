package ScheduleCreator;

import java.util.*;

public class UserInterface {
	
	private static HashMap<String, Schedule> recommendedSchedules;
	private static ArrayList<Course> courses;
	private static Student currentStudent;


	public static void consoleSearch() {

		ArrayList<Course> searchResults = new ArrayList<>();
		Scanner searchScan = new Scanner(System.in); //takes user input for now
		System.out.println("Would you like to search by code (1) or keyword (2)?");
		int searchType = intEntry(1, 2, searchScan);
		searchScan.nextLine();
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

	public void addRecommendedSchedule(RecommendedSchedule r) {}
	public static void createGuest() {
		currentStudent = new Student();

	}
	public static void autoScheduler(){
		Scanner scn = new Scanner(System.in);
		int yn;
		System.out.print("Do you want scheduling be automatic according to your year and major?: ");
		yn = ynEntry(scn);
		if (yn == 'Y'){
		}

	}

	public void viewSchedule(Schedule s) {

	}

	public void alterSchedule(Schedule s){

	}

	public static void createAccount(){
		Scanner input = new Scanner(System.in);
		System.out.println("Username: ");
		String username = input.next();
		System.out.println("Password: ");
		String password = input.next();

	}
	public static void menuNav(int menuId){
		switch(menuId){
			case 0:
				mainMenu();
				break;
			case 1:
				createAccount();
				break;
			case 2:
				login();
				break;
			case 3:
				createGuest();
				break;
			default:
				System.out.println("Invalid Selection");
				mainMenu();
				break;
		}
	}

	public static void mainMenu(){
		int selection = -1;
		Scanner menu = new Scanner(System.in);
		System.out.println("Welcome!");
		System.out.println("1) Create an account");
		System.out.println("2) Log into an account");
		System.out.println("3) Continue as guest");
		while(true){
            try {
                selection = menu.nextInt();
                if (selection > 0 && selection < 4){
                    break;
                } else {
                    System.out.println("Invalid, try again.");
                }
            } catch (Exception e){
                menu.next();
                System.out.println("Invalid, try again.");
            }
        }
		menuNav(selection);
	}

	public static void login() {
		Scanner input = new Scanner(System.in);
		System.out.println("Username: ");
		String username = input.next();
		System.out.println("Password: ");
		String password = input.next();

	}

	public static void testMenu() {
		menuNav(0);
	}

	public static void test2() {
		courses = new ArrayList<Course>();

		Course c1 = new Course("MUSI 102", "MUSIC HISTORY II", "9:00 AM", "9:50 AM", 'A', "MWF");
		Course c2 = new Course("MUSI 102", "MUSIC HISTORY II", "9:00 AM", "9:50 AM", 'B', "MWF");
		Course c3 = new Course("COMP 141", "INTRO TO PROGRAM", "11:00 AM", "11:50 AM", 'A', "MWF");
		Course c4 = new Course("COMP 205", "ETHICS, FAITH, AND THE CONSCIOUS MIND", "10:00 AM", "11:15 AM", 'A', "TR");


		System.out.println(c1);

		courses.add(c1);
		courses.add(c2);
		courses.add(c3);
		courses.add(c4);


		System.out.println(courses);
		System.out.println(searchCoursesByCode("COMP") + "\n");

		consoleSearch();
	}

	public static void main(String args[]) {
//		menuNav(0);
//		Schedule tests = new Schedule(courses, "Spring 2023");
//		tests.displaySchedule();

//		testMenu();
		test2();
	}





}
