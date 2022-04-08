package sleeplessdevelopers.schedulecreator;

import org.junit.Test;
import org.junit.Assert;

public class CourseTester {

    @Test
    public void daysToStringTest(){
        Course c1 = new Course("", "", "", "", ' ', "MWF");
        Course c2 = new Course("", "", "", "", ' ', "T");
        Course c3 = new Course("", "", "", "", ' ', "");

        Assert.assertEquals("MWF", c1.daysToString());
        Assert.assertEquals("T", c2.daysToString());
        Assert.assertEquals("", c3.daysToString());

    }

}
