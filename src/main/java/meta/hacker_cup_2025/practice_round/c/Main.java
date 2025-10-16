package meta.hacker_cup_2025.practice_round.c;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * <p>Problem C: Monkey Around</p>
 * <p>15 points</p>
 * <p>Verdict: Accepted</p>
 * <p>Link: <a href="https://www.facebook.com/codingcompetitions/hacker-cup/2025/practice-round/problems/C">Meta Hacker Cup</a></p>
 */
public class Main {
    static void main(final String... args) {
        final var in = new Scanner(new BufferedInputStream(System.in, 1 << 8));
        final var out = new PrintWriter(new BufferedOutputStream(System.out, 1 << 8));
        final var process = new Process();

        final var totalCases = in.nextInt();
        for (var i = 0; i < totalCases; i++) {
            final var length = in.nextInt();
            final var array = new int[length];
            for (var j = 0; j < length; j++) {
                final var value = in.nextInt();
                array[j] = value;
            }

            final var input = new Input(i + 1, length, array);
            final var output = process.process(input);

            out.format("Case #%d: %d\n", output.caseId(), output.totalOperations());
            for (int[] operation : output.operations()) {
                if (operation.length == 1) {
                    out.format("%d\n", operation[0]);
                } else if (operation.length == 2) {
                    out.format("%d %d\n", operation[0], operation[1]);
                }
            }
        }

        in.close();
        out.flush();
        out.close();
    }
}

record Input(int caseId, int length, int[] array) {
}

record Output(int caseId, int totalOperations, int[][] operations) {
}

class Process {
    public Output process(final Input input) {
        final var operations = new LinkedList<int[]>();

        final var parititions = findPartitions(input.array());
        var totalRotations = 0;
        for (int i = parititions.length - 1; i >= 0; i--) {
            final var partition = parititions[i];

            rotateRight(partition, totalRotations);
            final var rotation = findRotation(partition);
            totalRotations += rotation;
            rotateRight(partition, rotation);

            for (int j = 0; j < rotation; j++) {
                operations.addFirst(new int[]{2});
            }


            final var max = findMax(partition);
            final var operation = new int[]{1, max};
            operations.addFirst(operation);
        }


        return new Output(input.caseId(), operations.size(), operations.toArray(new int[0][0]));
    }

    private int[][] findPartitions(final int[] array) {
        final var partitions = new LinkedList<int[]>();

        for (var from = 0; from < array.length; ) {
            var length = findIncreasingLength(array, from, Integer.MAX_VALUE);
            if (array[from] != 1) {
                var length2 = findIncreasingLength(array, from + length, array[from] - 1);
                length += length2;
            }

            final var partition = Arrays.copyOfRange(array, from, from + length);
            partitions.addLast(partition);

            from = from + length;
        }

        return partitions.toArray(new int[0][0]);
    }

    private int findIncreasingLength(final int[] array, final int startedAt, final int max) {
        var finishedAt = startedAt + 1;

        while (true) {
            final var outsideBoundaries = finishedAt >= array.length;
            if (outsideBoundaries) break;

            final var isIncreasing = array[finishedAt] == array[finishedAt - 1] + 1;
            if (!isIncreasing) break;

            final var isExceedingMax = array[finishedAt] > max;
            if (isExceedingMax) break;

            finishedAt++;
        }

        return finishedAt - startedAt;
    }

    private int findRotation(final int[] partition) {
        int index1 = 0;
        while (partition[index1] != 1) index1++;
        return (partition.length - index1) % partition.length;
    }

    private void rotateRight(final int[] partition, final int rotation) {
        final int[] partition2 = new int[partition.length];
        for (
                int i = 0, j = (partition.length - (rotation % partition.length)) % partition.length;
                i < partition.length;
                i++, j = (j + 1) % partition.length
        ) {
            partition2[i] = partition[j];
        }

        for (int i = 0; i < partition.length; i++) {
            partition[i] = partition2[i];
        }
    }

    private int findMax(final int[] partition) {
        return Arrays.stream(partition).max().orElse(partition[0]);
    }
}
