package meta.hacker_cup_2025.practice_round.b;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * <p>Problem B: Zone In</p>
 * <p>13 points</p>
 * <p>Verdict: N/A</p>
 * <p>Link: <a href="https://www.facebook.com/codingcompetitions/hacker-cup/2025/practice-round/problems/B">Meta Hacker Cup</a></p>
 */
public class Main {
    static void main(final String... args) {
        final var in = new Scanner(new BufferedInputStream(System.in, 1 << 8));
        final var out = new PrintWriter(new BufferedOutputStream(System.out, 1 << 8));
        final var process = new Process();

        final var totalCases = in.nextInt();
        for (var i = 0; i < totalCases; i++) {
            final var totalRows = in.nextInt();
            final var totalColumns = in.nextInt();
            final var totalSpaces = in.nextInt();
            final var grid = new char[totalRows][];
            for (var j = 0; j < totalRows; j++) {
                grid[j] = in.next().toCharArray();
            }

            final var input = new Input(i + 1, totalRows, totalColumns, totalSpaces, grid);
            final var output = process.process(input);

            out.format("Case #%d: %d\n", output.caseId(), output.largestContiguousSpace());
        }

        in.close();
        out.flush();
        out.close();
    }
}

record Input(int caseId, int totalRows, int totalColumns, int totalSpaces, char[][] grid) {
}

record Output(int caseId, int largestContiguousSpace) {
}

/**
 * idea:
 * 1. dfs until met wall, return distance to wall
 * 2. store distance to wall in a grid
 * 3. flood fill safe area
 */
class Process {
    private static final int NONE = -1;
    private static final char EMPTY_SPACE = '.';
    private static final char OBJECT = '#';
    private static final char SAFE_SPACE = '*';

    public Output process(final Input input) {
        final var distances = new int[input.totalRows()][input.totalColumns()];
        fill(distances, NONE);

        final var visited = new HashSet<Position>();
        final var queue = new LinkedList<Step>();

        for (var i = 0; i < input.totalRows(); i++) {
            for (var j = 0; j < input.totalColumns(); j++) {
                final var cell = input.grid()[i][j];
                final var isObject = cell == OBJECT;
                if (isObject) {
                    final var position = new Position(i, j);
                    final var step = new Step(position, 0);
                    visited.add(position);
                    queue.addLast(step);
                    distances[position.row()][position.column()] = 0;
                }
            }
        }

        for (var i = 0; i < input.totalRows(); i++) {
            final var positionLeft = new Position(i, -1);
            final var positionRight = new Position(i, input.totalColumns());

            visited.add(positionLeft);
            visited.add(positionRight);
            queue.addLast(new Step(positionLeft, 0));
            queue.addLast(new Step(positionRight, 0));
        }

        for (var i = 0; i < input.totalColumns(); i++) {
            final var positionUp = new Position(-1, i);
            final var positionDown = new Position(input.totalRows(), i);

            visited.add(positionUp);
            visited.add(positionDown);
            queue.addLast(new Step(positionUp, 0));
            queue.addLast(new Step(positionDown, 0));
        }

        while (!queue.isEmpty()) {
            final var step = queue.removeFirst();
            final var position = step.position();

            {
                final var validRow = 0 <= position.row() && position.row() < input.totalRows();
                final var validColumn = 0 <= position.column() && position.column() < input.totalColumns();
                if (validRow && validColumn) {
                    distances[position.row()][position.column()] = step.distance();
                }
            }

            for (final var neighbour : position.neighbours()) {
                final var validRow = 0 <= neighbour.row() && neighbour.row() < input.totalRows();
                final var validColumn = 0 <= neighbour.column() && neighbour.column() < input.totalColumns();
                if (!(validRow && validColumn)) continue;

                final var isEmptySpace = input.grid()[neighbour.row()][neighbour.column()] == EMPTY_SPACE;
                if (!isEmptySpace) continue;

                final var isVisited = visited.contains(neighbour);
                if (isVisited) continue;

                visited.add(neighbour);
                queue.addLast(new Step(neighbour, step.distance() + 1));
            }
        }

        int maxCount = 0;
        queue.clear();
        visited.clear();

        for (int i = 0; i < input.totalRows(); i++) {
            for (int j = 0; j < input.totalColumns(); j++) {
                {
                    final int distance = distances[i][j];
                    final boolean isSafe = distance > input.totalSpaces();
                    if (!isSafe) continue;
                }

                int count = 0;
                {
                    final var position = new Position(i, j);
                    final var step = new Step(position, 0);

                    visited.add(position);
                    queue.addLast(step);
                    count++;
                }

                while (!queue.isEmpty()) {
                    final var step = queue.removeFirst();
                    final var position = step.position();
                    input.grid()[position.row()][position.column()] = SAFE_SPACE;

                    for (final var neighbour : position.neighbours()) {
                        final var validRow = 0 <= neighbour.row() && neighbour.row() < input.totalRows();
                        final var validColumn = 0 <= neighbour.column() && neighbour.column() < input.totalColumns();
                        if (!(validRow && validColumn)) continue;

                        final var isVisited = visited.contains(neighbour);
                        if (isVisited) continue;

                        final var distance = distances[neighbour.row()][neighbour.column()];
                        final var isSafe = distance > input.totalSpaces();
                        if (!isSafe) continue;

                        visited.add(neighbour);
                        queue.addLast(new Step(neighbour, step.distance() + 1));
                        count++;
                    }
                }

                maxCount = Math.max(maxCount, count);
            }
        }

        return new Output(input.caseId(), maxCount);
    }

    private void fill(final int[][] array, final int fill) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                array[i][j] = fill;
            }
        }
    }
}

record Position(int row, int column) {
    public Position[] neighbours() {
        return new Position[]{
                new Position(row - 1, column),
                new Position(row + 1, column),
                new Position(row, column - 1),
                new Position(row, column + 1)
        };
    }
}

record Step(Position position, int distance) {
}
