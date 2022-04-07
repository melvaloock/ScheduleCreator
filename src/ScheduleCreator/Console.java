package ScheduleCreator;

import java.util.ArrayList;
import java.util.Scanner;

import java.io.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class Console extends UserInterface{

    private static Scanner scn = new Scanner(System.in);
    private static int pageID;

    /** - provides the user with a menu to choose between manual creation (1) or recommended schedule (2)
     * if the user chooses 1, call the consoleSearch method
     * if the user chooses 2, get the input for their major and year
     * for now, stop here
     */
    public static void consoleScheduleChoice() {
        int yn;
        System.out.println("Do you want automatic scheduling?");
        System.out.println("1) No, I want to manually set up my schedule.");
        System.out.println("2) Yes, set up my schedule for me.");
        yn = intEntry(1,2,scn);
        if (yn == 1){
            consoleSchedulePage();
        }
        else {
            consoleRecommendedSchedule();
        }
    }

    /**
     * Melva
     */
    public static void consoleRecommendedSchedule() {
        int year;
        String major;
        System.out.println("What is your major? If none listed, we do not have a recommended schedule available");
        System.out.println("0) None Listed");
        for (int i = 0; i < recMajors.size(); i++) {
            System.out.println(i + 1 +") " + recMajors.get(i));
        }
        int mEntry = intEntry(0, recMajors.size(), scn);
        if (mEntry == 0) {
            consoleSchedulePage();
        } else {
            major = recMajors.get((mEntry) - 1);

            System.out.println("What is your graduation year?");
            year = intEntry(2000, 2030, scn);

            RecommendedSchedule rs = new RecommendedSchedule(major, year, db);
            currentStudent.setCurrentSchedule(rs.makeCurrentSchedule());
            consoleSchedulePage();
        }
    }

    /**
     * Melva
     * @return
     */
    public static ArrayList<Course> consoleSearch() {

        Scanner searchScan = new Scanner(System.in); //takes user input for now
        boolean search = false;
        ArrayList<Course> searchResults = new ArrayList<>();

        do {

            System.out.println("Would you like to search by code (1) or keyword (2)?");
            int searchType = intEntry(1, 2, searchScan);
            searchScan.nextLine(); //clears scanner

            if (searchType == 1) {
                System.out.println("Searching by course code \nEnter the course code you would like to search for");

                String code = searchScan.nextLine().toUpperCase(); //matches all to the format in the database
                //check for valid user input
                searchResults = searchCoursesByCode(code);
            } else if (searchType == 2) {
                System.out.println("Searching by keyword \nEnter the keyword you would like to search for");

                String keyword = searchScan.nextLine().toUpperCase(); //matches all to the format in the database
                //check for valid user input
                searchResults = searchCoursesByKeyword(keyword);
            }

            System.out.println("Search results:");
            for (int i = 1; i <= searchResults.size(); i++) {
                System.out.println(i + ". " + searchResults.get(i - 1));
            }

            if (searchResults.isEmpty()) {
                System.out.println("You search returned no courses.");
            }

            System.out.println("Would you like to search again? (y/n)");
            if (ynEntry(searchScan) == 'Y') {
                search = true;
            } else {
                search = false;
            }

        } while (search);

        return searchResults;
    }

    /**
     * Melva
     * @param searchResults
     * @return
     */
    public static ArrayList<Course> consoleFilter(ArrayList<Course> searchResults) {

        Scanner filterScan = new Scanner(System.in); //takes user input for now


        boolean filter = false;
        ArrayList<Course> filterResults = new ArrayList<>();

        do {

            System.out.println("Would you like to filter by day (1) or time of day (2)?");
            int filterType = intEntry(1, 2, filterScan);
            filterScan.nextLine();

            if (filterType == 1) {
                System.out.println("Enter all the days that you want to see results for:");
                String filterDays = filterScan.nextLine().toUpperCase();
                filterResults = dayFilter(searchResults, filterDays);

            } else if (filterType == 2) {
                System.out.println("Enter the numbers of all the times you want to see results for (separated by commas):");
                for (int i = 1; i <= 13; i++) {
                    System.out.printf("%d) %s \n", i, intToTime(i));
                }

                filterScan.useDelimiter(",");
                ArrayList<String> filterTimes = new ArrayList<>();

                while (filterScan.hasNextInt()) {
                    int i = filterScan.nextInt();
                    filterTimes.add(intToTime(i));
                }

                filterResults = timeFilter(searchResults, filterTimes);
            }

        } while (filter);

        System.out.println("Filtered results:");
        if (filterResults.isEmpty()) {
            System.out.println("You search returned no courses");
        } else {
            for (int i = 1; i <= filterResults.size(); i++) {
                System.out.println(i + ". " + filterResults.get(i - 1));
            }
        }

        return filterResults;
    }

    /** TYLER
     * shows the schedule page (can be either current or saved schedule)
     * should call the displaySchedule task from Schedule class
     * @param s
     */
    public static void viewSchedule(Schedule s) {
        s.displaySchedule2();


//        try {
//            s.displaySchedule3();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        //will alter to my method once it functions
    }


    /**
     * gives the user a selection of actions to take to alter their schedule.
     * the user can add and remove courses.
     * the user can clear all courses from their schedule.
     * @param s CurrentSchedule
     */
    public static void consoleAlterSchedule(CurrentSchedule s) {
        int choice;
        int courseEntry;
        while (true) {
            viewSchedule(currentStudent.getCurrentSchedule());
            System.out.println("Alter Schedule Choices: ");
            System.out.println("1. Add Course\n2. Remove Course\n3. Clear Schedule \n4. Return to Previous Menu");
            choice = intEntry(1, 4, scn);
            if (choice == 1) { // add course

                // course search
                ArrayList<Course> searchResults = consoleSearch();
                if (searchResults.isEmpty()) continue;

                ArrayList<Course> results = new ArrayList<>();
                System.out.println("Would you like to filter your search? (y/n)");
                if (ynEntry(scn) == 'N') {
                    results = searchResults;
                } else {

                    // filter search
                    boolean filterAgain = false;
                    do {
                        results = consoleFilter(searchResults);
                        System.out.println("Would you like to use another filter?");
                        if (ynEntry(scn) == 'Y') {
                            filterAgain = true;
                        } else {
                            filterAgain = false;
                        }
                    } while (filterAgain);
                }

                // ask for course to add (option to add none and/or search again)
                System.out.println("Which course would you like to add? ");
                System.out.print("Corresponding Integer (enter 0 to add none): ");

                ArrayList<Course> coursesToAdd = new ArrayList<>();

                courseEntry = intEntry(0, results.size(), scn);

                // add none
                if (courseEntry == 0) continue;

                coursesToAdd.add(results.get(courseEntry - 1));

                // see if the user wants to add any other courses from the list
                while (true) {
                    System.out.println("Add another course? (y/n)");
                    char addAnother = ynEntry(scn);
                    if (addAnother == 'Y') {
                        System.out.print("Corresponding Integer (enter 0 to add none): ");
                        courseEntry = intEntry(0, results.size(), scn);

                        // add none
                        if (courseEntry == 0) break;
                        coursesToAdd.add(results.get(courseEntry - 1));
                    } else if (addAnother == 'N') {
                        break;
                    }
                }

                // check if there are any conflicts
                boolean hasConflict = addCourses(coursesToAdd);

                /*
                 * if there are conflicts, the courses not added and the user is told,
                 * otherwise the courses are added and they user is told that they are added
                 */
                if (hasConflict) {
                    System.out.println("The course you selected conflicts with another course in your schedule," +
                            " so it cannot be added.");
                } else {
                    System.out.println("Course(s) added.");
                }

            } else if (choice == 2) { // remove course
                ArrayList<Course> courses = currentStudent.getCurrentSchedule().getCourseList();
                System.out.println("These are your current courses: ");

                for (int i = 1; i <= courses.size(); i++) {
                    System.out.println(i + ". " + courses.get(i-1));
                }

                System.out.println("Which course would you like to remove? ");
                System.out.print("Corresponding Integer (enter 0 to add none): ");
                courseEntry = intEntry(0, courses.size(), scn);

                // check if none
                if (courseEntry == 0) {
                    System.out.println("You entered 0, so nothing was removed.");
                    continue;
                }

                // remove course
                currentStudent.currentSchedule.removeCourse(courses.get(courseEntry-1));

                // give result
                System.out.println("Course Removed.");

            }else if (choice == 3){	// clear schedule

                // confirm they want to clear schedule
                System.out.println("Are you sure you want to clear your schedule? (Y/N) ");
                char yn = ynEntry(scn);
                if (yn == 'Y'){
                    clearSchedule();
                }

            }else if (choice == 4) { // return
                break;
            } else { // invalid choice (shouldn't reach this)
                System.out.println("Invalid choice, try again.");
            }
        }

    }

    public static int mainMenu(){
        //int selection = -1;
        //Scanner menu = new Scanner(System.in);
        System.out.println("Welcome!");
        System.out.println("1) Create an account");
        System.out.println("2) Log into an account");
        System.out.println("3) Continue as guest");
        return intEntry(1, 3, scn);

    }
    /**
     * gets email and password information from user.
     * if an account does not already exist using that email, an account is created;
     * otherwise, the user is told that an account already exists using that email
     * and is prompted to enter different information.
     */
    public static void consoleCreateAccount() {
        String userEmail;
        String userPassword;

        while (true) {
            System.out.println("Email: ");
            userEmail = scn.next();
            System.out.println("Password: ");
            userPassword = scn.next();

            if (createAccount(userEmail, userPassword)) break;
        }

        consoleScheduleChoice();
    }

    /**
     * Gets login information from user and attempts to log in to an account using that information.
     * If the attempt fails, the user has the option to try to log in again or to return to the
     * main menu.
     */
    public static void consoleLogin() {
        String userEmail, userPassword;
        char yn;
        boolean loggedIn = false;

        while(true) {
            System.out.println("Email: ");
            userEmail = scn.next();
            System.out.println("Password: ");
            userPassword = scn.next();

            if (loginToAccount(userEmail, userPassword)) {
                loggedIn = true;
                break;
            } else {
                System.out.println("Would you like to try again? (Y/N) ");
                yn = ynEntry(scn);
                if (yn == 'N') {
                    break;
                }
            }
        }

        if (loggedIn) {
            consoleScheduleChoice();
        }
        else {
            consoleMain();
        }
    }

    public static void consoleSchedulePage() {
        viewSchedule(currentStudent.getCurrentSchedule());
        System.out.println("1) Help");
        System.out.println("2) Save current schedule");
        System.out.println("3) Alter current schedule");
        System.out.println("4) Load another schedule");
        System.out.println("5) Send current schedule via email");
        System.out.println("6) Save schedule as file");
        System.out.println("7) logout");
        int in = intEntry(1,7,scn);
            switch (in) {
                case 1:
                    helpDescriptions(1);
                    break;
                case 2:
                    //save current schedule
                    currentStudent.saveCurrentSchedule(db);
                    break;
                case 3:
                    consoleAlterSchedule((CurrentSchedule) getCurrentStudent().getCurrentSchedule());
                    break;
                case 4:
                    //Load another schedule
                    break;
                case 5:
                    //send via email
                    break;
                case 6:
                    //save as file
                    chooseFileType();
                    break;
                case 7:
                    //if the currentStudent is an account and not a guest, log out here.
                    //else if its a guest then just return to mainMenu
                    pageID = 0;
                    break;
                default:
                    System.out.println("Invalid selection!"); //should not trigger in practice.
                    break;
            }
    }

    /**
     * gets a char entry from the user
     * @param scanner
     * @return char of either 'Y' or 'N'
     */
    public static char ynEntry(Scanner scanner) {
        char yn;
        while(true){
            try {
                yn = scanner.next().toUpperCase().charAt(0);
                if (yn == 'Y' || yn == 'N'){
                    break;
                } else {
                    System.out.println("Enter 'Y' or 'N'");
                }
            } catch (Exception e){
                scanner.next();
                System.out.println("Enter 'Y' or 'N'");
            }
        }
        return yn;
    }

    /**
     * gets an int entry from the user
     * @param min
     * @param max
     * @param scanner
     * @return an int between min and max
     */
    public static int intEntry(int min, int max, Scanner scanner) {
        int entry;
        while(true){
            try {
                entry = scanner.nextInt();
                if (entry >= min && entry <= max){
                    break;
                } else {
                    System.out.println("Enter an integer between " + min + " and "+ max);
                }
            } catch (Exception e){
                System.out.println("Enter an integer between " + min + " and "+ max);
                scanner.next();
            }
        }
        return entry;
    }

    static void consoleMain() {
        while (true) {
            switch (pageID) {
                case 0:
                    pageID = mainMenu();
                    break;
                case 1:
                    consoleCreateAccount();
                    pageID = 4;
                    break;
                case 2:
                    consoleLogin();
                    pageID = 4;
                    break;
                case 3:
                    createGuest();
                    consoleScheduleChoice();
                    pageID = 4;
                    break;
                case 4:
                    consoleSchedulePage();
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Invalid Selection");
                    mainMenu();
                    break;
            }
        }

    }

    public static void helpDescriptions(int helpID) {
        switch (helpID) {
            case 1:
                System.out.println("Here you can, save your schedule (if you have an account),");
                System.out.println("alter (change) your schedule to add or remove courses ");
                System.out.println("or clear it all together, load an existing schedule");
                System.out.println("(if you have an account), send the schedule to an email, ");
                System.out.println("or save this schedule on your computer.");
                System.out.println("1) Ok");
                intEntry(1,1,scn);
                break;
//            case 2:
//                System.out.println("");
//                System.out.println("");
//                System.out.println("");
//                System.out.println("");
//                break;
//            case 3:
//                System.out.println("1");
//                System.out.println("");
//                System.out.println("");
//                System.out.println("");
//                break;


        }
    }

    public static void chooseFileType() {
        System.out.println("1) .txt");
        System.out.println("2) .pdf");
        int in = intEntry(1, 2, scn);
        switch (in) {
            case 1:
                saveScheduleAsFile(".txt");
                break;
            case 2:
                saveScheduleAsFile(".pdf");
                break;
            default:
                // error checking (even though intEntry takes care of it)
                System.out.println("Invalid selection!");
                break;
        }
    }

    public static void saveScheduleAsFile(String fileType) {
        System.out.println("Enter the name of the file you would like to save to: ");
        String fileName = scn.next();

        switch (fileType) {
            case ".txt":
                break;
            case ".pdf":
                generatePDF(fileName);
                break;
        }
    }

    public static void generatePDF(String filename) {
        Document doc = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("./" + filename + ".pdf"));
            doc.open();
            doc.add(new Paragraph(currentStudent.getCurrentSchedule().toString()));
            doc.close();
            writer.close();
        } catch (Exception e) {
            System.out.println("Error generating PDF");
        }
    }


}



