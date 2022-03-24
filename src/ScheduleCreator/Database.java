package ScheduleCreator;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

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
            int courseID = 1;
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


    public void addAccount(String userEmail, String userPassword) throws SQLException, PasswordStorage.CannotPerformOperationException {
            PreparedStatement pstmtCheck = conn.prepareStatement("SELECT * FROM account WHERE UserEmail = ?");
            pstmtCheck.setString(1, userEmail);
            ResultSet rstCheck = pstmtCheck.executeQuery();

            if (rstCheck.next()) { 
                throw new SQLException("An account already exists under that email.");
            }

            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO account values(?, ?)");
            insertStmt.setString(1, userEmail);

            // TODO: this MUST be hashed before final release

            insertStmt.setString(2, PasswordStorage.createHash(userPassword));
            int rows = insertStmt.executeUpdate();

            if (rows <= 0) {
                throw new SQLException("ERROR: Account creation failed. Please try again.");
            }

            pstmtCheck.close();
            rstCheck.close(); 
            insertStmt.close();
    }

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

    public Student checkLogin(String userEmail, String userPassword) throws SQLException, PasswordStorage.InvalidHashException, PasswordStorage.CannotPerformOperationException {
        PreparedStatement userCheck = conn
                .prepareStatement("SELECT * FROM account WHERE UserEmail = ?");
        userCheck.setString(1, userEmail);
        ResultSet rstCheck = userCheck.executeQuery();

        // if an account exists with userEmail, continue
        if (rstCheck.next()) {
            String dbPass = rstCheck.getString("UserPassword");
            // check if the passwords match
            if (PasswordStorage.verifyPassword(userPassword, dbPass)){
                return getStudentInfo(userEmail);
            }
        }

        return null;
    }

    /**
     * after a user login is verified, gets the student's saved information
     * @param userEmail
     * @return
     */
    private Student getStudentInfo(String userEmail) throws SQLException {
        PreparedStatement pstmtCheck = conn.prepareStatement("SELECT * FROM schedule WHERE UserEmail = ?");
        pstmtCheck.setString(1, userEmail);
        ResultSet rstCheck = pstmtCheck.executeQuery();

        if (rstCheck.next()) {
            PreparedStatement selectStmt = conn
                .prepareStatement("SELECT * FROM student WHERE UserEmail = ? AND IsCurrent = 1");
        }
        // TODO: complete once account is setup with the methods below

        return new Student();
    }

    public Schedule getSchedule(String userEmail) throws SQLException {
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
            return new Course(rst.getString("CourseCode"), rst.getString("CourseName"),
                rst.getString("StartTime"), rst.getString("EndTime"), code.charAt(code.length() - 1),
                days);
        } else {
            throw new SQLException("No course found with that code.");
        }
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
        db.processCourses("CourseDB_WithFictionalCapacities.csv");
    }
}