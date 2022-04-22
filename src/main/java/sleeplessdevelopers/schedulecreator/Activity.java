package sleeplessdevelopers.schedulecreator;

import java.util.ArrayList;

public class Activity extends Course{

    public Activity(String name, String description, String startTime, String endTime, ArrayList<Day> days) {
        super(0, name, description, startTime, endTime, ' ', days);

    }



}
