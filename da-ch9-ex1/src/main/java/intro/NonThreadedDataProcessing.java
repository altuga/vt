package intro;

public class NonThreadedDataProcessing {

    public static void main(String[] args) throws InterruptedException {

        long startTime = System.currentTimeMillis();

        // Data retrieval simulations (replace with actual logic)
        String data1 = retrieveDataFromSource1(5000);
        String data2 = retrieveDataFromSource2(8000);

        long retrievalTime = System.currentTimeMillis() - startTime;

        System.out.println("Data retrieved from both sources in: " + retrievalTime + " milliseconds (simulated)");

        // Your data processing logic using data1 and data2 goes here
        processData(data1, data2);
    }

    private static String retrieveDataFromSource1(int delay) throws InterruptedException {
        System.out.println("Retrieving data from source 1 (simulated " + delay + " milliseconds)");
        Thread.sleep(delay);
        // Simulate data retrieval from source 1 (replace with your actual logic)
        return "Data from source 1";
    }

    private static String retrieveDataFromSource2(int delay) throws InterruptedException {
        System.out.println("Retrieving data from source 2 (simulated " + delay + " milliseconds)");
        Thread.sleep(delay);
        // Simulate data retrieval from source 2 (replace with your actual logic)
        return "Data from source 2";
    }

    private static void processData(String data1, String data2) {
        // Your data processing logic using retrieved data
        System.out.println("Processing data from both sources...");
        // ... (your processing logic here)
    }
}
