package meta.helper;


@FunctionalInterface
public interface ThrowingRunnable {
    void run(String... args) throws Exception;
}
