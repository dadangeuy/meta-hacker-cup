package dev.rizaldi.meta_hacker_cup.helper;


@FunctionalInterface
public interface ThrowingRunnable {
    void run(String... args) throws Exception;
}
