package async;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class One {

    private static final String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CompletableFuture<Integer> cf1 = CompletableFuture.supplyAsync(() -> findTagsFromStackoverflow("java"), executorService);
        CompletableFuture<Integer> cf2 = CompletableFuture.supplyAsync(() -> findTagsFromStackoverflow("linux"), executorService);

        //cf1.get(); // blocking Thread
        CompletableFuture<Void> result = CompletableFuture.allOf(cf1, cf2); // signals that it is finished
        result.join();
        executorService.shutdown();

        System.out.println("app is finished ");
    }

    public static int process(String value) {
        try {
            Thread.sleep(1000); // I/O operation
            System.out.println("Thread " +  value + " -> "+Thread.currentThread().getName());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return value.length();
    }


    public static int findTagsFromStackoverflow(String param) {
        String url = "https://stackoverflow.com/questions/tagged/"+param;

        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // Set request header
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = con.getResponseCode();
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Print result
            System.out.println(response.length());
            return response.length();

        } catch (IOException e) {
            System.out.println(" Error" + e.getMessage());
            return 0;
        }
    }
}
