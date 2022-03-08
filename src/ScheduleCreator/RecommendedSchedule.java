package ScheduleCreator.ScheduleCreator;

import java.util.*;

public class RecommendedSchedule extends Schedule {

	private String major;
	private String year;

	public RecommendedSchedule(ArrayList<Course> courseList, String semester, String major, String year) {
		super(courseList, semester);
		this.major = major;
		this.year = year;
	}

	public RecommendedSchedule(String statusSheet) {

	}


}
