package ScheduleCreator.ScheduleCreator;

import java.util.*;

import static java.util.Arrays.asList;

public class Course {

	private String title;
	private String code;
	private String semester; //Spring 2022
	private String color;
	private int referenceNum;
	enum Day {M, T, W, R, F, NULL}
	private String startTime;
	private String endTime;
	private ArrayList<Day> days;// figure out how to do enum later
	private String professor;
	private char section;
	private int totalSeats;
	private int openSeats;

	public Course(String code, String title, String startTime, String endTime, char section, ArrayList<Day> days) {
		this.code = code;
		this.title= title;
		this.startTime = startTime;
		this.endTime = endTime;
		this.section = section;
		this.days = days;
	}

	public Course(String code, String title, String startTime, String endTime, char section, String days) {
		this.code = code;
		this.title = title;
		this.startTime = startTime;
		this.endTime = endTime;
		this.section = section;
		this.days = daysToEnum(days);
	}

	/**
	 * converts a string into an array of Days (enum)
	 * @param days
	 * @return dayList, the array of enumerated Days
	 */
	public ArrayList<Day> daysToEnum(String days) {
		ArrayList<Day> dayList = new ArrayList<Day>();
		if (days == "") {
			dayList.add(Day.NULL);
			return dayList;
		}
		//iterates through each character in the string
		for (char c : days.toCharArray()) {
			//if the character is valid as an enum, convert to enum and add to list
			if (asList(Day.values()).contains(Day.valueOf(String.valueOf(c)))) {
				dayList.add(Day.valueOf(String.valueOf(c)));
			} else { //else if
				dayList.add(Day.NULL);
			}
		}
		return dayList;
	}

	public String daysToString(ArrayList<Day> dayList) {
		String dayString = "";
		for (Day day : dayList) {
			dayString.concat(String.valueOf(day));
		}
		return dayString;
	}

	@Override
	public String toString() {
		String ds = daysToString(days);
		String cs = String.format("%s (%c) | %s | %s %s - %s", code, section, title, ds, startTime, endTime);
		return cs;
	}

	public boolean isFull() {
		if (openSeats == 0) {
			return true;
		} else {
			return false;
		}
	}

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

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
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
		return days.toString();
	}

	public void setDays(String days) {
		this.days = daysToEnum(days);
	}
}
