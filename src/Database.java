package ScheduleCreator;
import java.sql.*;
import java.util.Properties;

public class Database { 
    private Connection conn;

    public Database(String username, String password, String schema) { 
        try {
            Properties info = new Properties();
            info.put("user", username);
            info.put("password", password);
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + schema, info);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String args[]) {
        Database db = new Database("root", "password", "sys");
    }
}