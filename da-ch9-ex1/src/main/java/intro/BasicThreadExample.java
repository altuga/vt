package intro;


import java.util.concurrent.ThreadLocalRandom;

public class BasicThreadExample {

    public static void main(String[] args) throws InterruptedException {

        // Define the duration (in milliseconds) for the main thread to run
        int mainThreadDuration = 2000;

        // Create and start five worker threads
        for (int i = 0; i < 5; i++) {
            Thread workerThread = new Thread(new LongRunningTask());
            workerThread.setName("Worker Thread " + (i + 1));
            workerThread.start();
        }

        // Let the main thread sleep for a shorter duration
        System.out.println("Main thread started");
        Thread.sleep(mainThreadDuration);
        System.out.println("Main thread finished after " + mainThreadDuration + " milliseconds");
    }
}

class LongRunningTask implements Runnable {

    @Override
    public void run() {
        try {
            // Simulate work by sleeping for a longer duration
            Thread.sleep(ThreadLocalRandom.current().nextInt(3000, 5000));
            System.out.println(Thread.currentThread().getName() + " finished");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
