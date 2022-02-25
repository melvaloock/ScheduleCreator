package ScheduleCreator;

import java.util.*;
import java.io.*;

public class Schedule {

	private ArrayList<Course> courseList = new ArrayList<Course>();
	private String semester;
	private String outFile;
	private FileWriter export;
	private boolean isCurrent;
	private static String defaultSemester = "Fall 2023";

	public void exportSchedule() {}
	public void displaySchedule() {};
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
