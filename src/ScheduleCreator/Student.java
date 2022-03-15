package ScheduleCreator;

public class Student {

	protected Schedule currentSchedule;
	protected String major;
	protected int year;

	public Student() {}

	public Schedule addRecommendedSchedule() {return null;}
	public void createNewSchedule() {}

	public Schedule getCurrentSchedule() {
		return currentSchedule;
	}

	public void setCurrentSchedule(Schedule currentSchedule) {
		this.currentSchedule = currentSchedule;
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
