package ScheduleCreator;

import java.util.*;
import java.io.*;

public class Schedule {

	protected ArrayList<Course> courseList = new ArrayList<Course>();
	protected String semester;
	protected String outFile;
	protected FileWriter export;
	protected boolean isCurrent;
	protected static String defaultSemester = "Fall 2023";

	public void exportSchedule() {}


	public void displaySchedule() {
		String[][] schedule = new String[14][6];
		String[] days = {"Monday","Tuesday","Wednesday","Thursday","Friday"};
		//Make an Empty Schedule
		for(int row = 0; row<schedule.length; row++){
			if(row == 0){
				System.out.println("************************* " + getSemester() + "  GCC Schedule " + " *************************");
			}
			for(int col = 0; col<schedule[row].length; col++){
				if(row == 0){
					schedule[row][col] = "*************";
				}else if(row == 1){
					if(col == 0){
						schedule[row][col] = "Time   | ";
					}else{
						schedule[row][col] = days[col-1] + "   |   ";
					}
				}else{
					schedule[row][0]= Integer.toString(row + 6) + ":00";
					schedule[row][col] ="       *      ";
				}
				
			}
		}
		for(int x =0; x< schedule.length; x++){
			for(int y =0; y< schedule[x].length; y++){
				System.out.print(schedule[x][y]);
			}
			System.out.println();
		}
	};
	public void makeCurrent() {}

	public Schedule() {}

	public Schedule(ArrayList<Course> courseList, String semester) {
		this.courseList = courseList;
		this.semester = semester;
	}

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
