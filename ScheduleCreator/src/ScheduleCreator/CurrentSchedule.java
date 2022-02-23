package ScheduleCreator;

import java.util.ArrayList;

public class CurrentSchedule extends Schedule {

	public CurrentSchedule(ArrayList<Course> courses) {
		super.setCourseList(courses);
	}

	public CurrentSchedule(Schedule schedule) {
		super();
	}

	public void clearSchedule() {}
	public void addCourse(Course c) {}
	public void removeCourse(Course c) {}
	public boolean conflictsWith(Course c) {return false;}
}
