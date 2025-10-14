package dev.rizaldi.meta_hacker_cup._2025.practice_round.a;

import dev.rizaldi.meta_hacker_cup.helper.TestHelper;
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
    public void warm_up() throws Exception {
        TestHelper.run(Main::main, "warm_up");
    }
}
