import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CronTabTest {
    private static final Integer MINUTE_RANGE = 59;
    private static final Integer HOUR_RANGE = 23;
    private static final Integer DAY_OF_THE_MONTH_RANGE = 30;
    private static final Integer MONTH_RANGE = 11;
    private static final Integer DAY_OF_THE_WEEK_RANGE = 6;

    private static final String[] validInput = {"5", "0", "1", "2", "0", "/usr/bin/sample.sh"};
    private static final List<String> simpleInput = Arrays.asList("5", "0", "1", "2", "0", "/usr/bin/sample.sh");
    private static final String[] invalidInput = {"5", "0", "1", "2"};
    private static final List<String> inputOutOfRange = Arrays.asList("67", "0", "1", "2", "0", "/usr/bin/sample.sh");

    @Test
    public void testValidateInput() {
        List<String> expected = Arrays.asList("5", "0", "1", "2", "0", "/usr/bin/sample.sh");
        Assert.assertEquals(expected, CronTab.validateInput(validInput));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testValidateInputException() {
        CronTab.validateInput(invalidInput);
    }

    @Test
    public void testSimpleInput() {
        List<String> expected = Arrays.asList("Minute: 5", "Hour: 0", "Day of the month: 1", "Month: 2", "Day of the week: 0", "Command: /usr/bin/sample.sh");
        Assert.assertEquals(expected, CronTab.parseInput(simpleInput));
    }

    @Test
    public void testInputOutOfRange() {
        List<String> expected = Collections.singletonList("Minute value must be between 0-59");
        Assert.assertEquals(expected, CronTab.parseInput(inputOutOfRange));
    }

    @Test
    public void testSimpleParseValue() {
        String expected = "Minute: 19";
        Assert.assertEquals(expected, CronTab.parseValue("Minute", "19", MINUTE_RANGE));

        expected = "Hour: 12";
        Assert.assertEquals(expected, CronTab.parseValue("Hour", "12", HOUR_RANGE));

        expected = "Day of the month: 28";
        Assert.assertEquals(expected, CronTab.parseValue("Day of the month", "28", DAY_OF_THE_MONTH_RANGE));

        expected = "Month: 2";
        Assert.assertEquals(expected, CronTab.parseValue("Month", "2", MONTH_RANGE));

        expected = "Day of the week: 6";
        Assert.assertEquals(expected, CronTab.parseValue("Day of the week", "6", DAY_OF_THE_WEEK_RANGE));
    }

    @Test
    public void testListParseValue() {
        String expected = "Minute: 7 10";
        Assert.assertEquals(expected, CronTab.parseValue("Minute", "7,10", MINUTE_RANGE));

        expected = "Hour: 1 12 22";
        Assert.assertEquals(expected, CronTab.parseValue("Hour", "1,12,22", HOUR_RANGE));

        expected = "Day of the month: 4 28";
        Assert.assertEquals(expected, CronTab.parseValue("Day of the month", "4,28", DAY_OF_THE_MONTH_RANGE));

        expected = "Month: 2 3 4 11";
        Assert.assertEquals(expected, CronTab.parseValue("Month", "2,3,4,11", MONTH_RANGE));

        expected = "Day of the week: 0 1 2 5 6";
        Assert.assertEquals(expected, CronTab.parseValue("Day of the week", "0,1,2,5,6", DAY_OF_THE_WEEK_RANGE));
    }

    @Test
    public void testRangeParseValue() {
        String expected = "Minute: 7 8 9 10";
        Assert.assertEquals(expected, CronTab.parseValue("Minute", "7-10", MINUTE_RANGE));

        expected = "Hour: 1 2";
        Assert.assertEquals(expected, CronTab.parseValue("Hour", "1-2", HOUR_RANGE));

        expected = "Day of the month: 20 21 22 23 24 25 26 27 28";
        Assert.assertEquals(expected, CronTab.parseValue("Day of the month", "20-28", DAY_OF_THE_MONTH_RANGE));

        expected = "Month: 2 3 4";
        Assert.assertEquals(expected, CronTab.parseValue("Month", "2-4", MONTH_RANGE));

        expected = "Day of the week: 0 1 2 3 4 5 6";
        Assert.assertEquals(expected, CronTab.parseValue("Day of the week", "0-6", DAY_OF_THE_WEEK_RANGE));
    }

    @Test
    public void testRangeWithStepParseValue() {
        String expected = "Minute: 7 9";
        Assert.assertEquals(expected, CronTab.parseValue("Minute", "7-10/2", MINUTE_RANGE));

        expected = "Hour: 1";
        Assert.assertEquals(expected, CronTab.parseValue("Hour", "1-2/3", HOUR_RANGE));

        expected = "Day of the month: 20 24 28";
        Assert.assertEquals(expected, CronTab.parseValue("Day of the month", "20-28/4", DAY_OF_THE_MONTH_RANGE));

        expected = "Month: 2 4";
        Assert.assertEquals(expected, CronTab.parseValue("Month", "2-4/2", MONTH_RANGE));

        expected = "Day of the week: 0 5";
        Assert.assertEquals(expected, CronTab.parseValue("Day of the week", "0-6/5", DAY_OF_THE_WEEK_RANGE));
    }

    @Test
    public void testStarParseValue() {
        String expected = "Month: 0 1 2 3 4 5 6 7 8 9 10 11";
        Assert.assertEquals(expected, CronTab.parseValue("Month", "*", MONTH_RANGE));

        expected = "Day of the week: 0 1 2 3 4 5 6";
        Assert.assertEquals(expected, CronTab.parseValue("Day of the week", "*", DAY_OF_THE_WEEK_RANGE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseValueException() {
        CronTab.parseValue("Hour", "28", HOUR_RANGE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseRangeException() {
        CronTab.parseValue("Hour", "3-1", HOUR_RANGE);
    }
}
