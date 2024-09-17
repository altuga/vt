package intro;

import java.util.concurrent.*;

public class GetDataConcurrentWithJoin {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        long startTime = System.currentTimeMillis();

        // Define tasks to retrieve data from sources
        Callable<Long> source1Task = GetDataConcurrentWithJoin::retrieveDataFromSource1;
        Callable<Long> source2Task = GetDataConcurrentWithJoin::retrieveDataFromSource2;

        // Create and start threads
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<Long> data1Future = executor.submit(source1Task);
        Future<Long> data2Future = executor.submit(source2Task);

        // Wait for both threads to finish using join and get data
        long data1 = data1Future.get();
        long data2 = data2Future.get();

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        // Process data (simulated here by addition)
        long sum = data1 + data2;
        System.out.println("Data from source 1: " + data1);
        System.out.println("Data from source 2: " + data2);
        System.out.println("Sum: " + sum);
        System.out.println("Total time spent retrieving data: " + totalTime + " ms");

        // Shutdown the executor service
        executor.shutdown();
    }

    // Simulate data retrieval from source 1 (replace with actual logic)
    private static long retrieveDataFromSource1() throws InterruptedException {
        Thread.sleep(5000); // Simulate some work (5 seconds)
        return 5;
    }

    // Simulate data retrieval from source 2 (replace with actual logic)
    private static long retrieveDataFromSource2() throws InterruptedException {
        Thread.sleep(8000); // Simulate some work (8 seconds)
        return 8;
    }
}
