package meta.hacker_cup_2025.round_1.b1;

import meta.helper.TestHelper;
import org.junit.jupiter.api.Test;

public class MainTest {
    @Test
    public void sample() throws Exception {
        TestHelper.run(Main::main, "sample");
    }

    @Test
    public void validation() throws Exception {
        TestHelper.run(Main::main, "final_product_chapter_1_validation_input");
    }

    @Test
    public void full_input() throws Exception {
        TestHelper.run(Main::main, "final_product_chapter_1_input");
    }
}
