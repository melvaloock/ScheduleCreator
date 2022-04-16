package sleeplessdevelopers.schedulecreator;

import java.text.ParseException;
import java.util.*;
import java.io.*;

public class Schedule {

	protected ArrayList<Course> courseList;
	protected String semester = "Fall 2022";
	protected boolean isCurrent;

	// used to create a schedule matrix; there may be a better solution, idk
	private static final int ROWS = 14;
	private static final int COLS = 6;

	/**
	 * 	default constructor, creates blank schedule
	 */
	public Schedule() {
		this.courseList = new ArrayList<Course>();
	}

	public Schedule(ArrayList<Course> courseList, String semester) {
		this.courseList = courseList;
		this.semester = semester;
	}

	public void exportSchedule() {}

	public void displaySchedule() {
		String[][] schedule = new String[14][6];
		String[] days = {"  Time  ","M","T","W","R","F"};
		//Make an Empty Schedule
		int PMTime = 1 ;
		for(int row = 0; row<schedule.length; row++){
			if(row == 0){
				System.out.println("*********************** " + getSemester() + " GCC Schedule " + "***********************");
			}
			for(int col = 0; col<schedule[row].length; col++){
				if(row == 0 && col == 0){
					schedule[row][col] = days[col] + "\t\t";
				}else if(row == 0){
					schedule[row][col] = days[col] + "\t\t\t";
				}else if (row < 5){
					if(row<3){
						schedule[row][0]= "0" + Integer.toString(row + 7) + ":00 AM";
						schedule[row][col] ="\t\t*\t";
					}else{
						schedule[row][0]= Integer.toString(row + 7) + ":00 AM";
						schedule[row][col] ="\t\t*\t";
					}
				}else if(row == 5){
					schedule[row][0]= Integer.toString(row + 7) + ":00 PM";
					schedule[row][col] ="\t\t*\t";
				}else{
					schedule[row][0]= "0" + Integer.toString(PMTime) + ":00 PM";
					schedule[row][col] ="\t\t*\t";
				}
			}
			if(row>5){
				PMTime++;
			}
		}
		if(courseList.isEmpty()){
			for(int x =0; x< schedule.length; x++){
				for(int y =0; y< schedule[x].length; y++){
					System.out.print(schedule[x][y]);
				}
				System.out.println();
			}
		}else{ //replace the "*" in the blank schedule with the needed class
			for(int x =1; x< schedule.length-1; x++){//2
				for(int y =1; y< schedule[x].length; y++) {//1
					for (Course curCourse : courseList) {
						ArrayList<Character> curCourseDays = curCourse.getDayList();
						//schedule[x+1][0].substring(3,5)
						for(int index = 0; index<curCourseDays.size(); index++){
							if(curCourseDays.get(index).equals('M')) {//do something else besides contains to check for something comparable
								if(curCourse.getStartTime().equals(schedule[x][0]) && curCourse.getEndTime().contains(schedule[x][0].substring(0,2)) ){
									schedule[x][y] = "\t\t" + curCourse.getCode() ;
								}else if(curCourse.getStartTime().equals(schedule[x][0]) && curCourse.getEndTime().contains(schedule[x+1][0].substring(0,2)) ){
									schedule[x][y] = "\t\t" + curCourse.getCode() ;
									schedule[x+1][y]= "\t\t" + curCourse.getCode();
								}else if(curCourse.getStartTime().equals(schedule[x][0]) && curCourse.getEndTime().contains(schedule[x+2][0].substring(0,2)) ){
									schedule[x][y] = "\t\t" + curCourse.getCode() ;
									schedule[x+1][y]= "\t\t" + curCourse.getCode();
									schedule[x+2][y]= "\t\t" + curCourse.getCode();
								}
							}if(curCourseDays.get(index).equals('T')) {
								if(curCourse.getStartTime().equals(schedule[x][0]) && curCourse.getEndTime().contains(schedule[x][0].substring(0,2)) ){
									schedule[x][y] = "\t\t" + curCourse.getCode() ;
								}else if(curCourse.getStartTime().equals(schedule[x][0]) && curCourse.getEndTime().contains(schedule[x+1][0].substring(0,2)) ){
									schedule[x][y] = "\t\t" + curCourse.getCode() ;
									schedule[x+1][y]= "\t\t" + curCourse.getCode();
								}else if(curCourse.getStartTime().equals(schedule[x][0]) && curCourse.getEndTime().contains(schedule[x+2][0].substring(0,2)) ){
									schedule[x][y] = "\t\t" + curCourse.getCode() ;
									schedule[x+1][y]= "\t\t" + curCourse.getCode();
									schedule[x+2][y]= "\t\t" + curCourse.getCode();
								}
							}if(curCourseDays.get(index).equals('W')) {
								if(curCourse.getStartTime().equals(schedule[x][0]) && curCourse.getEndTime().contains(schedule[x][0].substring(0,2)) ){
									schedule[x][y] = "\t" + curCourse.getCode() ;
								}else if(curCourse.getStartTime().equals(schedule[x][0]) && curCourse.getEndTime().contains(schedule[x+1][0].substring(0,2)) ){
									schedule[x][y] = "\t" + curCourse.getCode() ;
									schedule[x+1][y]= "\t" + curCourse.getCode();
								}else if(curCourse.getStartTime().equals(schedule[x][0]) && curCourse.getEndTime().contains(schedule[x+2][0].substring(0,2)) ){
									schedule[x][y] = "\t" + curCourse.getCode() ;
									schedule[x+1][y]= "\t" + curCourse.getCode();
									schedule[x+2][y]= "\t" + curCourse.getCode();
								}
							}if(curCourseDays.get(index).equals('R')) {
								if(curCourse.getStartTime().equals(schedule[x][0]) && curCourse.getEndTime().contains(schedule[x][0].substring(0,2)) ){
									schedule[x][y] = "\t" + curCourse.getCode() ;
								}else if(curCourse.getStartTime().equals(schedule[x][0]) && curCourse.getEndTime().contains(schedule[x+1][0].substring(0,2)) ){
									schedule[x][y] = "\t" + curCourse.getCode() ;
									schedule[x+1][y]= "\t" + curCourse.getCode();
								}else if(curCourse.getStartTime().equals(schedule[x][0]) && curCourse.getEndTime().contains(schedule[x+2][0].substring(0,2)) ){
									schedule[x][y] = "\t" + curCourse.getCode() ;
									schedule[x+1][y]= "\t" + curCourse.getCode() ;
									schedule[x+2][y]= "\t" + curCourse.getCode() ;
								}
							}if(curCourseDays.get(index).equals('F')) {
								if(curCourse.getStartTime().equals(schedule[x][0]) && curCourse.getEndTime().contains(schedule[x][0].substring(0,2)) ){
									schedule[x][y] = "\t" + curCourse.getCode() ;
								}else if(curCourse.getStartTime().equals(schedule[x][0]) && curCourse.getEndTime().contains(schedule[x+1][0].substring(0,2)) ){
									schedule[x][y] = "\t" + curCourse.getCode() ;
									schedule[x+1][y]= "\t" + curCourse.getCode() ;
								}else if(curCourse.getStartTime().equals(schedule[x][0]) && curCourse.getEndTime().contains(schedule[x+2][0].substring(0,2)) ){
									schedule[x][y] = "\t" + curCourse.getCode() ;
									schedule[x+1][y]= "\t" + curCourse.getCode();
									schedule[x+2][y]= "\t" + curCourse.getCode();
								}
							}
						}

					}
				}
			}
			for(int x =0; x< schedule.length; x++){
				for(int y =0; y< schedule[x].length; y++){
					System.out.print(schedule[x][y]);
				}
				System.out.println();
			}
		}

	};

