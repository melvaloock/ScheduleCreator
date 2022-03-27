package ScheduleCreator;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

import com.mysql.cj.x.protobuf.MysqlxPrepare.Prepare;

// import java.time.LocalTime;

public class Database { 
    private Connection conn;

    public Database(String username, String password, String schema) { 
        try {
            Properties info = new Properties();
            info.put("user", username);
            info.put("password", password);
            // conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + schema, info);
            conn = DriverManager.getConnection("jdbc:mysql://34.121.27.151/" + schema, info);
            System.out.println("Connection successful!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
    }

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

    // this is only used in db population
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

    // TODO: this doesn't actually save a schedule it only saves a scheduleID
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
        insertStmt.setString(3, userEmail);;
        int rows = insertStmt.executeUpdate();

        // TODO: put addCourseRef() somewhere here

        if (rows <= 0) {
            throw new SQLException("ERROR: Schedule creation failed. Please try again.");
        }

        pstmtCheck.close();
        rstCheck.close(); 
        insertStmt.close();
    }

    public void addCourseRef(String courseCode, String courseName, String scheduleID, String userEmail) throws SQLException {
        PreparedStatement pstmtCheck = conn.prepareStatement("SELECT * FROM courseReference WHERE CourseCode = ? AND CourseName = ? AND UserEmail = ?");
        pstmtCheck.setString(1, courseCode);
        pstmtCheck.setString(2, courseName);
        pstmtCheck.setString(3, scheduleID);
        ResultSet rstCheck = pstmtCheck.executeQuery();

        if (rstCheck.next()) { 
            throw new SQLException("This course already exists in your " + scheduleID + " semester");
        }

        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO courseReference values(?, ?, ?, ?)");
        insertStmt.setString(1, courseCode);
        insertStmt.setString(2, courseName);
        insertStmt.setString(3, scheduleID);
        insertStmt.setString(4, userEmail);
        int rows = insertStmt.executeUpdate();

        if (rows <= 0) {
            throw new SQLException("ERROR: Course addition failed. Please try again.");
        }

        pstmtCheck.close();
        rstCheck.close(); 
        insertStmt.close();
    }

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

    public void deleteAllCourses(String scheduleID, String userEmail) throws SQLException {
        PreparedStatement pstmtCheck = conn
            .prepareStatement("SELECT * FROM courseReference WHERE ScheduleID = ? AND UserEmail = ?");
        pstmtCheck.setString(1, scheduleID);
        pstmtCheck.setString(2, userEmail);
        ResultSet rstCheck = pstmtCheck.executeQuery();

        if (rstCheck.next()) {
            PreparedStatement deleteStmt = conn
                .prepareStatement("DELETE * FROM courseReference WHERE ScheduleID = ? AND UserEmail = ?");
            deleteStmt.setString(1, scheduleID);
            deleteStmt.setString(2, userEmail);
            int rows = deleteStmt.executeUpdate();
            deleteStmt.close();

            if (rows > 0) {
                throw new SQLException("ERROR: Course deletion failed. Please try again.");
            }
        } else {
            throw new SQLException("No courses found for that schedule.");
        }

        pstmtCheck.close();
        rstCheck.close(); 
    }

    public void deleteSchedule(String scheduleID, String userEmail) throws SQLException {
        PreparedStatement pstmtCheck = conn
            .prepareStatement("SELECT * FROM schedule WHERE ScheduleID = ? AND UserEmail = ?");
        pstmtCheck.setString(1, scheduleID);
        pstmtCheck.setString(2, userEmail);
        ResultSet rstCheck = pstmtCheck.executeQuery();

        if (rstCheck.next()) {
            deleteAllCourses(scheduleID, userEmail);
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

    public Account checkLogin(String userEmail, String userPassword) throws SQLException, PasswordStorage.InvalidHashException, PasswordStorage.CannotPerformOperationException {
        PreparedStatement userCheck = conn.prepareStatement("SELECT * FROM account WHERE UserEmail = ?");
        userCheck.setString(1, userEmail);
        ResultSet rstCheck = userCheck.executeQuery();

        Account ret;

        // if an account exists with userEmail, continue
        if (rstCheck.next()) {
            String dbPass = rstCheck.getString("UserPassword");
            // check if the passwords match
            if (PasswordStorage.verifyPassword(userPassword, dbPass)){
                //ret = getAccount(userEmail);
                ret = new Account("", "", new CurrentSchedule(), "", 0);
            } else {
                // wrong password
                throw new SQLException("Incorrect email or password.");
            }
        } else {
            // account with this email does not exist
            throw new SQLException("Incorrect email or password.");
        }

        userCheck.close();
        rstCheck.close();
        return ret;
    }

    /**
     * after a user login is verified, gets the account's saved information
     * @param userEmail
     * @return
     */
    public Account getAccount(String userEmail) throws SQLException {
        PreparedStatement pstmtCheck = conn.prepareStatement("SELECT * FROM schedule WHERE UserEmail = ?");
        pstmtCheck.setString(1, userEmail);
        ResultSet rstCheck = pstmtCheck.executeQuery();

        // get schedule if it exists
        Schedule currentSchedule = new Schedule();
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

        String passwordHash = rst.getString("UserPassword");

        pstmtCheck.close();
        rstCheck.close();
        return new Account(userEmail, passwordHash, currentSchedule, major, year);
    }

    // needs error checking somewhere
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

    // needs error checking somewhere
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

    public Schedule getCurrentSchedule(String userEmail) throws SQLException {
        PreparedStatement pstmtCheck = conn.prepareStatement("SELECT * FROM schedule WHERE UserEmail = ?");
        pstmtCheck.setString(1, userEmail);
        ResultSet rstCheck = pstmtCheck.executeQuery();

        if (rstCheck.next()) {
            PreparedStatement selectStmt = conn
                .prepareStatement("SELECT * FROM schedule WHERE UserEmail = ? AND IsCurrent = 1");
            selectStmt.setString(1, userEmail);
            ResultSet rstSelect = selectStmt.executeQuery();

            String scheduleID = rstSelect.getString("ScheduleID");
            ArrayList<Course> courses = getCourseList(userEmail, scheduleID);

            return new Schedule(courses, scheduleID);
        } else {
            throw new SQLException("No schedule found for that user.");
        }
    }

    /**
     *
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

    public ArrayList<Course> getCourseList(String userEmail, String scheduleID) throws SQLException {
        PreparedStatement pstmtCheck = conn
            .prepareStatement("SELECT * FROM courseReference WHERE UserEmail = ? AND ScheduleID = ?");
        pstmtCheck.setString(1, userEmail);
        pstmtCheck.setString(2, scheduleID);
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

    public Course createCourse(ResultSet rst) throws SQLException {
        if (rst.next()) {
            ArrayList<Day> days = new ArrayList<Day>();
            String daysString = rst.getString("Weekday");
            for (char c : daysString.toCharArray()) {
                days.add(Day.getDay(c));
            }
            String code = rst.getString("CourseCode");
            return new Course(rst.getInt("CourseID"), rst.getString("CourseCode"), rst.getString("CourseName"),
                rst.getString("StartTime"), rst.getString("EndTime"), code.charAt(code.length() - 1),
                days);
        } else {
            throw new SQLException("No course found with that code.");
        }
    }

    // TODO: Kevin -> sprint 2
    public void updateYear(){};
    public void updateMajor(){};
    public void updateEmail(){};
    
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
//        db.processCourses("CourseDB_WithFictionalCapacities.csv");

        try {
            db.addAccount("test", "testPass");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}