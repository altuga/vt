package main;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.concurrent.*;

import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DummyProcess {
    public static int loops = 100;
    public static int idx = 0;
    private static long sleepTimeMs = 1000;


    private static Logger log = LogManager.getLogger(DummyProcess.class);
    private static ExecutorService customPool = Executors.newFixedThreadPool(loops);

    public static void seq() {
        LongSummaryStatistics stats = LongStream.range(0, loops).map(DummyProcess::slowNetworkCall).summaryStatistics();
        System.out.println(stats);
    }

    public static void ps() {
        Instant start = Instant.now();
        LongSummaryStatistics stats = LongStream.range(0, loops).parallel().map(DummyProcess::slowNetworkCall).summaryStatistics();
        log.info("ps200 completed in :: {}, summaryStats :: {} ", Duration.between(start, Instant.now()).toMillis(), stats);
    }

    /**
     * Fastest, with one caveat: if any exception, will have to wait till the end,
     * but imo, assuming Exceptions are rare, the happy path gains should amortize the occasional exceptional slowness :)
     */
    public static void cf() {
        Instant start = Instant.now();

        List<CompletableFuture<Long>> collect = LongStream.range(0, loops).boxed()
                .map(number -> CompletableFuture.supplyAsync(() -> DummyProcess.slowNetworkCall(number), customPool))
                .collect(Collectors.toList());

        LongSummaryStatistics stats = collect.stream().map(CompletableFuture::join).mapToLong(Long::longValue).summaryStatistics();

        log.info("cf completed in :: {}, summaryStats :: {} ", Duration.between(start, Instant.now()).toMillis(), stats);
    }

    /**
     * Streams process things lazily, which means that the pipeline would submit each operation and immediately wait
     * for a result to arrive before starting remaining operations, which could quickly end up with a processing time
     * longer than processing the collection sequentially
     */
    public static void cf_directJoin() {
        Instant start = Instant.now();

        LongSummaryStatistics stats = LongStream.range(0, loops).boxed()
                .map(number -> CompletableFuture.supplyAsync(() -> DummyProcess.slowNetworkCall(number), customPool))
                .map(CompletableFuture::join)
                .mapToLong(Long::longValue)
                .summaryStatistics();

        log.info("cf200 completed in :: {}, summaryStats :: {} ", Duration.between(start, Instant.now()).toMillis(), stats);
    }

    public static void cfps_directJoin() {
        Instant start = Instant.now();
        LongSummaryStatistics stats = LongStream.range(0, loops).boxed()
                .parallel()
                .map(number -> CompletableFuture.supplyAsync(() -> DummyProcess.slowNetworkCall(number)))
                .map(CompletableFuture::join)
                .mapToLong(Long::longValue).summaryStatistics();

        log.info("cfps_directJoin completed in :: {}, summaryStats :: {} ", Duration.between(start, Instant.now()).toMillis(), stats);
    }

    public static void cfps() {
        Instant start = Instant.now();
        List<CompletableFuture<Long>> collect = LongStream.range(0, loops).boxed()
                .parallel()
                .map(number -> CompletableFuture.supplyAsync(() -> DummyProcess.slowNetworkCall(number), customPool))
                .collect(Collectors.toList());

        LongSummaryStatistics stats = collect.stream().map(CompletableFuture::join).mapToLong(Long::longValue).summaryStatistics();

        log.info("cf200ps completed in :: {}, summaryStats :: {} ", Duration.between(start, Instant.now()).toMillis(), stats);
    }

    public static Long slowNetworkCall(Long i) {
        Instant start = Instant.now();
        log.info(" {} going to sleep. poolsize: {}", i, ForkJoinPool.commonPool().getPoolSize());
        try {
            Thread.sleep(sleepTimeMs);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info(" {} woke up..", i);
        return Duration.between(start, Instant.now()).toMillis();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("CPU Core: " + Runtime.getRuntime().availableProcessors());

        System.out.println("CommonPool Parallelism: " + ForkJoinPool.commonPool().getParallelism());
        System.out.println("CommonPool Common Parallelism: " + ForkJoinPool.getCommonPoolParallelism());

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int result = bufferedReader.read();

//        DummyProcess.seq();
        // DummyProcess.ps();
//        DummyProcess.cf();
//        DummyProcess.cf_directJoin();
//        DummyProcess.cfps();
        DummyProcess.cfps_directJoin();
//        DummyProcess.cf200ps();
        result = bufferedReader.read();
        System.out.printf(" " + result);
        // System.exit(0);
    }
}