	public void displaySchedule2() {
		String[][] schedule = createEmptySchedule();
		schedule = populateSchedule(schedule);
		System.out.println("*********************** " + getSemester() + " GCC Schedule " + "***********************");
		for (int row = 0; row < ROWS; row++) {
			System.out.println();
			for (int col = 0; col < COLS; col++) {
				System.out.printf("%-12s", schedule[row][col]);
			}
		}
		//display courses not yet implemeneted reference numbers - Tyler
		System.out.println("\n");
		for (Course c: getCourseList()) {
			System.out.println(c);
			//System.out.println(c.getReferenceNum() + " " + c);
		}
	}

	public String[][] createEmptySchedule() {
		String[][] schedule = new String[ROWS][COLS];
		String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri"};
		String[] times = {"8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM",
			"1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM", "6:00 PM", "7:00 PM", "8:00 PM"};

		schedule[0][0] = "";

		// add column labels
		for (int i = 1; i < COLS; i++) {
			schedule[0][i] = days[i - 1];
		}

		// add row labels
		for (int i = 1; i < ROWS; i++) {
			schedule[i][0] = times[i - 1];
		}

		// all others empty
		for (int row = 1; row < ROWS; row++) {
			for (int col = 1; col < COLS; col++) {
				schedule[row][col] = "---";
			}
		}
		return schedule;
	}

