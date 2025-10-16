package meta.hacker_cup_2025.practice_round.d;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Stream;

/**
 * <p>Problem D: Plan Out</p>
 * <p>25 points</p>
 * <p>Verdict: Wrong Answer</p>
 * <p>Link: <a href="https://www.facebook.com/codingcompetitions/hacker-cup/2025/practice-round/problems/D">Meta Hacker Cup</a></p>
 */
public class Main {
    static void main(final String... args) {
        final var in = new Scanner(new BufferedInputStream(System.in, 1 << 8));
        final var out = new PrintWriter(new BufferedOutputStream(System.out, 1 << 8));
        final var process = new Process();

        final var totalCases = in.nextInt();
        for (int i = 0; i < totalCases; i++) {
            final var totalCoders = in.nextInt();
            final var totalActivities = in.nextInt();
            final var activities = new int[totalActivities][];
            for (int j = 0; j < totalActivities; j++) {
                final var coder1 = in.nextInt();
                final var coder2 = in.nextInt();
                final var activity = new int[]{coder1, coder2};
                activities[j] = activity;
            }

            final var input = new Input(i + 1, totalCoders, totalActivities, activities);
            final var output = process.process(input);

            out.format("Case #%d: %d ", output.caseId(), output.minimumTotalCosts());
            for (var day : output.dayPerActivity()) out.print(day);
            out.print('\n');
        }

        in.close();
        out.flush();
        out.close();
    }
}

record Input(int caseId, int totalCoders, int totalActivities, int[][] activities) {
}

record Output(int caseId, long minimumTotalCosts, int[] dayPerActivity) {
}

class Process {
    public Output process(final Input input) {
        final var activities = new ArrayList<Activity>(input.totalActivities());
        for (var id = 0; id < input.totalActivities(); id++) {
            final var coders = input.activities()[id];
            final var activity = new Activity(id, coders);
            activities.add(activity);
        }

        final var totalActivitiesPerCoder = new HashMap<Integer, Integer>();
        for (final var activity : activities) {
            for (final var coder : activity.coders()) {
                totalActivitiesPerCoder.computeIfPresent(coder, (_, v) -> v + 1);
                totalActivitiesPerCoder.putIfAbsent(coder, 1);
            }
        }

        var agenda0 = new Agenda();
        var agenda1 = new Agenda();

        for (final var activity : activities) {
            Agenda cheapest0 = null;
            Agenda cheapest1 = null;

            for (int day = 0; day < 2; day++) {
                final var option0 = agenda0.copy();
                final var option1 = agenda1.copy();

                if (day == 0) {
                    option0.dayOneAgenda.add(activity);
                    option1.dayOneAgenda.add(activity);
                    cheapest0 = Stream.of(option0, option1).min(Agenda.ORDER_BY_TOTAL_COSTS).orElseThrow();
                } else {
                    option0.dayTwoAgenda.add(activity);
                    option1.dayTwoAgenda.add(activity);
                    cheapest1 = Stream.of(option0, option1).min(Agenda.ORDER_BY_TOTAL_COSTS).orElseThrow();
                }
            }

            agenda0 = cheapest0;
            agenda1 = cheapest1;
        }

        final var cheapestAgenda = Stream.of(agenda0, agenda1).min(Agenda.ORDER_BY_TOTAL_COSTS).orElseThrow();
        final var dayPerActivities = activities.stream()
                .mapToInt(a -> cheapestAgenda.dayOneAgenda.activities.contains(a) ? 1 : 2)
                .toArray();

        return new Output(input.caseId(), cheapestAgenda.totalCosts(), dayPerActivities);
    }
}

record Activity(int id, int[] coders) {
}

class Agenda {
    public static final Comparator<Agenda> ORDER_BY_TOTAL_COSTS = Comparator.comparingLong(Agenda::totalCosts);

    public final DailyAgenda dayOneAgenda;
    public final DailyAgenda dayTwoAgenda;

    public Agenda() {
        dayOneAgenda = new DailyAgenda();
        dayTwoAgenda = new DailyAgenda();
    }

    public Agenda(final DailyAgenda dayOneAgenda, final DailyAgenda dayTwoAgenda) {
        this.dayOneAgenda = dayOneAgenda;
        this.dayTwoAgenda = dayTwoAgenda;
    }


    public Agenda copy() {
        return new Agenda(dayOneAgenda.copy(), dayTwoAgenda.copy());
    }

    public long totalCosts() {
        return dayOneAgenda.totalCosts() + dayTwoAgenda.totalCosts();
    }
}

class DailyAgenda {
    public Set<Activity> activities;
    public Map<Integer, Integer> totalActivitiesPerCoders;

    public DailyAgenda() {
        activities = new HashSet<>();
        totalActivitiesPerCoders = new HashMap<>();
    }

    public DailyAgenda(
            final Set<Activity> activities,
            final Map<Integer, Integer> totalActivitiesPerCoders
    ) {
        this.activities = activities;
        this.totalActivitiesPerCoders = totalActivitiesPerCoders;
    }

    public DailyAgenda copy() {
        return new DailyAgenda(
                new HashSet<>(activities),
                new HashMap<>(totalActivitiesPerCoders)
        );
    }

    public void add(final Activity activity) {
        for (final var coder : activity.coders()) {
            final var oldTotalActivities = totalActivitiesPerCoders.getOrDefault(coder, 0);
            final var newTotalActivities = oldTotalActivities + 1;
            totalActivitiesPerCoders.put(coder, newTotalActivities);
        }
        activities.add(activity);
    }

    public long totalCosts() {
        return totalActivitiesPerCoders.values().stream()
                .mapToLong(i -> i)
                .map(i -> i * i)
                .sum();
    }
}
