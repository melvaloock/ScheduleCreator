package sleeplessdevelopers.schedulecreator;

import java.util.ArrayList;

public class CurrentSchedule extends Schedule {

	public CurrentSchedule(Schedule s) {
		this.courseList = s.courseList;
		this.semester = s.semester;
		this.isCurrent = true;
	}

	public CurrentSchedule(ArrayList<Course> courses) {
		super.setCourseList(courses);
	}

	public CurrentSchedule(ArrayList<Course> courses, String semester) {
		super(courses, semester);
	}

	public CurrentSchedule() {
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
	 * @param c Course to add
	 */
	public void addCourse(Course c) {
		courseList.add(c);
	}

	/**
	 * Removes given course from schedule.
	 * @param remCourse code of Course to remove
	 */
	public void removeCourse(Course remCourse) {
		String remCode = remCourse.getCode();
		/*
		 * some courses have 2 Course objects but are just one course because they have different
		 * times on different days (like 10-10:50 on MWF and 12-1:15 on T).
		 * This loops through and checks if there are multiple courses with the same code.
		 * If there are, then it is the same course and they all need removed.
		 */
		for (int i = 0; i < courseList.size(); i++){
			if (courseList.get(i).getCode().equals(remCode)){
				courseList.remove(i);
				i--;
			}
		}
	}

	/**
	 * returns true if Course c has a time conflict with any of the courses in the current schedule
	 * @param c Course to check against Courses in schedule
	 * @return boolean
	 */
	public ArrayList<Course> conflictsWith(Course c) {
		ArrayList<Course> conflicts = new ArrayList<>();
		// if start-time is NULL, then it will have no conflicts
		if (c.getStartTime() == null){
			return conflicts;
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
				conflicts.add(current);
			}

			// check end time
			if (cEnd >= tempStart && cEnd <= tempEnd){
				conflicts.add(current);
			}

		}

		return conflicts;
	}

}
