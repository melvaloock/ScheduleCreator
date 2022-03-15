package ScheduleCreator;

public class Student {

	protected Schedule currentSchedule;
	protected String major;
	protected int year;

	public Student() {}

	public Schedule addRecommendedSchedule() {return null;}

	/** Creates a new schedule, and sets it as a current schedule
	 */
	public void createNewSchedule() {
		//use the setCurrentSchedule() method to set as current after creating schedule
	}

	public Schedule getCurrentSchedule() {
		return currentSchedule;
	}

	public void setCurrentSchedule(Schedule currentSchedule) {
		this.currentSchedule = currentSchedule;
		currentSchedule.setCurrent(true);
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
}
