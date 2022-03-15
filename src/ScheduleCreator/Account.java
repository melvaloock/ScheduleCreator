package ScheduleCreator;

import java.util.*; // trying to get git to work


public class Account extends Student {

	//TODO: @KEVIN figure out how to store account schedules with database -- my hunch is that this is
	// temporary
	private HashMap<String, Schedule> scheduleMap = new HashMap<String, Schedule>(); // stores saved schedules for now

	private String username;
	private String passwordHash;
	private int studentID;	
	private String advisorEmail;


	public Account(String username, String passwordHash) {
		//see if they are in the database
		//load hashmap from database

		//else, create default (blank) schedule
	}

	/** CHRISTIAN & KEVIN
	 * adds user's currentSchedule (inherited from parent class Student) to their saved schedules
	 */
	public void saveCurrentSchedule() {
		//TODO: add currentSchedule to scheduleMap (or however saved schedules will be stored)
		// - this includes adding a label (key) to this schedule so that it can be accessed
	}

	public void setCurrentSchedule(Schedule currentSchedule) {
		if (this.currentSchedule != null) {
			saveCurrentSchedule();
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

	public String getUsername() {
		return username;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public int getStudentID() {
		return studentID;
	}
}
