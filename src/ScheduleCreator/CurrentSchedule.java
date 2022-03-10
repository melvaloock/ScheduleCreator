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
	 * Removes course c from courseList. If c is not in courseList, returns false.
	 * @param c
	 * @return true if course successfully removed, false otherwise
	 */
	public boolean removeCourse(Course c) {
		return courseList.remove(c);
	}

	public boolean conflictsWith(Course c) {
		return false;
	}

}
