package vt;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class MainVT {


    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();
        System.out.println("Starting...");

        //goPlatform();
        goVirtual();
        System.out.println("Finished...");
        long endTime = System.currentTimeMillis();
        long loopTime = endTime - startTime;
        System.out.println("Loop time: " + loopTime);

    }


    public static void goVirtual() {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, 10_0000).forEach(i -> {
                executor.submit(() -> {
                    Thread.sleep(Duration.ofSeconds(1));
                    return i;
                });
            });
        }  // executor.close() is called implicitly, and waits
    }


    public static void goPlatform() {
        try (var executor = Executors.newFixedThreadPool(200)) {
            IntStream.range(0, 10_000).forEach(i -> {
                executor.submit(() -> {
                    Thread.sleep(Duration.ofSeconds(1));
                    return i;
                });
            });
        }  // executor.close() is called implicitly, and waits
    }


}
