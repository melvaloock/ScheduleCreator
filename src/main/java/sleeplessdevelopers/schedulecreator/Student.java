package sleeplessdevelopers.schedulecreator;

public class Student {

	protected CurrentSchedule currentSchedule;
	protected String major;
	protected int year;

	public Student() {}

	public Student(CurrentSchedule currentSchedule, String major, int year) {
		this.currentSchedule = currentSchedule;
		this.major = major;
		this.year = year;
	}

	public Schedule addRecommendedSchedule() {return null;}

	/** Creates a new schedule, and sets it as a current schedule
	 */
	public void createNewSchedule() {
		//use the setCurrentSchedule() method to set as current after creating schedule
	}

	public CurrentSchedule getCurrentSchedule() {
		return currentSchedule;
	}

	public void setCurrentSchedule(CurrentSchedule currentSchedule) {
		currentSchedule.makeCurrent();
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
