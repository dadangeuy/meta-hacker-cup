package meta.hacker_cup_2025.practice_round.c;

import meta.helper.TestHelper;
import org.junit.jupiter.api.Test;

public class MainTest {
    @Test
    public void sample() throws Exception {
        TestHelper.run(Main::main, "sample");
    }

    @Test
    public void validation() throws Exception {
        TestHelper.run(Main::main, "validation");
    }

    @Test
    public void monkey_around() throws Exception {
        TestHelper.run(Main::main, "monkey_around");
    }
}
