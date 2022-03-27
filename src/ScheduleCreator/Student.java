package ScheduleCreator;

public class Student {

	protected CurrentSchedule currentSchedule;
	protected String major;
	protected int year;

	public Student() {}

	public Student(Schedule currentSchedule, String major, int year) {
		this.currentSchedule = (CurrentSchedule) currentSchedule;
		this.major = major;
		this.year = year;
	}

	public Schedule addRecommendedSchedule() {return null;}

	/** Creates a new schedule, and sets it as a current schedule
	 */
	public void createNewSchedule() {
		//use the setCurrentSchedule() method to set as current after creating schedule
	}

	public Schedule getCurrentSchedule() {
		return currentSchedule;
	}

	//i think this might be broken
	public void setCurrentSchedule(Schedule currentSchedule) {
		currentSchedule.makeCurrent();
		currentSchedule.setCurrent(true);
		this.currentSchedule = (CurrentSchedule) currentSchedule;
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
