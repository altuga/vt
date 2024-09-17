package async;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Blocking {
    static ExecutorService executorService = Executors.newFixedThreadPool(5);

    public static void main(String[] args) throws Exception {
        // supplyAsyncWithExecutorService();
        chained();
        System.out.println("Finished " + Thread.currentThread().getName());
    }


    /**
     * String::toUpperCase ve String::toUpperCase fonksiyonları register ediliyor.
     * Ama bu non-blocking 
     * Bu esas nokta...
     * @throws Exception
     */
    public static void chained() throws Exception  {
        javaQuestions().thenAccept(status -> {
                if (status != null) {
                    System.out.println("app is finished");
                } else {
                    System.out.println("app is not finished");
                }
        });

            
    }


    /**
     * String::toUpperCase ve String::toUpperCase fonksiyonları register ediliyor.
     * Ama bu non-blocking 
     * @throws Exception
     */
    public static void thenApplyChainedNonBlocking() throws Exception  {
        final CompletableFuture<String> java = CompletableFuture.supplyAsync(
                        ()-> getAnswerFromAPI("java"), executorService); // running a blocking code
                    
         
            java.thenApply(String::toUpperCase).
            thenApply(String::length).
            thenAccept((Integer result) -> System.out.println(result)); // non-blocking

            
    }


    /**
     * String::toUpperCase ve String::toUpperCase fonksiyonları register ediliyor.
     * @throws Exception
     */
    public static void thenApplyChained() throws Exception  {
        final CompletableFuture<String> java = CompletableFuture.supplyAsync(
                        ()-> getAnswerFromAPI("java"), executorService); // running a blocking code 
                    
        final CompletableFuture<Integer> length = java.thenApply(String::toUpperCase).thenApply(String::length);

        System.out.println(" --> " + length.get()) ;   // blocking yapıyor            
    }



    /**
     * Bu ornekte once main thread bitiyor, sonra Future task bitiyor
     * Call back hell var
     * @throws Exception
     */
    public static void callbacksEverywhere() throws Exception  {
        final CompletableFuture<String> java = CompletableFuture.supplyAsync(
                        ()-> getAnswerFromAPI("java"), executorService); // running a blocking code 
                       
        System.out.println(" --> " + java.thenAccept((String result) -> System.out.println(result) )) ;               
    }


   /**
     * Bu method supplyAsync() ile aynı, tek fark  executorService kullanması
     * @throws Exception
     */
    public static void supplyAsyncWithExecutorService() throws Exception  {
        final CompletableFuture<String> java = CompletableFuture.supplyAsync(
                        ()-> getAnswerFromAPI("java"), executorService); // running a blocking code 
                       
        System.out.println(" --> " + java.get()) ;    // blocking
    }

    /**
     * 1 - Burada ne eksik
     *   - Executor service içerisinde çağrılmamıi, default fork-join kullanılıyor
     *   - fork-join içerisindeki pool mekanizmasını göster
     *   - Timeout yok
     * @throws Exception
     */
    public static void supplyAsync() throws Exception  {
        final CompletableFuture<String> java = CompletableFuture.supplyAsync(
                        ()-> getAnswerFromAPI("java")); // running a blocking code 
                       
        System.out.println(" --> " + java.get()) ;               
    }

    public static void waitForFirstOrAll() throws Exception  {
        final Future<String>  api_A =  findAnswersFromAPI("A");
        final Future<String>  api_B =  findAnswersFromAPI("B");
        String a = api_A.get(); // blocking - waste of Thread
        String b = api_B.get(); // blocking - waste of other Thread
        System.out.println(" a " + a + " - " + b );
    }
    
    public static void executorService()  throws Exception {
       
        final Callable<String>  task = () -> getAnswerFromAPI("a");
        final Future<String> answer = executorService.submit(task);

         System.out.println(answer.get(10, TimeUnit.SECONDS)); // blocking

    }

    
    public static void blockingCall() {
        String answer = getAnswerFromAPI("a"); // blocking 
        System.out.println(answer);

    }


    /**
     * Blocking operation
     * @param tag
     * @return
     */
    public static String getAnswerFromAPI(String tag)  {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextLong(1000, 10000));
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println(" " + e );
        }
        return "Answer from " + tag;
    }

    public static Future<String> findAnswersFromAPI(String tag)  {
        final Callable<String> task =  () -> {
            System.out.println(" --> " +   Thread.currentThread().getName() );
            return  getAnswerFromAPI(tag);};
        return executorService.submit(task);
    }

    public static CompletableFuture<String> javaQuestions()  {
        return 
            CompletableFuture.supplyAsync(() -> getAnswerFromAPI("java"), executorService);

       
    }


}
