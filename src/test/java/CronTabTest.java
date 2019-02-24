import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class CronTabTest {
    List<String> validInput = Arrays.asList("", "", "", "", "", "", "");
    @Test
    public void testValidateInput() {
        CronTab.validateInput();
    }

}
