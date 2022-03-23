package ScheduleCreator;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class DateAndTime {

    private final String TIMEFORMAT = "h:mm a";
    private final String DAYOFWEEKFORMAT = "u";
    private Date time1;
    private Date time2;
    private ArrayList<Date> daysOfWeek;
    private Date combined;

    public DateAndTime(){
    }

    public void setTime(String time1, String time2) throws ParseException {
        SimpleDateFormat inputParser = new SimpleDateFormat(TIMEFORMAT, Locale.US);
        this.time1 = inputParser.parse(time1);
        this.time2 = inputParser.parse(time2);
    }

    public long timeDifference(){
        return (time2.getTime() - time1.getTime());
    }

    public void setDaysOfWeek(ArrayList<Character> daysOfWeek) throws ParseException {
        SimpleDateFormat inputParser = new SimpleDateFormat(DAYOFWEEKFORMAT);
        for(Character c: daysOfWeek){
            this.daysOfWeek.add(inputParser.parse(String.valueOf(c)));
        }
    }

    public ArrayList<Date> getDaysOfWeek() {
        return daysOfWeek;
    }

}
