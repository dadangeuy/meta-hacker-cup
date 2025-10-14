package dev.rizaldi.meta_hacker_cup.helper;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

public class TestHelper {
    public static void run(
            final ThrowingRunnable main,
            final String... caseIds
    ) throws Exception {
        final File directory = getDirectory(main.getClass());

        for (final String caseId : caseIds) {
            final File input = new File(directory, caseId + ".i");
            final File output = new File(directory, caseId + ".o");
            final File result = new File(directory, caseId + ".r");

            final InputStream inputStream = new FileInputStream(input);
            final PrintStream resultStream = new PrintStream(result);

            System.setIn(inputStream);
            System.setOut(resultStream);

            main.run();

            final boolean match = FileUtils.contentEqualsIgnoreEOL(output, result, null);
            Assertions.assertTrue(match);
        }
    }

    public static File getDirectory(
            final Class<?> klass
    ) {
        final String packageName = klass.getPackage().getName();
        final String path = packageName
                .replace("dev.rizaldi.meta_hacker_cup", "")
                .replaceAll("\\.", "/");
        try {
            final URL testURL = Objects.requireNonNull(klass.getResource(path));
            final URI testURI = testURL.toURI();
            return new File(testURI);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}