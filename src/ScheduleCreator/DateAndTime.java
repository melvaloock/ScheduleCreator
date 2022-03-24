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
    private ArrayList<Date> scheduleTime;
    private ArrayList<Date> daysOfWeek;

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
        return (((double)(time2.getTime() - time1.getTime())/60000)/60);
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



    //    It returns the value 0 if the argument Date is equal to this Date.
    //    It returns a value less than 0 if this Date is before the Date argument.
    //    It returns a value greater than 0 if this Date is after the Date argument.
    public ArrayList<ArrayList<Integer>> locOfClass(String time1, String time2, ArrayList<Character> daysOfWeek) throws ParseException {
        ArrayList<ArrayList<Integer>> loc;
        setTime(time1,time2);

        String[] times = {"8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM",
                "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM", "6:00 PM", "7:00 PM", "8:00 PM"};
        for (String tim: times){
            setTime(tim);
        }
        for(int x = 0; x < times.length; x++){
            if(scheduleTime.get(x).compareTo(this.time1) == 0){
                if(timeDifference() > 1){

                }
            }
        }

        return null;
    }

//    public ArrayList<Integer> findDayOfWeek(ArrayList<Character> daysOfWeek) throws ParseException {
//        setDaysOfWeek(daysOfWeek);
//        int[] days = {1,2,3,4,5};
//        ArrayList<Integer> dayLoc;
//        for(int x = 0; x < daysOfWeek.size(); x++){
//            if(this.daysOfWeek.get(x) == days[x]){
//
//            }
//        }
//    }

}
