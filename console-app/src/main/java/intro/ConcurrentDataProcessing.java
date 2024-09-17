package intro;

public class ConcurrentDataProcessing {

    public static void main(String[] args) throws InterruptedException {


        long startTime = System.currentTimeMillis();

        // Define tasks to retrieve data from each source
        Runnable source1Task = () -> {
            System.out.println("Retrieving data from source 1 (5 seconds)");
            try {
                Thread.sleep(5000); // Simulate 5 seconds of data retrieval
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Store retrieved data from source 1 (replace with your actual logic)
            System.out.println("Data from source 1");
        };

        Runnable source2Task = () -> {
            System.out.println("Retrieving data from source 2 (8 seconds)");
            try {
                Thread.sleep(8000); // Simulate 8 seconds of data retrieval
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Store retrieved data from source 2 (replace with your actual logic)
            System.out.println("Data from source 2");
        };

        // Create threads for each task
        Thread thread1 = new Thread(source1Task);
        Thread thread2 = new Thread(source2Task);

        // Start both threads concurrently
        thread1.start();
        thread2.start();

        // Wait for both threads to finish (optional)
        thread1.join();
        thread2.join();

        // Now you have data from both sources and can proceed with processing
        System.out.println("Data retrieved from both sources. Processing can begin.");
        // Your data processing logic using data1 and data2 goes here

        long retrievalTime = System.currentTimeMillis() - startTime;

        System.out.println("Data retrieved from both sources in: " + retrievalTime + " milliseconds (simulated)");
    }
}
