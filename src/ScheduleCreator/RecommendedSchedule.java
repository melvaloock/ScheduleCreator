package ScheduleCreator;

import java.sql.SQLException;
import java.util.*;

public class RecommendedSchedule extends Schedule {

	private String major;
	private int year;
	private String semester = "Fall 2022";

	public RecommendedSchedule(ArrayList<Course> courseList, String semester, String major, int year) {
		super(courseList, semester);
		this.major = major;
		this.year = year;
	}

	public RecommendedSchedule(String major, int year, Database db) {
		super();
		try {
			super.courseList = db.getRecommendedCourses(major, year, semester);
		} catch (SQLException e) {
			System.out.print(e.getMessage());
		}
		this.major = major;
		this.year = year;
	}

	public CurrentSchedule makeCurrentSchedule() {
		CurrentSchedule cs = new CurrentSchedule(courseList, this.semester);

		return cs;
	}

	public RecommendedSchedule(String statusSheet) {

	}


}
