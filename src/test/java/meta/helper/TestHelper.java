package meta.helper;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.util.Objects;

public class TestHelper {
    public static void run(
            final ThrowingRunnable main,
            final String... caseIds
    ) throws Exception {
        final var directory = getDirectory(main.getClass());

        for (final var caseId : caseIds) {
            final var input = new File(directory, caseId + ".i");
            final var output = new File(directory, caseId + ".o");
            final var result = new File(directory, caseId + ".r");

            final var inputStream = new FileInputStream(input);
            final var resultStream = new PrintStream(result);

            System.setIn(inputStream);
            System.setOut(resultStream);

            main.run();

            final var match = FileUtils.contentEqualsIgnoreEOL(output, result, null);
            Assertions.assertTrue(match);
        }
    }

    public static File getDirectory(
            final Class<?> klass
    ) throws URISyntaxException {
        final var packageName = klass.getPackage().getName();
        final var packagePath = packageName.replaceAll("\\.", "/");
        final var testCasePath = packagePath.replaceFirst("meta/", "/testcase/");

        final var testCaseURL = Objects.requireNonNull(klass.getResource(testCasePath));
        final var testCaseURI = testCaseURL.toURI();
        return new File(testCaseURI);
    }
}