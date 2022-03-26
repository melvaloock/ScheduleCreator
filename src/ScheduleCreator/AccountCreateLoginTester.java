package ScheduleCreator;

import org.junit.Assert;
import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;


public class AccountCreateLoginTester {

    // db variables
    private final Database db = new Database("root", "EnuzPkHDO29J6gCH", "schedule_creator_db");
    private Connection conn;

    // variables for adding test data to db
    private final String email = "AccountTestEmail@email.com";
    private final String password = "testPassword";
    private final String major = "Computer Science";
    private final String currentScheduleID = "SPRING2022";
    private final String nonCurrentScheduleID = "FALL2021"; // may be used in addScheduleTest when fixed

    private final int year = 2023;

    private final Course testCourse1 = new Course("ACCT 202", "PRINCIPLES OF ACCOUNTING II", "8:00", "8:50", 'A', "MWF", 100001);
    private final Course testCourse2 = new Course("BIOL 102", "GENERAL BIOLOGY II", "9:00", "9:50", 'B', "MWF", 100035);

    private final ArrayList<Course> courseList = makeCourseList();
    private final Schedule currentSchedule = makeCurrentSchedule();

    private ArrayList<Course> makeCourseList(){
        ArrayList<Course> temp = new ArrayList<>();
        temp.add(testCourse1);
        temp.add(testCourse2);
        return temp;
    }

    private Schedule makeCurrentSchedule() {
        Schedule temp = new Schedule(courseList, currentScheduleID);
        temp.isCurrent = true;
        return temp;
    }

    /**
     * call at the start of every test that needs access to the db
     */
    private void dbSetUp() throws SQLException {
        String schema = "schedule_creator_db";
        String username = "root";
        String password = "EnuzPkHDO29J6gCH";
        Properties info = new Properties();
        info.put("user", username);
        info.put("password", password);
        conn = DriverManager.getConnection("jdbc:mysql://34.121.27.151/" + schema, info);
    }

    /**
     * call at the end of every test that needs access to the db
     */
    private void dbTearDown() throws SQLException {
        conn.close();
        db.endDatabaseConnection();
    }

    /**
     * adds test data to the db.
     * only run this once.
     * if the data is removed from the db, run this before running the tests
     */
    @Test   // not actually a test (only made it a test for easy run of this code)
    public void addTestData() throws SQLException {
        dbSetUp();
        PreparedStatement stmnt;

        // add account
        stmnt = conn.prepareStatement("INSERT INTO account values(?, ?, ?, ?)");
        stmnt.setString(1, email);
        stmnt.setString(2, password);   // not hashing the password for test data
        stmnt.setString(3, major);
        stmnt.setInt(4, year);
        stmnt.executeUpdate();

        // add current schedule
        stmnt = conn.prepareStatement("INSERT INTO schedule values (?, ?, ?)");
        stmnt.setString(1, currentScheduleID);
        stmnt.setInt(2, 1);
        stmnt.setString(3, email);
        stmnt.executeUpdate();

        stmnt = conn.prepareStatement("INSERT INTO courseReference values(?, ?, ?, ?)");
        for (Course add: courseList) {
            stmnt.setString(1, add.getCode());
            stmnt.setString(2, add.getTitle());
            stmnt.setString(3, currentScheduleID);
            stmnt.setString(4, email);
            stmnt.executeUpdate();
        }

        // clean up
        stmnt.close();
        dbTearDown();
    }

    /**
     * removes test data from the db.
     * NOTE: the tests won't work properly if the db does not have the test data
     */
    @Test   // not actually a test (only made it a test for easy run of this code)
    public void removeTestData() throws SQLException {
        dbSetUp();
        PreparedStatement stmnt;

        // remove courses
        stmnt = conn.prepareStatement("DELETE FROM courseReference WHERE UserEmail = ?");
        stmnt.setString(1, email);
        stmnt.executeUpdate();

        // remove schedule
        stmnt = conn.prepareStatement("DELETE FROM schedule WHERE UserEmail = ?");
        stmnt.setString(1, email);
        stmnt.executeUpdate();

        // remove account
        stmnt = conn.prepareStatement("DELETE FROM account WHERE UserEmail = ?");
        stmnt.setString(1, email);
        stmnt.executeUpdate();

        stmnt.close();
        dbTearDown();
    }

    @Test
    public void passwordHashWorks() throws PasswordStorage.CannotPerformOperationException, PasswordStorage.InvalidHashException {
        String hash = PasswordStorage.createHash(password);
        Assert.assertTrue(PasswordStorage.verifyPassword(password, hash));
        Assert.assertFalse(PasswordStorage.verifyPassword("wrong", hash));
    }


    @Test
    public void canCreateAccount() throws SQLException, PasswordStorage.CannotPerformOperationException {
        dbSetUp();

        // need different vars from the gobal ones because the data from the global vars is
        // already in the db
        String addAccountEmail = "addTestEmail@email.com";
        String addAccountPassword = "password";

        // add account
        db.addAccount(addAccountEmail, addAccountPassword);

        // should throw exception if account already exists
        Assert.assertThrows(SQLException.class, () -> db.addAccount(addAccountEmail, addAccountPassword));

        PreparedStatement stmnt = conn.prepareStatement("SELECT * FROM account WHERE UserEmail = ?");
        stmnt.setString(1, addAccountEmail);
        ResultSet rstCheck = stmnt.executeQuery();

        // if has next, then account exists with the info given
        Assert.assertTrue(rstCheck.next());

        // remove account from db
        stmnt = conn.prepareStatement("DELETE FROM account WHERE UserEmail = ?");
        stmnt.setString(1, addAccountEmail);
        stmnt.executeUpdate();

        stmnt.close();
        rstCheck.close();
        dbTearDown();
    }

