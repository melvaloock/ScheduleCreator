package sleeplessdevelopers.schedulecreator;

import java.util.*;

import static java.util.Arrays.asList;

public class Course {

	private String title;
	private String code;
	private String semester; //Spring 2022
	private String color;
	private int referenceNum;
	private String startTime;
	private String endTime;
	private ArrayList<Day> days;// figure out how to do enum later
	private String professor;
	private char section;
	private int totalSeats;
	private int openSeats;

	public Course(int referenceNum, String code, String title, String startTime, String endTime, char section, ArrayList<Day> days) {
		this.referenceNum = referenceNum;
		this.code = code;
		this.title= title;
		this.startTime = startTime;
		this.endTime = endTime;
		this.section = section;
		this.days = days;
	}

	public Course(String code, String title, String startTime, String endTime, char section, String days, int referenceNum) {
		this.code = code;
		this.title = title;
		this.startTime = startTime;
		this.endTime = endTime;
		this.section = section;
		this.days = daysToEnum(days);
		this.referenceNum = referenceNum;
	}

	public Course(String code, String title, String startTime, String endTime, char section, String days) {
		this.code = code;
		this.title = title;
		this.startTime = startTime;
		this.endTime = endTime;
		this.section = section;
		this.days = daysToEnum(days);
	}

	@Override
	public boolean equals(Object obj){
		if (this == obj) {
			return true;
		}

		Course c;
		if (obj instanceof Course){
			c = (Course)obj;
		}else {
			return false;
		}

		if (!code.equals(c.code)){
			return false;
		}
		if (!title.equals(c.title)){
			return false;
		}
		if (!startTime.equals(c.startTime)){
			return false;
		}
		if (!endTime.equals(c.endTime)){
			return false;
		}
		if (section != c.section){
			return false;
		}
		if (!days.equals(c.days)){
			return false;
		}
		if (!professor.equals(c.professor)){
			return false;
		}
		if (referenceNum != c.referenceNum){
			return false;
		}
		

		return true;
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

	public String daysToString() {
		String dayString = "";
		for (Day day : days) {
			if (day != Day.NULL) {	//NULL just goes to empty string
				dayString += String.valueOf(day);
			}
		}
		return dayString;
	}

	@Override
	public String toString() {
		String ds = daysToString();
		return String.format("%s | %s | %s %s - %s", code, title, ds, startTime, endTime);
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
		return daysToString();
	}

	//temp method
	public ArrayList<String> getDayStringList(){

		ArrayList<String> stringList = new ArrayList<>();
		if (days.size() == 0) {
			return stringList;
		}
		for(int x = 0; x<days.size(); x++){
			stringList.add(x, Day.toDayString(days.get(x)));
		}
		return stringList;
	}

	public ArrayList<Character> getDayList() {
		ArrayList<Character> charList = new ArrayList<>();
		for (int i = 0; i < days.size(); i++) {
			charList.add(daysToString().charAt(i));
		}
		return charList;
	}

	public String CSSStartTime() {
		if (startTime.charAt(1) == ':') {
			return "0" + startTime.charAt(0);
		}
		return startTime.substring(0,2);
	}

	public String CSSEndTime() {
		if (endTime.charAt(1) == ':') {
			if(endTime.charAt(0) == '9' || endTime.charAt(0) == '8') {
				return String.valueOf(Integer.valueOf(endTime.substring(0,1))+ 2);
			}
			return "0" + (Integer.valueOf(endTime.substring(0,1)) + 2);
		}
		return endTime.substring(0,2);
	}

	public String toCSSString(String dayString) {
		return "grid-column:  " + dayString.toLowerCase() + ";	" +
		 "grid-row:	h" + CSSStartTime() +
		  "	/	h" + CSSEndTime() + ";	";
	}

	public String displaySpecial() {
		StringBuilder specials = new StringBuilder();
		String[] startData = startTime.split(":");
		String[] endData = endTime.split(":");
		int startMins = Integer.valueOf(startData[1]);
		int endMins = Integer.valueOf(endData[1]);
		int startHours = Integer.valueOf(startData[0]);
		int endHours =  Integer.valueOf(endData[0]);

		if (startMins > 15) {
			specials.append("latestart ");
		}
		if (endMins < 45) {
			specials.append("earlyend ");
		}

		specials.append("event ");

		return specials.toString();
	}
	public void setDays(String days) {
		this.days = daysToEnum(days);
	}


}
