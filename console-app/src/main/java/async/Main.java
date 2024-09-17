package async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        work04();
    }


    // combining the Future combine example
    public static void work04() throws InterruptedException, ExecutionException {
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> 10) ;
        CompletableFuture<Integer> completableFuture2= CompletableFuture.supplyAsync(() -> 20);

        completableFuture1 = completableFuture1.thenApply(f-> f * 5);
        completableFuture1 = completableFuture1.thenCombine(completableFuture2, Integer::sum);
        completableFuture1.thenRun(() -> {
            System.out.println("Thread Name -> "  +  Thread.currentThread().getName() ); 
        }); // side-effect
        Integer result = completableFuture1.get();
        System.out.println("Result : " + result );
    }

    // CompletableFuture thenApply
    public static void work03() throws InterruptedException, ExecutionException {
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> 10);
        completableFuture = completableFuture.thenApply(f -> f + 1).thenApply(f -> f + 2);
        completableFuture.thenRun(() -> {
            System.out.println("After multiplication: hello" ); 
        }); // side-effect

        System.out.println("Final result: " + completableFuture.get()); // this will print 13
    }
    
    public static void work02() {

        CompletableFuture<Object> completableFuture = new CompletableFuture<>();
        completableFuture.join();

    }

    public static void work01() {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        Future<Integer> future = executorService.submit(() -> 42);
        System.out.println(future.cancel(false));
    }

}