    @Test
    public void canCreateAccountWithMajorYear() throws SQLException, PasswordStorage.CannotPerformOperationException {
        dbSetUp();

        // need different vars from the gobal ones because the data from the global vars is
        // already in the db
        String addAccountEmail = "addTestEmail@email.com";
        String addAccountPassword = "password";
        String addAccountMajor = "Computer Science";
        int addAccountYear = 2019;

        // add account
        db.addAccount(addAccountEmail, addAccountPassword, addAccountMajor, addAccountYear);

        PreparedStatement stmnt = conn.prepareStatement("SELECT * FROM account WHERE UserEmail = ?");
        stmnt.setString(1, addAccountEmail);
        ResultSet rstCheck = stmnt.executeQuery();

        // if has next, then account exists with the info given
        Assert.assertTrue(rstCheck.next());

        // remove account from db
        stmnt = conn.prepareStatement("DELETE FROM account WHERE UserEmail = ?");
        stmnt.setString(1, addAccountEmail);
        stmnt.executeUpdate();

        stmnt.close();
        rstCheck.close();
        dbTearDown();
    }

    @Test
    public void canLoginToAccount() throws SQLException, PasswordStorage.CannotPerformOperationException, PasswordStorage.InvalidHashException {

        // check that a student object was returned
        Assert.assertNotNull(db.checkLogin(email, password));

        // wrong password throws exception
        Assert.assertThrows(SQLException.class, () -> db.checkLogin(email, "wrong"));


    }

    @Test
    public void addScheduleTest() throws SQLException {
        // TODO: update this test when addSchedule is updated
        dbSetUp();

        String addSchedCurrentScheduleID = "AddTestID";
        String addSchedEmail = "AddTestEmail@email.com";

        // add a current schedule
        db.addSchedule(addSchedCurrentScheduleID, true, addSchedEmail);

        PreparedStatement stmnt = conn.prepareStatement("SELECT * FROM schedule WHERE UserEmail = ? AND ScheduleID = ? AND IsCurrent = ?");
        stmnt.setString(1, addSchedEmail);
        stmnt.setString(2, addSchedCurrentScheduleID);
        stmnt.setInt(3, 1);
        ResultSet rstCheck = stmnt.executeQuery();

        // current schedule was added
        Assert.assertTrue(rstCheck.next());

        stmnt = conn.prepareStatement("DELETE FROM schedule WHERE UserEmail = ? AND ScheduleID = ? AND IsCurrent = ?");
        stmnt.setString(1, addSchedEmail);
        stmnt.setString(2, addSchedCurrentScheduleID);
        stmnt.setInt(3, 1);
        stmnt.executeUpdate();

        // add non current schedule
        db.addSchedule(addSchedCurrentScheduleID, false, addSchedEmail);

        stmnt = conn.prepareStatement("SELECT * FROM schedule WHERE UserEmail = ? AND ScheduleID = ? AND IsCurrent = ?");
        stmnt.setString(1, addSchedEmail);
        stmnt.setString(2, addSchedCurrentScheduleID);
        stmnt.setInt(3, 0);
        rstCheck = stmnt.executeQuery();

        // non current schedule was added
        Assert.assertTrue(rstCheck.next());

        stmnt = conn.prepareStatement("DELETE FROM schedule WHERE UserEmail = ? AND ScheduleID = ? AND IsCurrent = ?");
        stmnt.setString(1, addSchedEmail);
        stmnt.setString(2, addSchedCurrentScheduleID);
        stmnt.setInt(3, 0);
        stmnt.executeUpdate();

        stmnt = conn.prepareStatement("DELETE FROM account WHERE UserEmail = ?");
        stmnt.setString(1, addSchedEmail);
        stmnt.executeUpdate();

        stmnt.close();
        rstCheck.close();
    }

    @Test
    public void getYearTest() throws SQLException {
        Assert.assertEquals(year, db.getYear(email));
    }

    @Test
    public void getMajorTest() throws SQLException {
        Assert.assertEquals(major, db.getMajor(email));
    }

    @Test
    public void getCourseListTest() throws SQLException {
        Assert.assertEquals(courseList, db.getCourseList(email, currentScheduleID));
    }

    @Test
    public void createCourseTest() throws SQLException {
        dbSetUp();
        PreparedStatement stmnt = conn.prepareStatement("SELECT * FROM course WHERE CourseID = ?");
        stmnt.setInt(1, testCourse1.getReferenceNum());
        ResultSet rs = stmnt.executeQuery();

        Course actual = db.createCourse(rs);

        Assert.assertEquals(testCourse1, actual);

        stmnt.close();
        rs.close();
    }

    @Test
    public void getCurrentScheduleTest() throws SQLException {
        Assert.assertEquals(currentSchedule, db.getCurrentSchedule(email));
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
