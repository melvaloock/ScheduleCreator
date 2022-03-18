package ScheduleCreator;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;
import java.time.LocalTime;

public class Database { 
    private Connection conn;

    public Database(String username, String password, String schema) { 
        try {
            Properties info = new Properties();
            info.put("user", username);
            info.put("password", password);
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + schema, info);
            System.out.println("Connection successful!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
    }

    public void addCourse(int CourseID, String courseCode, String courseName, String startTime, String endTime, String weekday, int enrollment, int capacity) {
        try {
            PreparedStatement pstmtCheck = conn.prepareStatement("SELECT * FROM course WHERE CourseID = ?");
            pstmtCheck.setInt(1, CourseID);
            ResultSet rstCheck = pstmtCheck.executeQuery();

            if (rstCheck.next()) { 
                throw new SQLException("That course already exists.");
            }

            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO course values(?, ?, ?, ?, ?, ?, ?, ?)");
            insertStmt.setInt(1, CourseID);
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

    public void addAccount(int userID, String userEmail, String userPassword) {
        
    }

    public static void main(String args[]) {
        Database db = new Database("root", "password", "sys");
        db.processCourses("CourseDB_WithFictionalCapacities.csv");
    }
}