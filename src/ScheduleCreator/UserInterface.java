package ScheduleCreator;

import java.util.*;

public class UserInterface {
	
	private static HashMap<String, Schedule> recommendedSchedules;
	private static ArrayList<Course> courses;
	private static Student currentStudent;
	private Scanner searchScan = new Scanner(System.in); //takes user input for now


	public static void consoleSearch() {
		Scanner searchScan = new Scanner(System.in); //takes user input for now
		System.out.println("Would you like to search by code (1) or keyword (2)?");
		int searchType = searchScan.nextInt();
		searchScan.nextLine();
		if (searchType == 1) {
			System.out.println("Searching by course code \nEnter the course code you would like to search for");

			String code = searchScan.nextLine().toUpperCase(); //matches all to the format in the database
			//check for valid user input
			ArrayList<Course> searchResults = searchCoursesByCode(code);
			System.out.println("Search results:");
			for (int i = 1; i <= searchResults.size(); i++) {
				System.out.println(i + ". " +searchResults.get(i - 1));
			}
		}
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
	public ArrayList<Course> searchCoursesByKeyword(String keyword) {return null;}
	public ArrayList<Course> dayFilter(ArrayList<Course> courses, ArrayList<String> days) {return null;}
	public ArrayList<Course> timeFilter(ArrayList<Course> courses, ArrayList<String> times) {return null;}

	public void addRecommendedSchedule(RecommendedSchedule r) {}
	public static void createGuest() {
		currentStudent = new Student();
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
		while (selection < 0 || selection > 3){
			try{
				selection = menu.nextInt();
			}
			catch (Exception e){
				System.out.println("Invalid Selection");
				selection = -1;
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

	public static void test1() {
		courses = new ArrayList<Course>();

		Course c1 = new Course("MUSI 102", "MUSIC HISTORY II", "9:00 AM", "9:50 AM", 'A', "MWF");
		Course c2 = new Course("MUSI 102", "MUSIC HISTORY II", "9:00 AM", "9:50 AM", 'B', "MWF");
		Course c3 = new Course("COMP 141", "INTRO TO PROGRAM", "11:00 AM", "11:50 AM", 'A', "MWF");

		System.out.println(c1);

		courses.add(c1);
		courses.add(c2);
		courses.add(c3);


		System.out.println(courses);
		System.out.println(searchCoursesByCode("COMP") + "\n");

		consoleSearch();
	}

	public static void testMenu() {
		menuNav(0);
	}

	public static void main(String args[]) {
//		menuNav(0);
//		Schedule tests = new Schedule(courses, "Spring 2023");
//		tests.displaySchedule();

		//test1();
		testMenu();
	}





}
