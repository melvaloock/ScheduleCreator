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
	 * Removes course of the given course code from schedule.
	 * Returns false if the given course is not in the schedule.
	 * @param remCode
	 * @return boolean
	 */
	public boolean removeCourse(String remCode) {
		// removes multiples because some courses have 2 course objects for different times
		remCode = remCode.toUpperCase();
		boolean didRem = false;

		for (Course c: courseList){
			if (c.getCode().equals(remCode)){
				courseList.remove(c);
				didRem = true;
			}
		}

		return didRem;
	}

	/**
	 * returns true if Course c has a time conflict with any of the courses in the current schedule
	 * @param c
	 * @return boolean
	 */
	public boolean conflictsWith(Course c) {
		// if c is NULL, then it will have no conflicts
		if (c.getStartTime() == null){
			return false;
		}

		// parse Course c time
		String[] times = c.getStartTime().split(":");
		double cStart = Integer.valueOf(times[0]) + Integer.valueOf(times[1])/60;

		// check if each course has a conflict
		for (Course current: courseList) {
			// check current course to see if it's NULL
			if (current.getStartTime() == null){
				continue;
			}

			// check startTime

		}

		return false;
	}

}
