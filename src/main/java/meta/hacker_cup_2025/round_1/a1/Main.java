package meta.hacker_cup_2025.round_1.a1;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
    static void main(final String... args) {
        final var in = new Scanner(new BufferedInputStream(System.in, 1 << 8));
        final var out = new PrintWriter(new BufferedOutputStream(System.out, 1 << 8));
        final var process = new Process();

        final var totalCases = in.nextInt();
        for (var i = 0; i < totalCases; i++) {
            final var totalPlatforms = in.nextInt();
            final var platforms = new int[totalPlatforms];
            for (var j = 0; j < totalPlatforms; j++) {
                final var platform = in.nextInt();
                platforms[j] = platform;
            }

            final var input = new Input(i + 1, totalPlatforms, platforms);
            final var output = process.process(input);

            out.format("Case #%d: %d\n", output.caseId(), output.minimumHeight());
        }

        in.close();
        out.flush();
        out.close();
    }
}

record Input(int caseId, int totalPlatforms, int[] platforms) {
}

record Output(int caseId, int minimumHeight) {
}

class Process {
    public Output process(final Input input) {
        var maxHeight = 0;
        for (int i = 0, j = 1; j < input.totalPlatforms(); i++, j++) {
            var height = Math.abs(input.platforms()[i] - input.platforms()[j]);
            maxHeight = Math.max(maxHeight, height);
        }

        return new Output(input.caseId(), maxHeight);
    }
}
