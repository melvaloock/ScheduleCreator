package ScheduleCreator;

public class Course {

	private String title;
	private String code;
	private String semester; //Spring 2022
	private String color;
	private int referenceNum;
	private String time;
	private String days;// figure out how to do enum later
	private String professor;
	private char section;
	private int totalSeats;
	private int openSeats;

	public Course(String code, String professor, String time, char section, String days) {
		this.code = code;
		this.professor = professor;
		this.time = time;
		this.section = section;
		this.days = days;
	}

	@Override
	public String toString() {return "";}
	public boolean isFull() {return false;}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getReferenceNum() {
		return referenceNum;
	}

	public void setReferenceNum(int referenceNum) {
		this.referenceNum = referenceNum;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getProfessor() {
		return professor;
	}

	public void setProfessor(String professor) {
		this.professor = professor;
	}

	public char getSection() {
		return section;
	}

	public void setSection(char section) {
		this.section = section;
	}

	public int getTotalSeats() {
		return totalSeats;
	}

	public void setTotalSeats(int totalSeats) {
		this.totalSeats = totalSeats;
	}

	public int getOpenSeats() {
		return openSeats;
	}

	public void setOpenSeats(int openSeats) {
		this.openSeats = openSeats;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}
}
