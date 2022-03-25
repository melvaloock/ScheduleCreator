package ScheduleCreator;

import java.util.*; // trying to get git to work


public class Account extends Student {

	private HashMap<String, Schedule> scheduleMap = new HashMap<String, Schedule>(); // stores saved schedules for now

	private String email;
	private String passwordHash;
	private int studentID; // may not need this
	private String advisorEmail;


	public Account(String username, String passwordHash, Schedule currentSchedule, String major, int year) {
		//see if they are in the database
		//load hashmap from database

		super(currentSchedule, major, year);
		this.email = username;
		this.passwordHash = passwordHash;

		//else, create default (blank) schedule
	}

	public Account(String username, String passwordHash) {
		this.email = username;
		this.passwordHash = passwordHash;
	}

	/** CHRISTIAN & KEVIN
	 * adds user's currentSchedule (inherited from parent class Student) to their saved schedules
	 */
	public void saveCurrentSchedule(Schedule sch, String email, Database db) {
		//TODO: add currentSchedule to scheduleMap (or however saved schedules will be stored)
		// - this includes adding a label (key) to this schedule so that it can be accessed
		try {
			db.addSchedule(sch.getSemester(), sch.isCurrent(), email);
			for (Course c : sch.getCourseList()){
				db.addCourseRef(c.getCode(), c.getTitle(), c.getSemester(), email);
			}

		}
		catch (Exception e){

		}

		
	}

	public void setCurrentSchedule(Schedule currentSchedule, Database db) {
		if (this.currentSchedule != null) {
			saveCurrentSchedule(currentSchedule, email, db);
			this.currentSchedule.setCurrent(false);
		}
		super.setCurrentSchedule(currentSchedule);
	}

	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}

	public void setAdvisorEmail(String advisorEmail) {
		this.advisorEmail = advisorEmail;
	}

	public String getAdvisorEmail() {
		return advisorEmail;
	}

	public String getEmail() {
		return email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public int getStudentID() {
		return studentID;
	}
}
