package sleeplessdevelopers.schedulecreator;

import org.junit.jupiter.api.Test;

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

}
