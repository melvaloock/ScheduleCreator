package sleeplessdevelopers.schedulecreator;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

// import java.time.LocalTime;

public class Database { 
    private Connection conn;

    /**
     * Constructor for the Database class.
     * @param username username of database user
     * @param password password of database user
     * @param schema db schema name
     */
    public Database(String username, String password, String schema) { 
        try {
            Properties info = new Properties();
            info.put("user", username);
            info.put("password", password);
            // conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + schema, info);
            conn = DriverManager.getConnection("jdbc:mysql://34.121.27.151/" + schema, info);
            // System.out.println("Connection successful!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
    }

    /**
     * Adds a course to the database.
     * @param courseID 6-digit course ID
     * @param courseCode course code (e.g., COMP 141)
     * @param courseName course name (e.g., COMP PROGRAMMING I )
     * @param startTime start time of course (e.g, 8:00 AM)
     * @param endTime end time of course (e.g, 8:50 AM)
     * @param weekday abbreviated weekday (e.g., MWF)
     * @param enrollment enrollment capacity
     * @param capacity maximum capacity
     */
    public void addCourse(int courseID, String courseCode, String courseName, String startTime, String endTime, String weekday, int enrollment, int capacity) {
        try {
            PreparedStatement pstmtCheck = conn.prepareStatement("SELECT * FROM course WHERE CourseID = ?");
            pstmtCheck.setInt(1, courseID);
            ResultSet rstCheck = pstmtCheck.executeQuery();

            if (rstCheck.next()) { 
                throw new SQLException("That course already exists.");
            }

            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO course values(?, ?, ?, ?, ?, ?, ?, ?)");
            insertStmt.setInt(1, courseID);
            insertStmt.setString(2, courseCode);
            insertStmt.setString(3, courseName);
            insertStmt.setString(4, weekday);
            insertStmt.setString(5, startTime);
            insertStmt.setString(6, endTime);
            insertStmt.setInt(7, enrollment);
            insertStmt.setInt(8, capacity);
            int rows = insertStmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Insertion successful!");
            }

            pstmtCheck.close();
            rstCheck.close(); 
            insertStmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Parses the csv database file and adds the courses to the database.
     * @param csvFilename name of csv file
     */
    public void processCourses(String csvFilename) {
        try {
            Scanner csvScanner = new Scanner(new File(csvFilename));
            String[] vars;
            int courseID = 100000;
            while (csvScanner.hasNextLine()) {
                vars = csvScanner.nextLine().split(";");
                addCourse(courseID, vars[0], vars[2], vars[3], vars[4], vars[5], Integer.parseInt(vars[8]), Integer.parseInt(vars[9]));
                courseID++;
            } 
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * add account with the given username and password.
     * throws SQLException if there is already an account with that email.
     * major and year are set to null.
     * @param userEmail String of the user's email
     * @param userPassword String of the user's password
     * @throws SQLException throws exception if email already in user
     * @throws PasswordStorage.CannotPerformOperationException throws exception if issue with password hashing
     */
    public void addAccount(String userEmail, String userPassword) throws SQLException, PasswordStorage.CannotPerformOperationException {
            PreparedStatement pstmtCheck = conn.prepareStatement("SELECT * FROM account WHERE UserEmail = ?");
            pstmtCheck.setString(1, userEmail);
            ResultSet rstCheck = pstmtCheck.executeQuery();

            if (rstCheck.next()) { 
                throw new SQLException("An account already exists under that email.");
            }

            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO account values(?, ?, null, null)");
            insertStmt.setString(1, userEmail);


            insertStmt.setString(2, PasswordStorage.createHash(userPassword));
            int rows = insertStmt.executeUpdate();

            if (rows <= 0) {
                throw new SQLException("ERROR: Account creation failed. Please try again.");
            }

            pstmtCheck.close();
            rstCheck.close(); 
            insertStmt.close();
    }

    /**
     * add account with the given username and password.
     * throws SQLException if there is already an account with that email.
     * @param userEmail String of user's email
     * @param userPassword String of user's password
     * @param major String of user's major
     * @param year int of user's year
     * @throws SQLException throws exception if email already in user
     * @throws PasswordStorage.CannotPerformOperationException throws exception if issue with password hashing
     */
    public void addAccount(String userEmail, String userPassword, String major, int year) throws SQLException, PasswordStorage.CannotPerformOperationException {
        PreparedStatement pstmtCheck = conn.prepareStatement("SELECT * FROM account WHERE UserEmail = ?");
        pstmtCheck.setString(1, userEmail);
        ResultSet rstCheck = pstmtCheck.executeQuery();

        if (rstCheck.next()) {
            throw new SQLException("An account already exists under that email.");
        }

        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO account values(?, ?, ?, ?)");
        insertStmt.setString(1, userEmail);
        insertStmt.setString(2, PasswordStorage.createHash(userPassword));
        insertStmt.setString(3, major);
        insertStmt.setInt(4, year);

        int rows = insertStmt.executeUpdate();

        if (rows <= 0) {
            throw new SQLException("ERROR: Account creation failed. Please try again.");
        }

        pstmtCheck.close();
        rstCheck.close();
        insertStmt.close();
    }

    /**
     * Adds an empty schedule to the database
     * @param scheduleID semester and year of schedule (e.g., "Fall 2022")
     * @param isCurrent true if the schedule is the current schedule, false otherwise
     * @param userEmail String of the user's email
     * @throws SQLException
     */
    public void addSchedule(String scheduleID, boolean isCurrent, String userEmail) throws SQLException {
        PreparedStatement pstmtCheck = conn.prepareStatement("SELECT * FROM schedule WHERE UserEmail = ? AND ScheduleID = ?");
        pstmtCheck.setString(1, userEmail);
        pstmtCheck.setString(2, scheduleID);
        ResultSet rstCheck = pstmtCheck.executeQuery();

        if (rstCheck.next()) { 
            throw new SQLException("A schedule already exists during that semester.");
        }

        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO schedule values(?, ?, ?)");
        insertStmt.setString(1, scheduleID);
        insertStmt.setInt(2, (isCurrent) ? 1 : 0);
        insertStmt.setString(3, userEmail);
        int rows = insertStmt.executeUpdate();

        if (rows <= 0) {
            throw new SQLException("ERROR: Schedule creation failed. Please try again.");
        }

        pstmtCheck.close();
        rstCheck.close(); 
        insertStmt.close();
    }

    /**
     * Adds a course reference to the specified schedule
     * @param courseID 6-digit course ID
     * @param scheduleID semester and year of schedule (e.g., "Fall 2022")
     * @param userEmail String of the user's email
     * @throws SQLException
     */
    public void addCourseRef(int courseID, String scheduleID, String userEmail) throws SQLException {
        PreparedStatement pstmtCheck = conn.prepareStatement("SELECT * FROM courseReference WHERE CourseID = ? AND UserEmail = ?");
        pstmtCheck.setInt(1, courseID);
        pstmtCheck.setString(2, userEmail);
        ResultSet rstCheck = pstmtCheck.executeQuery();

        if (rstCheck.next()) { 
            throw new SQLException("This course already exists in your " + scheduleID + " semester");
        }

        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO courseReference values(?, ?, ?)");
        insertStmt.setInt(1, courseID);
        insertStmt.setString(2, scheduleID);
        insertStmt.setString(3, userEmail);
        int rows = insertStmt.executeUpdate();

        if (rows <= 0) {
            throw new SQLException("ERROR: Course addition failed. Please try again.");
        }

        pstmtCheck.close();
        rstCheck.close(); 
        insertStmt.close();
    }

    // TODO: update with courseID
    /**
     * Deletes a course reference from the specified schedule
     * @param courseCode 6-digit course code
     * @param courseName course name (e.g., COMP PROGRAMMING I)
     * @param scheduleID semester and year of schedule (e.g., "Fall 2022")
     * @param userEmail String of the user's email
     * @throws SQLException
     */
    public void deleteCourseRef(String courseCode, String courseName, String scheduleID, String userEmail) throws SQLException {
        PreparedStatement pstmtCheck = conn
            .prepareStatement("SELECT * FROM courseReference WHERE CourseCode = ? AND CourseName = ? AND ScheduleID = ? AND UserEmail = ?");
        pstmtCheck.setString(1, courseCode);
        pstmtCheck.setString(2, courseName);
        pstmtCheck.setString(3, scheduleID);
        pstmtCheck.setString(4, userEmail);
        ResultSet rstCheck = pstmtCheck.executeQuery();

        if (rstCheck.next()) {
            PreparedStatement deleteStmt = conn
                .prepareStatement("DELETE FROM courseReference WHERE CourseCode = ? AND CourseName = ? AND ScheduleID = ? AND UserEmail = ?");
            deleteStmt.setString(1, courseCode);
            deleteStmt.setString(2, courseName);
            deleteStmt.setString(3, scheduleID);
            deleteStmt.setString(4, userEmail);
            int rows = deleteStmt.executeUpdate();
            deleteStmt.close();

            if (rows > 0) {
                throw new SQLException("ERROR: Course deletion failed. Please try again.");
            }
        } else {
            throw new SQLException("No course found given those parameters.");
        }

        pstmtCheck.close();
        rstCheck.close(); 
    }

    /**
     * Deletes all course references from the specified schedule
     * @param scheduleID semester and year of schedule (e.g., "Fall 2022")
     * @param userEmail String of the user's email
     * @throws SQLException
     */
    public void deleteAllCourseRefs(String scheduleID, String userEmail) throws SQLException {
        PreparedStatement pstmtCheck = conn
            .prepareStatement("SELECT * FROM courseReference WHERE ScheduleID = ? AND UserEmail = ?");
        pstmtCheck.setString(1, scheduleID);
        pstmtCheck.setString(2, userEmail);
        ResultSet rstCheck = pstmtCheck.executeQuery();

        if (rstCheck.next()) {
            PreparedStatement deleteStmt = conn
                .prepareStatement("DELETE FROM courseReference WHERE ScheduleID = ? AND UserEmail = ?");
            deleteStmt.setString(1, scheduleID);
            deleteStmt.setString(2, userEmail);
            int rows = deleteStmt.executeUpdate();
            deleteStmt.close();

            // TODO: fix this pls -_-
            if (rows == 0) {
                throw new SQLException("ERROR: Course deletion failed. Please try again.");
            }
        }
        pstmtCheck.close();
        rstCheck.close(); 
    }

    /**
     * Deletes a schedule from the database.
     * Note: This will also delete all course references associated with the schedule.
     * @param scheduleID semester and year of schedule (e.g., "Fall 2022")
     * @param userEmail String of the user's email
     * @throws SQLException
     */
    public void deleteSchedule(String scheduleID, String userEmail) throws SQLException {
        PreparedStatement pstmtCheck = conn
            .prepareStatement("SELECT * FROM schedule WHERE ScheduleID = ? AND UserEmail = ?");
        pstmtCheck.setString(1, scheduleID);
        pstmtCheck.setString(2, userEmail);
        ResultSet rstCheck = pstmtCheck.executeQuery();

        if (rstCheck.next()) {
            deleteAllCourseRefs(scheduleID, userEmail);
            PreparedStatement deleteStmt = conn
                .prepareStatement("DELETE FROM schedule WHERE ScheduleID = ? AND UserEmail = ?");
            deleteStmt.setString(1, scheduleID);
            deleteStmt.setString(2, userEmail);
            int rows = deleteStmt.executeUpdate();
            deleteStmt.close();

            if (rows > 0) {
                throw new SQLException("ERROR: Schedule deletion failed. Please try again.");
            }
        } else {
            throw new SQLException("No schedule found given those parameters.");
        }

        pstmtCheck.close();
        rstCheck.close(); 
    }

    /**
     * Checks the login credentials with the database
     * @param userEmail String of the user's email
     * @param userPassword String of the user's password
     * @throws SQLException
     * @throws PasswordStorage.InvalidHashException
     * @throws PasswordStorage.CannotPerformOperationException
     */
    public void checkLogin(String userEmail, String userPassword) throws SQLException, PasswordStorage.InvalidHashException, PasswordStorage.CannotPerformOperationException {
        PreparedStatement userCheck = conn.prepareStatement("SELECT * FROM account WHERE UserEmail = ?");
        userCheck.setString(1, userEmail);
        ResultSet rstCheck = userCheck.executeQuery();

        // if an account exists with userEmail, continue
        if (rstCheck.next()) {
            String dbPass = rstCheck.getString("UserPassword");
            // check if the passwords don't match
            if (!PasswordStorage.verifyPassword(userPassword, dbPass)){
                // wrong password
                throw new SQLException("Incorrect email or password.");
            }
        } else {
            // account with this email does not exist
            throw new SQLException("Incorrect email or password.");
        }

        userCheck.close();
        rstCheck.close();
    }

    /**
     * after a user login is verified, gets the account's saved information
     * @param userEmail
     * @return
     */
    public Account getAccount(String userEmail, String userPassword) throws SQLException, PasswordStorage.InvalidHashException, PasswordStorage.CannotPerformOperationException {
        // verify user login
        checkLogin(userEmail, userPassword);

        PreparedStatement pstmtCheck = conn.prepareStatement("SELECT * FROM schedule WHERE UserEmail = ?");
        pstmtCheck.setString(1, userEmail);
        ResultSet rstCheck = pstmtCheck.executeQuery();

        // get schedule if it exists
        CurrentSchedule currentSchedule = new CurrentSchedule();
        if (rstCheck.next()) {
            currentSchedule = getCurrentSchedule(userEmail);
        }

        // get the year and major; may return nothing
        int year = getYear(userEmail);
        String major = getMajor(userEmail);

        // get password hash
        PreparedStatement selectStmt = conn.prepareStatement("SELECT * FROM account WHERE UserEmail = ?");
        selectStmt.setString(1, userEmail);
        ResultSet rst = selectStmt.executeQuery();

        String passwordHash = "";
        if (rst.next()) {
            passwordHash = rst.getString("UserPassword");
        }

        pstmtCheck.close();
        rstCheck.close();
        return new Account(userEmail, passwordHash, currentSchedule, major, year);
    }

    // TODO: needs error checking somewhere
    /**
     * Gets the graduation year of the user
     * @param userEmail String of the user's email
     * @return int of the user's graduation year
     * @throws SQLException
     */
    public int getYear(String userEmail) throws SQLException {
        PreparedStatement pstmtCheck = conn.prepareStatement("SELECT * FROM account WHERE UserEmail = ?");
        pstmtCheck.setString(1, userEmail);
        ResultSet rstCheck = pstmtCheck.executeQuery();

        int year = 0;

        if (rstCheck.next()) {
            year = rstCheck.getInt("GradYear");
        }

        pstmtCheck.close();
        rstCheck.close();
        return year;
    }

    // TODO: needs error checking somewhere
    /**
     * Gets the major of the user
     * @param userEmail String of the user's email
     * @return String of the user's major
     * @throws SQLException
     */
    public String getMajor(String userEmail) throws SQLException {
        PreparedStatement pstmtCheck = conn.prepareStatement("SELECT * FROM account WHERE UserEmail = ?");
        pstmtCheck.setString(1, userEmail);
        ResultSet rstCheck = pstmtCheck.executeQuery();

        String major = "";

        if (rstCheck.next()) {
            major = rstCheck.getString("Major");
        }

        pstmtCheck.close();
        rstCheck.close();
        return major;
    }

    /**
     * Gets the current schedule of the user
     * @param userEmail String of the user's email
     * @return CurrentSchedule object of the user's current schedule
     * @throws SQLException
     */
    public CurrentSchedule getCurrentSchedule(String userEmail) throws SQLException {
        PreparedStatement pstmtCheck = conn.prepareStatement("SELECT * FROM schedule WHERE UserEmail = ?");
        pstmtCheck.setString(1, userEmail);
        ResultSet rstCheck = pstmtCheck.executeQuery();

        if (rstCheck.next()) {
            PreparedStatement selectStmt = conn
                .prepareStatement("SELECT * FROM schedule WHERE UserEmail = ? AND IsCurrent = 1");
            selectStmt.setString(1, userEmail);
            ResultSet rstSelect = selectStmt.executeQuery();

            rstSelect.next();
            String scheduleID = rstSelect.getString("ScheduleID");
            ArrayList<Course> courses = getCoursesFromRefs(userEmail, scheduleID);

            return new CurrentSchedule(courses, scheduleID);
        } else {
            // returns null if no current schedule
            return null;
        }
    }

    /**
     * @param searchCode
     * @return
     * @throws SQLException
     */
    public ArrayList<Course> searchByCode(String searchCode) throws SQLException {
        PreparedStatement pstmtCheck = conn
                .prepareStatement("SELECT * FROM course WHERE CourseCode LIKE ?");
        pstmtCheck.setString(1, "%" + searchCode + "%");
        ResultSet rstSearch = pstmtCheck.executeQuery();

        ArrayList<Course> searchResults = new ArrayList<Course>();
        while (rstSearch.next()) {
            searchResults.add(createCourse(rstSearch));
        }

        return searchResults;
    }

    /**
     * @param searchKeyword
     * @return ArrayList of course objects that match the search keyword
     * @throws SQLException
     */
    public ArrayList<Course> searchByKeyword(String searchKeyword) throws SQLException {
        PreparedStatement pstmtCheck = conn
                .prepareStatement("SELECT * FROM course WHERE CourseName LIKE ?");
        pstmtCheck.setString(1, "%" + searchKeyword + "%");
        ResultSet rstSearch = pstmtCheck.executeQuery();

        ArrayList<Course> searchResults = new ArrayList<Course>();
        while (rstSearch.next()) {
            searchResults.add(createCourse(rstSearch));
        }

        return searchResults;
    }

    /**
     * Gets an ArrayList of course objects from the user's course references
     * @param userEmail String of the user's email 
     * @param scheduleID semester and year of the schedule
     * @return ArrayList of course objects
     * @throws SQLException
     */
    public ArrayList<Course> getCoursesFromRefs(String userEmail, String scheduleID) throws SQLException {
        PreparedStatement pstmtCheck = conn
            .prepareStatement("SELECT * FROM courseReference WHERE UserEmail = ? AND ScheduleID = ?");
        pstmtCheck.setString(1, userEmail);
        pstmtCheck.setString(2, scheduleID);
        ResultSet rstCheck = pstmtCheck.executeQuery();

        ArrayList<Course> courses = new ArrayList<Course>();
        while (rstCheck.next()) {
            PreparedStatement selectStmt = conn
                .prepareStatement("SELECT * FROM course WHERE CourseID = ?");
            selectStmt.setString(1, rstCheck.getString("CourseID"));
            ResultSet rstSelect = selectStmt.executeQuery();

            while (rstSelect.next()) {
                courses.add(createCourse(rstSelect));
            }
        }
        return courses;
    }

    // TODO: add proper error checking
    /**
     * Gets an ArrayList of course IDs from an ArrayList of course objects
     * @param courses ArrayList of course objects
     * @return ArrayList of course IDs
     * @throws SQLException
     */
    public ArrayList<Integer> getCourseIDs(ArrayList<Course> courses) throws SQLException {
        ArrayList<Integer> courseIDs = new ArrayList<Integer>();

        for (Course c : courses) {
            PreparedStatement pstmtCheck = conn
                    .prepareStatement("SELECT * FROM course WHERE CourseCode = ? AND CourseName = ? AND StartTime = ?");
            pstmtCheck.setString(1, c.getCode());
            pstmtCheck.setString(2, c.getTitle());
            pstmtCheck.setString(3, c.getStartTime());
            ResultSet rstCheck = pstmtCheck.executeQuery();

            if (rstCheck.next()) {
                courseIDs.add(rstCheck.getInt("CourseID"));
            }
        }
        return courseIDs;
    }

    /**
     * Gets an ArrayList of courses from the recommended courses of a recommended schedule
     * @param major String of the user's major
     * @param year int of the user's graduation year
     * @param semester semester and year of the schedule
     * @return ArrayList of course objects
     * @throws SQLException
     */
    public ArrayList<Course> getRecommendedCourses(String major, int year, String semester) throws SQLException {
        PreparedStatement pstmtCheck = conn
                .prepareStatement("SELECT * FROM recommendedCourse WHERE Major = ? AND GradYear = ? and Semester = ?");
        pstmtCheck.setString(1, major);
        pstmtCheck.setInt(2, year);
        pstmtCheck.setString(3, semester);
        ResultSet rstCheck = pstmtCheck.executeQuery();

        ArrayList<Course> courses = new ArrayList<Course>();
        while (rstCheck.next()) {
            PreparedStatement selectStmt = conn
                    .prepareStatement("SELECT * FROM course WHERE CourseCode = ? AND CourseName = ?");
            selectStmt.setString(1, rstCheck.getString("CourseCode"));
            selectStmt.setString(2, rstCheck.getString("CourseName"));
            ResultSet rstSelect = selectStmt.executeQuery();

            while (rstSelect.next()) {
                courses.add(createCourse(rstSelect));
            }
        }

        return courses;
    }

    // this COULD throw an eror, so make sure it's handled
    /**
     * Creates a course object from a ResultSet
     * @param rst ResultSet of course(s)
     * @return Course object
     * @throws SQLException
     */
    public Course createCourse(ResultSet rst) throws SQLException {
        ArrayList<Day> days = new ArrayList<Day>();
        String daysString = rst.getString("Weekday");
        for (char c : daysString.toCharArray()) {
            days.add(Day.Day(c));
        }
        String code = rst.getString("CourseCode");
        return new Course(rst.getInt("CourseID"), rst.getString("CourseCode"), rst.getString("CourseName"),
            rst.getString("StartTime"), rst.getString("EndTime"), code.charAt(code.length() - 1),
            days);
    }
    
    /**
     * Updates the course references for a schedule
     * Note: deletes and replaces all course references for the specified schedule
     * @param scheduleID semester and year of the schedule
     * @param userEmail String of the user's email
     * @param courses ArrayList of course objects
     */
    public void updateCourseRefs(String scheduleID, String userEmail, ArrayList<Course> courses) {
        try {
            if (courses.isEmpty()) {
                deleteAllCourseRefs(scheduleID, userEmail);
                return;
            }

            deleteAllCourseRefs(scheduleID, userEmail);

            ArrayList<Integer> courseIDs = getCourseIDs(courses);
            for (int courseID : courseIDs) {
                addCourseRef(courseID, scheduleID, userEmail);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //Implement a method to get the reference number of each class -Tyler
    public int getReferenceNum(Course c) throws SQLException {
        PreparedStatement selectStmt = conn.prepareStatement("SELECT * FROM course WHERE CourseCode = ? and CourseName = ?");
        selectStmt.setString(1, c.getCode());
        selectStmt.setString(2, c.getTitle());
        ResultSet resultSet = selectStmt.executeQuery();
        return resultSet.getInt("CourseID");
    }

    // TODO: Kevin -> sprint 2
    public void updateYear(){}
    public void updateMajor(){}

    /**
     * updates the oldEmail everywhere in the database to the new email.
     * the user should be asked to enter their login information before changing their email
     * @param oldEmail
     * @param newEmail
     * @throws SQLException
     */
    public void updateEmail(String oldEmail, String newEmail) throws SQLException {
        PreparedStatement stmnt;

        /*
         * turn off foreign key checks.
         * since we are updating the foreign key in multiple tables, the checks for the
         * constraints need to be turned off.
         */
        stmnt = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 0");
        stmnt.executeUpdate();

        // change email in each table to the new email
        stmnt = conn.prepareStatement("UPDATE account SET UserEmail = ? where UserEmail = ?");
        stmnt.setString(1, newEmail);
        stmnt.setString(2, oldEmail);
        stmnt.executeUpdate();

        stmnt = conn.prepareStatement("UPDATE schedule SET UserEmail = ? where UserEmail = ?");
        stmnt.setString(1, newEmail);
        stmnt.setString(2, oldEmail);
        stmnt.executeUpdate();

        stmnt = conn.prepareStatement("UPDATE courseReference SET UserEmail = ? where UserEmail = ?");
        stmnt.setString(1, newEmail);
        stmnt.setString(2, oldEmail);
        stmnt.executeUpdate();


        // turn checks back on
        stmnt = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 1");
        stmnt.executeUpdate();

        stmnt.close();
    }

    /**
     * updates the user's password to the new password
     * the user should be asked to enter their login information before changing their email
     * @param email
     * @param newPassword
     * @throws SQLException
     * @throws PasswordStorage.CannotPerformOperationException
     */
    public void updatePassword(String email, String newPassword) throws SQLException, PasswordStorage.CannotPerformOperationException {
        PreparedStatement stmnt = conn.prepareStatement("UPDATE account SET UserPassword = ? WHERE UserEmail = ?");
        stmnt.setString(1, PasswordStorage.createHash(newPassword));
        stmnt.setString(2, email);
        stmnt.executeUpdate();

        stmnt.close();
    }

    /**
	 * This method implements the functionality necessary to exit the application:
	 * this should allow the user to cleanly exit the application properly. This
	 * should close the connection and any prepared statements.
	 */
	public void endDatabaseConnection() {
		try {
			if (conn != null) {
				conn.close();
				System.out.println("Disconnected!");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

    public static void main(String args[]) {
        // Database db = new Database("root", "password", "sys");
        Database db = new Database("root", "EnuzPkHDO29J6gCH", "schedule_creator_db");
        //db.processCourses("CourseDB_WithFictionalCapacities.csv");

        try {
            db.addAccount("test", "testPass");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}