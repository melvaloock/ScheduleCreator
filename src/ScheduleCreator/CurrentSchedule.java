package ScheduleCreator;

import java.util.ArrayList;

public class CurrentSchedule extends Schedule {

	public CurrentSchedule(ArrayList<Course> courses) {
		super.setCourseList(courses);
	}

	public CurrentSchedule(Schedule schedule) {
		super();
		isCurrent = true;
	}

	/**
	 * removes all courses from schedule
	 */
	public void clearSchedule() {
		courseList = new ArrayList<Course>();
	}

	/**
	 * adds Course c to list of courses
	 * @param c
	 */
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

		for (int i = 0; i < courseList.size(); i++){
			if (courseList.get(i).getCode().equals(remCode)){
				courseList.remove(i);
				didRem = true;
				i--;
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

		// parse Course c days and time
		double cStart, cEnd, tempStart, tempEnd;
		char[] cDays, tempDays;

		cDays = c.getDays().toCharArray();

		String[] times = c.getStartTime().split(":");
		cStart = Integer.parseInt(times[0]) + Integer.parseInt(times[1])/60.0;

		times = c.getEndTime().split(":");
		cEnd = Integer.parseInt(times[0]) + Integer.parseInt(times[1])/60.0;

		// check if each course has a conflict
		for (Course current: courseList) {
			// check current course to see if it's NULL
			if (current.getStartTime() == null){
				continue;
			}

			// check if they have overlapping days
			tempDays = current.getDays().toCharArray();
			boolean hasOverlapDay = false;
			for (char temp: tempDays) {
				for (char orgDay: cDays){
					if (temp == orgDay){
						hasOverlapDay = true;
						break;
					}
				}
			}

			// if no overlapping days, then no conflict
			if (!hasOverlapDay) {
				continue;
			}


			// get current Course times
			times = current.getStartTime().split(":");
			tempStart = Integer.parseInt(times[0]) + Integer.parseInt(times[1])/60.0;
			times = current.getEndTime().split(":");
			tempEnd = Integer.parseInt(times[0]) + Integer.parseInt(times[1])/60.0;


			// check start time
			if (cStart >= tempStart && cStart <= tempEnd){
				return true;
			}

			// check end time
			if (cEnd >= tempStart && cEnd <= tempEnd){
				return true;
			}

		}

		return false;
	}

}
