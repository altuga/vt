package async;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Three {

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {

        ExecutorService executorServiceTwo = Executors.newFixedThreadPool(3);
        CompletableFuture<Integer> cf1 =
                CompletableFuture.supplyAsync(() -> {

                            return process(1);
                        });

        CompletableFuture<Integer> cf2 =
                CompletableFuture.supplyAsync(() -> {
                    return process(2);
                });


        CompletableFuture<Object> result = CompletableFuture.anyOf(cf1, cf2); // signals that it is finished
        result.thenAccept(System.out::println).

                join();
        executorServiceTwo.shutdown();

        System.out.println("app is finished ");
    }

    public static int process(int value) {
        try {
            Thread.sleep(1000);
            System.out.println("Thread " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return value;
    }

}
