package ScheduleCreator;

import org.junit.Assert;
import org.junit.Test;

import java.sql.*;
import java.util.Properties;


public class AccountCreateLoginTester {

    private final Database db = new Database("root", "EnuzPkHDO29J6gCH", "schedule_creator_db");
    private Connection conn;

    private void dbSetUp() throws SQLException {
        String schema = "schedule_creator_db";
        String username = "root";
        String password = "EnuzPkHDO29J6gCH";
        Properties info = new Properties();
        info.put("user", username);
        info.put("password", password);
        conn = DriverManager.getConnection("jdbc:mysql://34.121.27.151/" + schema, info);
    }

    @Test
    public void passwordHashWorks() throws PasswordStorage.CannotPerformOperationException, PasswordStorage.InvalidHashException {
        String pass = "somepassword";
        String hash = PasswordStorage.createHash(pass);
        Assert.assertTrue(PasswordStorage.verifyPassword(pass, hash));
        Assert.assertFalse(PasswordStorage.verifyPassword("wrong", hash));
    }


    @Test
    public void canCreateAccount() throws SQLException, PasswordStorage.CannotPerformOperationException {
        dbSetUp();

        // add account
        String email = "createAccountTest@email.com";
        String password = "create";
        db.addAccount(email, password);

        // should throw exception if account already exists
        Assert.assertThrows(SQLException.class, () -> db.addAccount(email, password));

        PreparedStatement stmnt = conn.prepareStatement("SELECT * FROM account WHERE UserEmail = ?");
        stmnt.setString(1, email);
        ResultSet rstCheck = stmnt.executeQuery();

        // if has next, then account exists with the info given
        Assert.assertTrue(rstCheck.next());

        // remove account from db
        stmnt = conn.prepareStatement("DELETE FROM account WHERE UserEmail = ?");
        stmnt.setString(1, email);
        stmnt.executeUpdate();
    }

    @Test
    public void canCreateAccountWithMajorYear() throws SQLException, PasswordStorage.CannotPerformOperationException {
        dbSetUp();

        // add account
        String email = "createAccountTestWithMajor@email.com";
        String password = "create";
        String major = "Computer Science";
        int year = 2019;
        db.addAccount(email, password, major, year);

        PreparedStatement stmnt = conn.prepareStatement("SELECT * FROM account WHERE UserEmail = ?");
        stmnt.setString(1, email);
        ResultSet rstCheck = stmnt.executeQuery();

        // if has next, then account exists with the info given
        Assert.assertTrue(rstCheck.next());

        // remove account from db
        stmnt = conn.prepareStatement("DELETE FROM account WHERE UserEmail = ?");
        stmnt.setString(1, email);
        stmnt.executeUpdate();
    }

    @Test
    public void canLoginToAccount() throws SQLException, PasswordStorage.CannotPerformOperationException, PasswordStorage.InvalidHashException {
        dbSetUp();

        // add account (assuming account creation works)
        String email = "createAccountTest@email.com";
        String password = "create";
        db.addAccount(email, password);

        // check that a student object was returned
        Assert.assertNotNull(db.checkLogin(email, password));

        // wrong password throws exception
        Assert.assertThrows(SQLException.class, () -> db.checkLogin(email, "wrong"));

        // remove account from db
        PreparedStatement stmnt = conn.prepareStatement("DELETE FROM account WHERE UserEmail = ?");
        stmnt.setString(1, email);
        stmnt.executeUpdate();
    }

    @Test
    public void addScheduleTest() throws SQLException, PasswordStorage.CannotPerformOperationException {
        dbSetUp();
        String email = "scheduleTest@email.com";
        String scheduleID = "SPRING2022";
        // add account with this email for foreign keys
        db.addAccount(email, "pass");

        // add a current schedule
        db.addSchedule(scheduleID, true, email);

        PreparedStatement stmnt = conn.prepareStatement("SELECT * FROM schedule WHERE UserEmail = ? AND ScheduleID = ? AND IsCurrent = ?");
        stmnt.setString(1, email);
        stmnt.setString(2, scheduleID);
        stmnt.setInt(3, 1);
        ResultSet rstCheck = stmnt.executeQuery();

        // current schedule was added
        Assert.assertTrue(rstCheck.next());

        stmnt = conn.prepareStatement("DELETE FROM schedule WHERE UserEmail = ? AND ScheduleID = ? AND IsCurrent = ?");
        stmnt.setString(1, email);
        stmnt.setString(2, scheduleID);
        stmnt.setInt(3, 1);
        stmnt.executeUpdate();

        // add non current schedule
        db.addSchedule(scheduleID, false, email);

        stmnt = conn.prepareStatement("SELECT * FROM schedule WHERE UserEmail = ? AND ScheduleID = ? AND IsCurrent = ?");
        stmnt.setString(1, email);
        stmnt.setString(2, scheduleID);
        stmnt.setInt(3, 0);
        rstCheck = stmnt.executeQuery();

        // non current schedule was added
        Assert.assertTrue(rstCheck.next());

        stmnt = conn.prepareStatement("DELETE FROM schedule WHERE UserEmail = ? AND ScheduleID = ? AND IsCurrent = ?");
        stmnt.setString(1, email);
        stmnt.setString(2, scheduleID);
        stmnt.setInt(3, 0);
        stmnt.executeUpdate();

        stmnt = conn.prepareStatement("DELETE FROM account WHERE UserEmail = ?");
        stmnt.setString(1, email);
        stmnt.executeUpdate();

        stmnt.close();
        rstCheck.close();
    }

    @Test
    public void getYearTest() throws SQLException, PasswordStorage.CannotPerformOperationException {
        dbSetUp();

        // add account
        String email = "yearTest@email.com";
        String password = "create";
        String major = "Computer Science";
        int year = 2019;
        db.addAccount(email, password, major, year);

        Assert.assertEquals(year, db.getYear(email));

        // remove account from db
        PreparedStatement stmnt = conn.prepareStatement("DELETE FROM account WHERE UserEmail = ?");
        stmnt.setString(1, email);
        stmnt.executeUpdate();
    }

    @Test
    public void getMajorTest() throws SQLException, PasswordStorage.CannotPerformOperationException {
        dbSetUp();

        // add account
        String email = "yearTest@email.com";
        String password = "create";
        String major = "Computer Science";
        int year = 2019;
        db.addAccount(email, password, major, year);

        Assert.assertEquals(major, db.getMajor(email));

        // remove account from db
        PreparedStatement stmnt = conn.prepareStatement("DELETE FROM account WHERE UserEmail = ?");
        stmnt.setString(1, email);
        stmnt.executeUpdate();
    }

    @Test
    public void getCourseListTest() {

    }

    @Test
    public void createCourseTest() {

    }

    @Test
    public void getCurrentScheduleTest() {

    }

    @Test
    public void getAccountTest() {

    }

    @Test
    public void searchByCodeTest() {

    }

    @Test
    public void searchByKeywordTest() {

    }

}
