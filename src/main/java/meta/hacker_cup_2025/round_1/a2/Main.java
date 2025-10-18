package meta.hacker_cup_2025.round_1.a2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
        final var ladders = new ArrayList<Ladder>();
        for (int i = 0; i < input.totalPlatforms(); i++) {
            final var height = input.platforms()[i];
            final var ladder = new Ladder(Ladder.GROUND_PLATFORM, i, height);
            ladders.add(ladder);
        }
        for (int i = 0, j = 1; j < input.totalPlatforms(); i++, j++) {
            final var height1 = input.platforms()[i];
            final var height2 = input.platforms()[j];
            final var height = Math.abs(height1 - height2);
            final var ladder = new Ladder(i, j, height);
            ladders.add(ladder);
        }

        ladders.sort(Ladder.ORDER_BY_HEIGHT);

        final var disjointSet = new DisjointSet<Integer>();
        for (int platform = -1; platform < input.totalPlatforms(); platform++) {
            disjointSet.union(platform, platform);
        }

        var ladderHeight = 0;

        for (final var ladder : ladders) {
            final var isFromGrounded = disjointSet.find(Ladder.GROUND_PLATFORM).equals(disjointSet.find(ladder.fromPlatform));
            final var isIntoGrounded = disjointSet.find(Ladder.GROUND_PLATFORM).equals(disjointSet.find(ladder.intoPlatform));
            final var isGrounded = isFromGrounded && isIntoGrounded;
            if (isGrounded) continue;

            disjointSet.union(ladder.fromPlatform, ladder.intoPlatform);
            ladderHeight = Math.max(ladderHeight, ladder.height);
        }

        return new Output(input.caseId(), ladderHeight);
    }
}

class Ladder {
    public static final Comparator<Ladder> ORDER_BY_HEIGHT = Comparator.comparingInt(l -> l.height);
    public static final int GROUND_PLATFORM = -1;

    public final int fromPlatform;
    public final int intoPlatform;
    public final int height;

    public Ladder(final int fromPlatform, final int intoPlatform, final int height) {
        this.fromPlatform = fromPlatform;
        this.intoPlatform = intoPlatform;
        this.height = height;
    }
}

class DisjointSet<V> {
    public final Map<V, V> parents = new HashMap<>();

    public V find(final V item) {
        final V parent = parents.getOrDefault(item, item);
        if (parent != item) {
            final V root = find(parent);
            parents.put(item, root);
            return root;
        }
        return parent;
    }

    public void union(final V item1, final V item2) {
        final V parent1 = find(item1);
        final V parent2 = find(item2);
        parents.put(parent2, parent1);
    }
}
