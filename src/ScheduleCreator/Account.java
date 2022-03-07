package ScheduleCreator.ScheduleCreator;

import java.util.*; // trying to get git to work


public class Account extends Student {
	
	private HashMap<String, Schedule> scheduleMap = new HashMap<String, Schedule>();
	private String username;
	private String passwordHash;
	private int studentID;	
	private String advisorEmail;

	public Account(String username, String passwordHash) {
		//see if they are in the database
		//load hashmap from database

		//else, create default schedule
	}



	public void saveCurrentSchedule() {}

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
