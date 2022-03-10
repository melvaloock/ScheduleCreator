package ScheduleCreator.ScheduleCreator;

import java.util.*;

public class UserInterface {
	
	private static HashMap<String, Schedule> recommendedSchedules;
	private static ArrayList<Course> courses;
	private static Student currentStudent;
	private Scanner searchScan = new Scanner(System.in); //takes user input for now

	
	public ArrayList<Course> searchCoursesByCode(String code) {return null;}
	public ArrayList<Course> searchCoursesByKeyword(String keyword) {return null;}
	public ArrayList<Course> dayFilter(ArrayList<Course> courses, ArrayList<String> days) {return null;}
	public ArrayList<Course> timeFilter(ArrayList<Course> courses, ArrayList<String> times) {return null;}

	public void addRecommendedSchedule(RecommendedSchedule r) {}
	public void createGuest() {}
	public void viewSchedule(Schedule s) {

	}
	public static void createAccount(){
//		System.out.println("Username: ");
//		String username = searchScan.next();
//		System.out.println("Password: ");
//		String password = searchScan.next();

	}
	public static void menuNav(int menuId){
		switch(menuId){
			case 0: mainMenu();
			case 1: //create account;
		}
	}

	public static void mainMenu(){
		System.out.println("Welcome!");
		System.out.println("1) Create an account");
		System.out.println("2) Log into an account");
		System.out.println("3) Continue as guest");
	}

	public void login() {
		System.out.println("Username: ");
		String username = searchScan.next();
		System.out.println("Password: ");
		String password = searchScan.next();

	}

	public static void main(String args[]) {
		Test test = new Test();
		test.test1();
		menuNav(0);
		Schedule tests = new Schedule(courses, "Spring 2023");
		tests.displaySchedule();
	}





}