	public String[][] populateSchedule(String[][] schedule) {
		for (Course c: getCourseList()) {
			int row = getRow(c.getStartTime());
			for (char day: c.getDayList()) {
				int col = getCol(day);
				schedule[row][col] = c.getCode();
			}
		}
		return schedule;
	}
//gonna be the methods I change and alter
	public String[][] populateSchedule2(String[][] schedule) throws ParseException {
		for (Course c: getCourseList()) {
			DateAndTime dat = new DateAndTime();
			ArrayList<ArrayList<Integer>> location = new ArrayList<>();
//			c.getDayStringList();
			location = dat.locOfClass(c.getStartTime(), c.getEndTime(), c.getDayStringList());
//			System.out.println(c.getDayStringList());
			ArrayList<Integer> rowLoc  = location.get(0);
			ArrayList<Integer> colLoc  = location.get(1);
//			System.out.println(location);
//			System.out.println("rowLoc: " + rowLoc);
//			System.out.println("colLoc: " + colLoc);
			for(int x = 0; x< rowLoc.size(); x++){
				for(int y = 0; y<colLoc.size(); y++){
					schedule[rowLoc.get(x)][colLoc.get(y)]= c.getCode();
				}
			}
		}
		return schedule;
	}
	//method I'll change and alter
	public void displaySchedule3() throws ParseException {
		String[][] schedule = createEmptySchedule();
		schedule = populateSchedule2(schedule);
		System.out.println("*********************** " + getSemester() + " GCC Schedule " + "***********************");
		for (int row = 0; row < ROWS; row++) {
			System.out.println();
			for (int col = 0; col < COLS; col++) {
				System.out.printf("%-12s", schedule[row][col]);
			}
		}
		System.out.println("\n");
		for (Course c: getCourseList()) {
			System.out.println(c);
			//System.out.println(c.getReferenceNum() + " " + c);
		}
	}

	@Override
	public String toString() {
		String[][] schedule = createEmptySchedule();
		try {
			schedule = populateSchedule2(schedule);
		} catch (ParseException e) {}

		StringBuilder sb = new StringBuilder();
		sb.append("*********************** " + getSemester() + " GCC Schedule " + "***********************");
		for (int row = 0; row < ROWS; row++) {
			sb.append("\n");
			for (int col = 0; col < COLS; col++) {
				String str = String.format("%-12s", schedule[row][col]);
				sb.append(str);
			}
		}
		sb.append("\n");
		return sb.toString();
	}

	public int getCol(char day) {
		if (day == 'M') {
			return 1;
		} else if (day == 'T') {
			return 2;
		} else if (day == 'W') {
			return 3;
		} else if (day == 'R') {
			return 4;
		} else if (day == 'F') {
			return 5;
		} else {
			return -1;
		}
	}

