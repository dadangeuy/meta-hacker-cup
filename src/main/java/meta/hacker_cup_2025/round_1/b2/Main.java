package meta.hacker_cup_2025.round_1.b2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

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

            out.format("Case #%d: %d\n", output.caseId(), output.permutations());
        }

        in.close();
        out.flush();
        out.close();
    }
}

record Input(int caseId, long n, long a, long b) {
}

record Output(int caseId, long permutations) {
}

class Process {
    private static final long MODULO = 1000000000 + 7;
    private static final Map<Long, Map<Long, Long>> CACHE_COUNT_PARTITION = new HashMap<>();

    public Output process(final Input input) {
        // find pairs of numbers with first <= A and first * second = B
        final var pairLL = new LinkedList<long[]>();
        for (long first = 1L; first <= input.a() && first * first <= input.b(); first++) {
            final long second = input.b() / first;
            if (first * second != input.b()) continue;

            pairLL.addLast(new long[]{first, second});
            if (second <= input.a() && first != second) {
                pairLL.addLast(new long[]{second, first});
            }
        }
        final var pairs = new ArrayList<>(pairLL);

        long totalCounts = 0L;
        for (final var pair : pairs) {
            final long count1 = countPartition(pair[0], input.n());
            final long count2 = countPartition(pair[1], input.n());

            totalCounts += (count1 * count2) % MODULO;
            totalCounts %= MODULO;
        }


        return new Output(input.caseId(), totalCounts);
    }

    private long countPartition(long number, long partition) {
        if (partition == 1) return 1;
        if (number == 1) return 1;

        final var isCached = CACHE_COUNT_PARTITION.containsKey(number) &&
                             CACHE_COUNT_PARTITION.get(number).containsKey(partition);
        if (isCached) {
            return CACHE_COUNT_PARTITION.get(number).get(partition);
        }

        var count = 0L;
        for (var factor1 = 1L; factor1 * factor1 <= number; factor1++) {
            final var factor2 = number / factor1;
            if (factor1 * factor2 != number) continue;

            count += countPartition(factor1, partition - 1);
            count %= MODULO;
            if (factor1 != factor2) {
                count += countPartition(factor2, partition - 1);
                count %= MODULO;
            }
        }

        CACHE_COUNT_PARTITION.computeIfAbsent(number, _ -> new HashMap<>()).put(partition, count);
        return count;
    }
}
