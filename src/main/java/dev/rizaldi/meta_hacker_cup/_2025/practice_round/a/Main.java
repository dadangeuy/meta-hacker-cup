package dev.rizaldi.meta_hacker_cup._2025.practice_round.a;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * <p>Problem A: Warm Up</p>
 * <p>12 points</p>
 * <p>Verdict: N/A</p>
 * <p>Link: <a href="https://www.facebook.com/codingcompetitions/hacker-cup/2025/practice-round/problems/A">Meta Hacker Cup</a></p>
 */
public class Main {
    static void main(final String... args) throws Exception {
        final var in = new Scanner(new BufferedInputStream(System.in, 1 << 8));
        final var out = new PrintWriter(new BufferedOutputStream(System.out, 1 << 8));
        final var process = new Process();

        final var totalCases = in.nextInt();
        for (var i = 0; i < totalCases; i++) {
            final var totalDishes = in.nextInt();
            final var fromTemperatures = new int[totalDishes];
            for (int j = 0; j < totalDishes; j++) {
                final var temperature = in.nextInt();
                fromTemperatures[j] = temperature;
            }
            final var intoTemperatures = new int[totalDishes];
            for (var j = 0; j < totalDishes; j++) {
                final var temperature = in.nextInt();
                intoTemperatures[j] = temperature;
            }

            final var input = new Input(i + 1, totalDishes, fromTemperatures, intoTemperatures);
            final var output = process.process(input);

            out.format("Case #%d: %d\n", output.caseId(), output.totalOperations());
            for (int j = 0; j < output.totalOperations(); j++) {
                final var operation = output.operations()[j];
                out.format("%d %d\n", operation[0], operation[1]);
            }
        }

        in.close();
        out.flush();
        out.close();
    }
}

record Input(int caseId, int totalDishes, int[] fromTemperatures, int[] intoTemperatures) {
}

record Output(int caseId, int totalOperations, int[][] operations) {
}

class Process {
    public Output process(final Input input) {
        final var graph = new Graph<Vertex>();

        for (var i = 0; i < input.totalDishes(); i++) {
            final var dish = new Vertex("dish", i + 1);
            final var currentTemperature = new Vertex("temperature", input.fromTemperatures()[i]);
            final var targetTemperature = new Vertex("temperature", input.intoTemperatures()[i]);

            graph.add(currentTemperature, dish);
            graph.add(dish, targetTemperature);
        }

        final var cookedDishes = new HashSet<Vertex>();
        final var operationList = new LinkedList<int[]>();

        for (var i = 0; i < input.totalDishes(); i++) {
            final var dish = new Vertex("dish", i + 1);
            final var currentTemperature = graph.getBackwards(dish).iterator().next();
            final var targetTemperature = graph.getForwards(dish).iterator().next();

            final var isCooked = currentTemperature.value() == targetTemperature.value();
            if (isCooked) {
                cookedDishes.add(dish);
                final boolean isValid = depthFirstSearch(graph, dish, dish, cookedDishes, operationList);
                if (!isValid) return new Output(input.caseId(), -1, null);
            }
        }

        if (cookedDishes.size() != input.totalDishes()) {
            return new Output(input.caseId(), -1, null);
        }
        return new Output(input.caseId(), operationList.size(), operationList.toArray(new int[0][0]));
    }

    private boolean depthFirstSearch(
            final Graph<Vertex> graph,
            final Vertex warmDish,
            final Vertex coldDish,
            final Set<Vertex> cookedDishes,
            final LinkedList<int[]> operationList
    ) {
        final var warmTemperature = graph.getBackwards(warmDish).iterator().next();
        final var coldTemperature = graph.getBackwards(coldDish).iterator().next();
        final var validTemperature = warmTemperature.value() >= coldTemperature.value();
        if (!validTemperature) {
            return false;
        }

        for (var colderDish : graph.getBackwards(coldTemperature)) {
            if (!cookedDishes.contains(colderDish)) {
                cookedDishes.add(colderDish);
                final boolean isValid = depthFirstSearch(
                        graph,
                        coldDish,
                        colderDish,
                        cookedDishes,
                        operationList
                );
                if (!isValid) {
                    return false;
                }
            }
        }

        if (warmDish != coldDish) {
            final int[] operation = new int[]{warmDish.value(), coldDish.value()};
            operationList.addLast(operation);
        }

        return true;
    }
}

record Vertex(String type, int value) {
}

class Graph<V> {
    private final Map<V, Set<V>> forwards = new HashMap<>();
    private final Map<V, Set<V>> backwards = new HashMap<>();

    public void add(final V from, final V into) {
        forwards.computeIfAbsent(from, _ -> new HashSet<>()).add(into);
        backwards.computeIfAbsent(into, _ -> new HashSet<>()).add(from);
    }

    public Set<V> getForwards(final V from) {
        return forwards.getOrDefault(from, Collections.emptySet());
    }

    public Set<V> getBackwards(final V into) {
        return backwards.getOrDefault(into, Collections.emptySet());
    }
}
