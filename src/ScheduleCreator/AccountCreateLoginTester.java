package ScheduleCreator;

import org.junit.Assert;
import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
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
        // TODO: update this test when addSchedule is updated
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
    public void getCourseListTest() throws SQLException, PasswordStorage.CannotPerformOperationException {
        dbSetUp();
        ArrayList<Course> expected = new ArrayList<>();
        Course c1 = new Course("ACCT 202", "PRINCIPLES OF ACCOUNTING II", "8:00", "8:50", 'A', "MWF");
        Course c2 = new Course("BIOL 102", "GENERAL BIOLOGY II", "9:00", "9:50", 'B', "MWF");
        expected.add(c1);
        expected.add(c2);

        String email = "courseListTest@email.com";
        String scheduleID = "SPRING2022";
        // add account with this email for foreign keys
        db.addAccount(email, "pass");

        // add a current schedule
        PreparedStatement stmnt = conn.prepareStatement("INSERT INTO schedule values(?, ?, ?)");
        stmnt.setString(1, scheduleID);
        stmnt.setString(2, "1");
        stmnt.setString(3, email);

        // add courses to courseReference
        stmnt = conn.prepareStatement("INSERT INTO courseReference values(?, ?, ?, ?)");
        for (Course add: expected) {
            stmnt.setString(1, add.getCode());
            stmnt.setString(2, add.getTitle());
            stmnt.setString(3, scheduleID);
            stmnt.setString(4, email);
        }

        ArrayList<Course> actual = db.getCourseList(email, scheduleID);

        Assert.assertEquals(expected, actual);

        // clean up
        stmnt = conn.prepareStatement("DELETE FROM courseReference WHERE UserEmail = ?");
        stmnt.setString(1, email);
        stmnt.executeUpdate();

        stmnt = conn.prepareStatement("DELETE FROM schedule WHERE UserEmail = ?");
        stmnt.setString(1, email);
        stmnt.executeUpdate();

        stmnt = conn.prepareStatement("DELETE FROM account WHERE UserEmail = ?");
        stmnt.setString(1, email);
        stmnt.executeUpdate();

        stmnt.close();
    }

    @Test
    public void createCourseTest() throws SQLException {
        dbSetUp();
        Course expected = new Course("ACCT 202", "PRINCIPLES OF ACCOUNTING II", "8:00", "8:50", 'A', "MWF");

        PreparedStatement stmnt = conn.prepareStatement("SELECT * FROM course WHERE CourseID = ?");
        stmnt.setInt(1, 100001);
        ResultSet rs = stmnt.executeQuery();

        Course actual = db.createCourse(rs);

        Assert.assertEquals(expected, actual);

        stmnt.close();
        rs.close();
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
