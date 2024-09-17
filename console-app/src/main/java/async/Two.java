package async;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Two {

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {

        ExecutorService executorServiceTwo = Executors.newFixedThreadPool(4);
        CompletableFuture<Integer> cf1 =
                CompletableFuture.supplyAsync(() -> {

                            throw new RuntimeException("hata");
                        },
                        executorServiceTwo);

        CompletableFuture<Integer> cf2 =
                CompletableFuture.supplyAsync(() -> {
                    return process(2);
                }, executorServiceTwo);


        CompletableFuture<Void> result = CompletableFuture.allOf(cf1, cf2); // signals that it is finished
        result.join();
        // in order to show the error message in the thread
        //result.thenAccept(System.out::println).join();
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
