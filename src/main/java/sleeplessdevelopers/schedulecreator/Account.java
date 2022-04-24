package sleeplessdevelopers.schedulecreator;

import java.util.*; // trying to get git to work


public class Account extends Student {

	private String email;
	private String passwordHash;
	private int studentID; // may not need this
	private String advisorEmail;


	public Account(String username, String passwordHash, CurrentSchedule currentSchedule, String major, int year) {
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

	// TODO: fix in sprint 2 --> this is a temporary default constructor for Account
	// so a guest can be created (it may or may not be needed, idk)
	public Account() {};


	public void saveCurrentSchedule(Database db) {
		//TODO: add currentSchedule to scheduleMap (or however saved schedules will be stored)
		// - this includes adding a label (key) to this schedule so that it can be accessed
		if (getEmail() == null) {
			System.out.println("\n You must have an account to save a Schedule");
			return;
		}
		try {
			Schedule checkSch = db.getCurrentSchedule(getEmail());
			if (checkSch == null) {
				// add a current schedule to the database if it isn't already there
				db.addSchedule(getCurrentSchedule().semester, true, getEmail());
				db.updateCourseRefs(getCurrentSchedule().semester, getEmail(), getCurrentSchedule().getCourseList());
			} else {
				// update the current schedule in the database
				db.updateCourseRefs(getCurrentSchedule().semester, getEmail(), getCurrentSchedule().getCourseList());
			}
		}
		catch (Exception e){
			System.out.println(e.getMessage());
		}

		
	}

	public void setCurrentSchedule(CurrentSchedule currentSchedule, Database db) {
		if (this.currentSchedule != null) {
			saveCurrentSchedule(db);
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		Account acct;
		if (obj instanceof Account){
			acct = (Account)obj;
		} else {
			return false;
		}

		// TODO: finish this method once Account is set up


		return true;
	}
}
