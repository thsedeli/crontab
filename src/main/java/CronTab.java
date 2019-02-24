import java.util.Arrays;
import java.util.List;

public class CronTab {

    public static void main(String[] args) {
        try {
            validateInput(Arrays.asList(args));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    static void validateInput(List<String> input) {
        if (input)
    }
}
