package sleeplessdevelopers.schedulecreator;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CourseTester {

    @Test
    public void daysToStringTest(){
        Course c1 = new Course("", "", "", "", ' ', "MWF");
        Course c2 = new Course("", "", "", "", ' ', "T");
        Course c3 = new Course("", "", "", "", ' ', "");

        assertEquals("MWF", c1.daysToString());
        assertEquals("T", c2.daysToString());
        assertEquals("", c3.daysToString());

    }

    @Test
    public void daysToStringListTest(){
        Course c1 = new Course("", "", "", "", ' ', "MWF");
        Course c2 = new Course("", "", "", "", ' ', "T");
        Course c3 = new Course("", "", "", "", ' ', "");

        ArrayList<String> res1 = new ArrayList<>() {{add("Mon");add("Wed");add("Fri");}};
        ArrayList<String> res2 = new ArrayList<>() {{add("Tue");}};
        ArrayList<String> res3 = new ArrayList<>() {{add("NULL");}};

        assertEquals(res1, c1.getDayStringList());
        assertEquals(res2, c2.getDayStringList());
        assertEquals(res3, c3.getDayStringList());

    }

}
