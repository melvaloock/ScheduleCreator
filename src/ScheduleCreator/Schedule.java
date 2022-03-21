package ScheduleCreator;

import java.util.*;
import java.io.*;

public class Schedule {

	protected ArrayList<Course> courseList;
	protected String semester;
	protected String outFile;
	protected FileWriter export;
	protected boolean isCurrent;
	protected static String defaultSemester = "Fall 2023";

	/**
	 * 	default constructor, creates blank schedule
	 */
	public Schedule() {
		this.courseList = new ArrayList<Course>();
		this.semester = defaultSemester;

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

	public String getOutFile() {
		return outFile;
	}

	public void setOutFile(String outFile) {
		this.outFile = outFile;
	}

	public FileWriter getExport() {
		return export;
	}

	public void setExport(FileWriter export) {
		this.export = export;
	}

	public boolean isCurrent() {
		return isCurrent;
	}

	public void setCurrent(boolean current) {
		isCurrent = current;
	}
}