	// yes, I know this is less than desirable--I am trying to just get it done
	public int getRow(String startTime) {
		if ((startTime.compareTo("8:00 AM") > 0 && startTime.compareTo("9:00 AM") < 0) || startTime.compareTo("8:00:00") == 0) {
			return 1;
		} else if ((startTime.compareTo("9:00 AM") > 0 && startTime.compareTo("10:00 AM") < 0) || startTime.compareTo("9:00:00") == 0) {
			return 2;
		} else if ((startTime.compareTo("10:00 AM") > 0 && startTime.compareTo("11:00 AM") < 0) || startTime.compareTo("10:00:00") == 0) {
			return 3;
		} else if ((startTime.compareTo("11:00 AM") > 0 && startTime.compareTo("12:00 AM") < 0) || startTime.compareTo("11:00:00") == 0) {
			return 4;
		} else if ((startTime.compareTo("12:00 PM") > 0 && startTime.compareTo("13:00 PM") < 0) || startTime.compareTo("12:00:00") == 0) {
			return 5;
		} else if ((startTime.compareTo("13:00 PM") > 0 && startTime.compareTo("14:00 PM") < 0) || startTime.compareTo("13:00:00") == 0) {
			return 6;
		} else if ((startTime.compareTo("14:00 PM") > 0 && startTime.compareTo("15:00 PM") < 0) || startTime.compareTo("14:00:00") == 0) {
			return 7;
		} else if ((startTime.compareTo("15:00 PM") > 0 && startTime.compareTo("16:00 PM") < 0) || startTime.compareTo("15:00:00") == 0) {
			return 8;
		} else if ((startTime.compareTo("16:00 PM") > 0 && startTime.compareTo("17:00 PM") < 0) || startTime.compareTo("16:00:00") == 0) {
			return 9;
		} else if ((startTime.compareTo("17:00 PM") > 0 && startTime.compareTo("18:00 PM") < 0) || startTime.compareTo("17:00:00") == 0) {
			return 10;
		} else if ((startTime.compareTo("18:00 PM") > 0 && startTime.compareTo("19:00 PM") < 0) || startTime.compareTo("18:00:00") == 0) {
			return 11;
		} else if ((startTime.compareTo("19:00 PM") > 0 && startTime.compareTo("20:00 PM") < 0) || startTime.compareTo("19:00:00") == 0) {
			return 12;
		} else if ((startTime.compareTo("20:00 PM") > 0 && startTime.compareTo("21:00 PM") < 0) || startTime.compareTo("20:00:00") == 0) {
			return 13;
		} else {
			return -1;
		}
	}

	public void makeCurrent() {}

	//create constructor for saved schedules in whatever format they are

	public ArrayList<Course> getCourseList() {
		return courseList;
	}

	public void setCourseList(ArrayList<Course> courseList) {
		this.courseList = courseList;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public boolean isCurrent() {
		return isCurrent;
	}

	public void setCurrent(boolean current) {
		isCurrent = current;
	}

	@Override
	public boolean equals(Object obj){
		if (this == obj) {
			return true;
		}

		Schedule sched;
		if (obj instanceof Schedule){
			sched = (Schedule) obj;
		}else {
			return false;
		}

		if (!this.courseList.equals(sched.courseList)){
			return false;
		}
		if (this.isCurrent != sched.isCurrent){
			return false;
		}
		if (!this.semester.equals(sched.semester)) {
			return false;
		}

		return true;
	}

	public String toJSON() {

		StringBuilder sb = new StringBuilder();
		// starting bracket
		sb.append("{");

		// add schedule info
		sb.append("\"semester\":\"").append(semester).append("\",");
		sb.append("\"isCurrent\":\"").append(isCurrent).append("\",");

		// add courses
		sb.append("\"courseList\":[");

		int i = 0;	// keeps track of where we are in courseList
		for (Course current: courseList) {
			sb.append(current.toJSON());

			// if not the last course, add a comma
			if (i != courseList.size()-1) {
				sb.append(",");
			}
			// increment our counter
			i++;
		}

		sb.append("]");

		// ending bracket
		sb.append("}");

		return sb.toString();
	}

	public Schedule fromJSON(String jsonString) {




		return this;
	}

	public static void main(String[] args) throws ParseException {
		Course c1 = new Course("MUSI 102", "MUSIC HISTORY II", "9:00:00", "9:50:00", 'A', "MWF");
		Course c4 = new Course("COMP 205", "INTRO TO PROGRAM", "11:00:00", "11:50:00", 'A', "MWF");
		Course c5 = new Course("COMP 233", "PARALLEL COMP", "11:00:00", "12:15:00", 'A', "TR");
		Course c6 = new Course("Test 000", "TEST COURSE", "13:00:00", "13:50:00", 'A', "MWF");

		ArrayList<Course> course = new ArrayList<Course>();
		course.add(c1);
		course.add(c4);
		course.add(c5);
		course.add(c6);
		System.out.println(course);

		//System.out.println(c1.getDayStringList());
		Schedule sch = new Schedule(course, "SPRING 2022");

		// Kevin's test
		sch.displaySchedule2();

		// Tyler's test
		Schedule sch2 = new Schedule(course, "FALL 2022");
		sch2.displaySchedule3();
	}
}
