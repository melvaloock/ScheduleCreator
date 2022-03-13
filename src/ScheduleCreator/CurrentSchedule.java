package ScheduleCreator;

import java.util.ArrayList;

public class CurrentSchedule extends Schedule {

	public CurrentSchedule(ArrayList<Course> courses) {
		super.setCourseList(courses);
	}

	public CurrentSchedule(Schedule schedule) {
		super();
	}

	public void clearSchedule() {
		courseList = new ArrayList<Course>();
	}

	public void addCourse(Course c) {
		courseList.add(c);
	}

	/**
	 * Removes course c from courseList
	 * @param c
	 */
	public void removeCourse(Course c) {
		courseList.remove(c);
	}

	public boolean conflictsWith(Course c) {
		return false;
	}

}
