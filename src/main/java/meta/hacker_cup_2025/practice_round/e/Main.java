package meta.hacker_cup_2025.practice_round.e;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;

/**
 * <p>Problem E: Pay Off</p>
 * <p>35 points</p>
 * <p>Verdict: Wrong Answer</p>
 * <p>Link: <a href="https://www.facebook.com/codingcompetitions/hacker-cup/2025/practice-round/problems/E">Meta Hacker Cup</a></p>
 */
public class Main {
    static void main(final String... args) {
        final var in = new Scanner(new BufferedInputStream(System.in, 1 << 8));
        final var out = new PrintWriter(new BufferedOutputStream(System.out, 1 << 8));
        final var process = new Process();

        final var totalCases = in.nextInt();
        for (var i = 0; i < totalCases; i++) {
            final var totalRobots = in.nextInt();
            final var totalOperations = in.nextInt();
            final var maxLength = in.nextInt();

            final var robots = new int[totalRobots];
            for (var j = 0; j < totalRobots; j++) {
                robots[j] = in.nextInt();
            }

            final var operations = new int[totalOperations][];
            for (var j = 0; j < totalOperations; j++) {
                final var operationType = in.nextInt();
                if (operationType == 1) {
                    final var point = in.nextInt();
                    final var operation = new int[]{operationType, point};
                    operations[j] = operation;
                } else {
                    final var robotId = in.nextInt();
                    final var totalSeconds = in.nextInt();
                    final var operation = new int[]{operationType, robotId, totalSeconds};
                    operations[j] = operation;
                }
            }

            final var input = new Input(i + 1, totalRobots, totalOperations, maxLength, robots, operations);
            final var output = process.process(input);

            out.format("Case #%d: %d\n", output.caseId(), output.answerSum());
        }

        in.close();
        out.flush();
        out.close();
    }
}

record Input(int caseId, int totalRobots, int totalOperations, int maxLength, int[] robots, int[][] operations) {
}

record Output(int caseId, int answerSum) {
}

class Process {
    private static final Comparator<Wall> ORDER_WALL_BY_POSITION = Comparator.comparingInt(Wall::position);
    private static final Comparator<Robot> ORDER_ROBOT_BY_POSITION = Comparator.comparingInt(Robot::position);

    public Output process(final Input input) {
        final var walls = new ArrayList<Wall>();
        walls.add(new Wall(walls.size(), 1));
        walls.add(new Wall(walls.size(), input.maxLength()));

        final var robots = new ArrayList<Robot>();
        for (int i = 0; i < input.robots().length; i++) {
            final var position = input.robots()[i];
            final var robot = new Robot(i + 1, position);
            robots.add(robot);
        }

        final var sortedWalls = new TreeSet<>(ORDER_WALL_BY_POSITION);
        sortedWalls.addAll(walls);

        final var sortedRobots = new TreeSet<>(ORDER_ROBOT_BY_POSITION);
        sortedRobots.addAll(robots);

        var answerSum = 0;
        for (final var operation : input.operations()) {
            final var operationType = operation[0];
            if (operationType == 1) {
                addWall(walls, sortedWalls, operation[1]);
            } else if (operationType == 2) {
                final var answer = runSimulation(
                        operation[1],
                        operation[2],
                        walls,
                        sortedWalls,
                        robots,
                        sortedRobots
                );
                answerSum += answer;
            }
        }

        return new Output(input.caseId(), answerSum);
    }

    private void addWall(final List<Wall> walls, final TreeSet<Wall> sortedWalls, final int point) {
        final var wall = new Wall(walls.size(), point);
        walls.add(wall);
        sortedWalls.add(wall);
    }

    private int runSimulation(
            final int robotId,
            final int totalSeconds,
            final List<Wall> walls,
            final TreeSet<Wall> sortedWalls,
            final List<Robot> robots,
            final TreeSet<Robot> sortedRobots
    ) {
        var mainRobot = robots.get(robotId - 1);

        final var leftWall = sortedWalls.floor(new Wall(0, mainRobot.position()));
        final var rightWall = sortedWalls.ceiling(new Wall(0, mainRobot.position()));

        final var relevantRobots = sortedRobots.subSet(
                new Robot(0, leftWall.position()),
                true,
                new Robot(0, rightWall.position()),
                false
        );
        final var otherRobots = relevantRobots.stream()
                .filter(r -> r.id() != mainRobot.id())
                .toList();

        for (int i = totalSeconds; i >= 0; i--) {
            final var totalSimulationSeconds = i;
            final var movedMainRobot = runSimulation(leftWall, rightWall, mainRobot, totalSimulationSeconds);
            final var movedOtherRobots = otherRobots.stream()
                    .map(r -> runSimulation(leftWall, rightWall, r, totalSimulationSeconds))
                    .toList();
            final var lastCollisionRobot = movedOtherRobots.stream()
                    .filter(o -> o.id() > movedMainRobot.id())
                    .filter(o -> o.position() == movedMainRobot.position())
                    .findFirst();
            if (lastCollisionRobot.isPresent()) {
                return lastCollisionRobot.get().id();
            }
        }

        return 0;
    }

    private Robot runSimulation(
            final Wall leftWall,
            final Wall rightWall,
            final Robot robot,
            final int totalSeconds
    ) {
        var movedRobot = robot;

        final var wallDistance = rightWall.position() - leftWall.position();
        final var leftWallDistance = movedRobot.position() - leftWall.position();
        if (totalSeconds < leftWallDistance) {
            // there's no wall hit
            movedRobot = movedRobot.move(-totalSeconds);
        } else {
            // hit left wall
            movedRobot = movedRobot.move(-leftWallDistance);
            var remainingSeconds = totalSeconds - leftWallDistance;
            var totalTrip = remainingSeconds / wallDistance;
            if (totalTrip % 2 == 0) {
                var remainingTrip = remainingSeconds % wallDistance;
                movedRobot = movedRobot.move(remainingTrip);
            } else {
                var remainingTrip = remainingSeconds % wallDistance;
                movedRobot = movedRobot
                        .set(rightWall.position())
                        .move(-remainingTrip);
            }
        }

        return movedRobot;
    }
}

record Robot(int id, int position) {
    public Robot set(int position) {
        return new Robot(id, position);
    }

    public Robot move(int move) {
        return new Robot(id, position + move);
    }
}

record Wall(int id, int position) {
}
