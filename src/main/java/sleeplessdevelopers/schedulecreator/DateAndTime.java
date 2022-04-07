package main.java.sleeplessdevelopers.schedulecreator;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class DateAndTime {

    private final String TIMEFORMAT = "k:mm:ss";
    private Date time1;
    private Date time2;
    private ArrayList<Date> scheduleTime = new ArrayList<>();;

    public DateAndTime(){
    }

    public void setTime(String scheduleTime) throws ParseException {
        SimpleDateFormat inputParser = new SimpleDateFormat(TIMEFORMAT, Locale.US);
        this.scheduleTime.add(inputParser.parse(scheduleTime));
    }

    public void setTime(String time1, String time2) throws ParseException {
        SimpleDateFormat inputParser = new SimpleDateFormat(TIMEFORMAT, Locale.US);
        this.time1 = inputParser.parse(time1);
        this.time2 = inputParser.parse(time2);
    }

    public double timeDifference(){
        return (((double)(time2.getTime() - time1.getTime())/60000));
    }

    public ArrayList<Integer> findDayOfWeek(ArrayList<String> daysOfWeek) throws ParseException {
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri"};
        ArrayList<Integer> dayLoc = new ArrayList<>();
        for(int x = 0; x < days.length; x++){
            if(daysOfWeek.contains(days[x])){
                dayLoc.add(x+1);
            }
        }
        return dayLoc;
    }
    //    It returns the value 0 if the argument Date is equal to this Date.
    //    It returns a value less than 0 if this Date is before the Date argument.
    //    It returns a value greater than 0 if this Date is after the Date argument.
    public ArrayList<ArrayList<Integer>> locOfClass(String time1, String time2, ArrayList<String> daysOfWeek) throws ParseException {
        ArrayList<ArrayList<Integer>> loc = new ArrayList<>();
        ArrayList<Integer> timeLoc = new ArrayList<>();
        String[] times = {"8:00:00", "9:00:00", "10:00:00", "11:00:00", "12:00:00",
                "13:00:00", "14:00:00", "15:00:00", "16:00:00", "17:00:00", "18:00:00", "19:00:00", "20:00:00"};

        setTime(time1,time2);

        for (String time : times) {
            setTime(time);
        }
        for(int x = 0; x < times.length; x++){
            if(scheduleTime.get(x).compareTo(this.time1) == 0){
                if(timeDifference() == 50){
                    timeLoc.add(x+1);
                }else if(timeDifference() == 75 || timeDifference() == 120){
                    timeLoc.add(x+1);
                    timeLoc.add(x+2);
                }else if(timeDifference() == 180){
                    timeLoc.add(x+1);
                    timeLoc.add(x+2);
                    timeLoc.add(x+3);
                }
            }
        }

        loc.add(timeLoc);
        loc.add(findDayOfWeek(daysOfWeek));
        return loc;
    }

}
