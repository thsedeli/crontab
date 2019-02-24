import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CronTab {
    private static final Integer MINUTE_RANGE = 59;
    private static final Integer HOUR_RANGE = 23;
    private static final Integer DAY_OF_THE_MONTH_RANGE = 30;
    private static final Integer MONTH_RANGE = 11;
    private static final Integer DAY_OF_THE_WEEK_RANGE = 6;

    public static void main(String[] args) {
        List<String> inputArgs;
        try {
            inputArgs = validateInput(args);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }

        for (String crontab : parseInput(inputArgs))
            System.out.println(crontab);
    }

    /**
     * Given the initial arguments of the application checks if the input is of valid format
     */
    static List<String> validateInput(String[] args) {
        List<String> input = Arrays.asList(args);

        if (input.size() < 6)
            throw new IllegalArgumentException("Number of arguments should always be 6");

        return input;
    }

    /**
     * Given a list of input strings returns a list of parsed input with the format we need to show them
     */
    static List<String> parseInput(List<String> input) {
        List<String> parsedInput = new ArrayList<String>();

        try {
            parsedInput.add(parseValue("Minute", input.get(0), MINUTE_RANGE));
            parsedInput.add(parseValue("Hour", input.get(1), HOUR_RANGE));
            parsedInput.add(parseValue("Day of the month", input.get(2), DAY_OF_THE_MONTH_RANGE));
            parsedInput.add(parseValue("Month", input.get(3), MONTH_RANGE));
            parsedInput.add(parseValue("Day of the week", input.get(4), DAY_OF_THE_WEEK_RANGE));
            parsedInput.add(parseCommands(input.subList(5, input.size())));
        } catch (IllegalArgumentException e) {
            return Collections.singletonList(e.getMessage());
        }

        return parsedInput;
    }

    /**
     * Given a list of string commands it appends them in one string
     */
    private static String parseCommands(List<String> commands) {
        StringBuilder parsedCommand = new StringBuilder("Command: ");
        for (String command : commands)
            parsedCommand.append(command).append(" ");
        return String.valueOf(parsedCommand).trim();
    }

    /**
     * Given the type of input, the actual value and the range of this type it returns the string in the format we want to show it
     */
    static String parseValue(String type, String value, Integer range) {
        StringBuilder returnedValue = new StringBuilder(type).append(":");

        if (value.equals("*")) {
            returnedValue.append(returnAllValues(range));
        }
        else if (value.contains("-")) {
            try {
                returnedValue.append(parseRange(value, range));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(type + " value must be between 0-" + range);
            } catch (ArithmeticException e) {
                throw new IllegalArgumentException(type + " value: " + e.getMessage());
            }
        } else {
            for (String splitValues : Arrays.asList(value.split(","))) {
                Integer parsedInt = Integer.parseInt(splitValues);
                if (checkRange(range, parsedInt))
                    throw new IllegalArgumentException(type + " value must be between 0-" + range);
                returnedValue.append(" ").append(parsedInt);
            }
        }
        return String.valueOf(returnedValue);
    }

    /**
     * Given an Integer returns a string which contains all the values within the range: 0-range
     */
    private static String returnAllValues(Integer range) {
        StringBuilder allValuesString = new StringBuilder();
        for (int i = 0; i <= range; i++) {
            allValuesString.append(" ").append(i);
        }
        return String.valueOf(allValuesString);
    }

    /**
     * Given a string with the input value and an integer for a range returns the string that we want to show
     */
    private static String parseRange(String value, Integer range) {
        StringBuilder parsedRange = new StringBuilder();
        String rangePart = value.split("/")[0];
        Integer parsedStep = value.split("/").length > 1 ? Integer.parseInt(value.split("/")[1]) : 1;

        Integer begin = Integer.parseInt(rangePart.split("-")[0]);
        Integer end = Integer.parseInt(rangePart.split("-")[1]);

        if (checkRange(range, begin) || checkRange(range, end))
            throw new IllegalArgumentException();
        else if (begin > end)
            throw new ArithmeticException("Beginning of range cannot be larger than the ending");

        for(int i = begin; i <= end; i += parsedStep)
            parsedRange.append(" ").append(i);

        return String.valueOf(parsedRange);
    }

    /**
     * Checks if an integer is in the range
     */
    private static boolean checkRange(Integer range, Integer parsedInt) {
        return 0 > parsedInt || parsedInt > range;
    }

}
