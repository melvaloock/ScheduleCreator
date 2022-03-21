package ScheduleCreator;

import java.io.File;
import java.io.IOException;
import java.sql.*;
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
            conn = DriverManager.getConnection("jdbc:mysql:///<DATABASE_NAME>?cloudSqlInstance=sleepless-developers" +
                "-db&socketFactory=com.google.cloud.sql.mysql.SocketFactory&user=root&password=EnuzPkHDO29J6gCH" + schema, info);
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

    public void addAccount(String userEmail, String userPassword) throws SQLException {
            PreparedStatement pstmtCheck = conn.prepareStatement("SELECT * FROM account WHERE UserEmail = ?");
            pstmtCheck.setString(1, userEmail);
            ResultSet rstCheck = pstmtCheck.executeQuery();

            if (rstCheck.next()) { 
                throw new SQLException("An account already exists under that email.");
            }

            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO account values(?, ?, ?)");
            insertStmt.setString(1, userEmail);

            // TODO: this MUST be hashed before final release
            insertStmt.setString(2, userPassword);
            int rows = insertStmt.executeUpdate();

            if (rows <= 0) {
                throw new SQLException("ERROR: Account creation failed. Please try again.");
            }

            pstmtCheck.close();
            rstCheck.close(); 
            insertStmt.close();
    }

    public void addSchedule(String scheduleID, boolean isCurrent, int courseID) throws SQLException {
        // PreparedStatement pstmtCheck = conn.prepareStatement("SELECT * FROM schedule WHERE UserEmail = ?");
        // pstmtCheck.setString(1, userEmail);
        // ResultSet rstCheck = pstmtCheck.executeQuery();

        // if (rstCheck.next()) { 
        //     throw new SQLException("An account already exists under that email.");
        // }

        // PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO account values(?, ?, ?)");
        // insertStmt.setString(1, userEmail);

        // // TODO: this MUST be hashed before final release
        // insertStmt.setString(2, userPassword);
        // insertStmt.setString(3, scheduleID);
        // int rows = insertStmt.executeUpdate();

        // if (rows <= 0) {
        //     throw new SQLException("ERROR: Account creation failed. Please try again.");
        // }

        // pstmtCheck.close();
        // rstCheck.close(); 
        // insertStmt.close();
    }

    public static void main(String args[]) {
        Database db = new Database("root", "password", "sys");
        db.processCourses("CourseDB_WithFictionalCapacities.csv");
    }
}