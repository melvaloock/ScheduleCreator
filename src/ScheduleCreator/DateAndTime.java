package ScheduleCreator;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class DateAndTime  {

    private String time;
    private ArrayList<Character> daysOfTheWeek;
    private String timeFormat = "h:mm a";

    //consider making blank
    public DateAndTime(String time, ArrayList<Character> daysOfTheWeek){
        this.time = time;
        this.daysOfTheWeek = daysOfTheWeek;
    }

    public DateFormat convertTime(String time, ArrayList<Character> daysOfTheWeek){
        SimpleDateFormat inputParser = new SimpleDateFormat(timeFormat, Locale.US);
        //not done
        return inputParser;
    }

    //method to compare a current time
    public boolean compareTimes(String time1, String time2){
       //not done
        //a am-pm-of-day text  PM
    return false;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ArrayList<Character> getDaysOfTheWeek() {
        return daysOfTheWeek;
    }

    public void setDaysOfTheWeek(ArrayList<Character> daysOfTheWeek) {
        this.daysOfTheWeek = daysOfTheWeek;
    }
}
