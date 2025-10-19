package meta.hacker_cup_2025.round_1.b1;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Main {
    static void main(final String... args) {
        final var in = new Scanner(new BufferedInputStream(System.in, 1 << 8));
        final var out = new PrintWriter(new BufferedOutputStream(System.out, 1 << 8));
        final var process = new Process();

        final var totalCases = in.nextInt();
        for (var i = 0; i < totalCases; i++) {
            final var n = in.nextInt();
            final var a = in.nextInt();
            final var b = in.nextInt();

            final var input = new Input(i + 1, n, a, b);
            final var output = process.process(input);

            out.format("Case #%d:", output.caseId());
            for (final var change : output.changes()) out.format(" %d", change);
            out.println();
        }

        in.close();
        out.flush();
        out.close();
    }
}

record Input(int caseId, int n, int a, int b) {
}

record Output(int caseId, int[] changes) {
}

class Process {
    private static final int[] PRIMES = getPrimes(100);

    public Output process(final Input input) {
        final int totalDays = 2 * input.n();
        final var changes = new int[totalDays];
        for (int day = 0; day < totalDays - 1; day++) changes[day] = 1;
        changes[totalDays - 1] = input.b();
        return new Output(input.caseId(), changes);
    }

    private int[] getFactors(final int number) {
        final var factors = new LinkedList<Integer>();
        for (var prime : PRIMES) {
            if (prime * prime > number) break;
            if (number % prime != 0) break;

            var copy = number;
            while (copy % prime == 0) {
                factors.add(prime);
                copy = copy / prime;
            }
        }

        return factors.stream().mapToInt(i -> i).toArray();
    }

    private static int[] getPrimes(final int max) {
        final var isPrimes = new boolean[max + 1];
        Arrays.fill(isPrimes, true);

        isPrimes[0] = false;
        for (var i = 2; i <= max; i++) {
            if (!isPrimes[i]) continue;
            for (var j = i + i; j <= max; j += i) {
                isPrimes[j] = false;
            }
        }

        return IntStream.rangeClosed(1, max)
            .filter(i -> isPrimes[i])
            .toArray();
    }
}
