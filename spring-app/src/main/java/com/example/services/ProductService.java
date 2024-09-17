package com.example.services;

import com.example.model.dtos.TotalCostResponse;
import com.example.model.entities.CardInfo;
import com.example.model.entities.Product;
import com.example.repositories.ProductRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class ProductService {



  private final ProductRepository productRepository;

  static ExecutorService executorService = Executors.newFixedThreadPool(10);
  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }



  public int calculate() {
    int lowerBound = 100;
    int upperBound = 20000; 
    int count = 0;

    for (int i = lowerBound; i <= upperBound; i++) {
      if (isPrime(i)) {
        count++;
      }
    }

    return count;
  }


  public  boolean isPrime(int num) {
    if (num <= 1) {
      return false;
    }
    for (int i = 2; i <= Math.sqrt(num); i++) {
      if (num % i == 0) {
        return false;
      }
    }
    return true;
  }

  public  CompletableFuture<TotalCostResponse> getTotalCostsAsyncWay()  {
    return
            CompletableFuture.supplyAsync(() -> getTotalCosts(), executorService);

  }

  public  CompletableFuture<TotalCostResponse> getTotalCostsAsyncWayForkJoin()  {
    return
            CompletableFuture.supplyAsync(() -> getTotalCosts());

  }


  public TotalCostResponse getTotalCostsForked() {
    TotalCostResponse response = new TotalCostResponse();
    try {


      var products = productRepository.findAll();

      var costs = products.stream().parallel()
              .collect(Collectors.toMap(
                      Product::getName,
                      p -> p.getPrice().multiply(new BigDecimal(p.getQuantity()))));

      Thread.sleep(ThreadLocalRandom.current().nextInt(1000,5000));
      response.setTotalCosts(costs);
      System.out.println(" Service Thread name " + Thread.currentThread().getName());
    } catch (Exception e) {
      e.printStackTrace(); // do nothing
    }

    return response;
  }


  public TotalCostResponse getTotalCosts() {
    TotalCostResponse response = new TotalCostResponse();
    try {
      var products = productRepository.findAll(); // IO

      var costs = products.stream()
          .collect(Collectors.toMap(
              Product::getName,
              p -> p.getPrice().multiply(new BigDecimal(p.getQuantity())))); // CPU

      Thread.sleep(ThreadLocalRandom.current().nextInt(1000,5000)); // IO
      response.setTotalCosts(costs);
      ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executorService;
      int activeCount = threadPoolExecutor.getActiveCount();
      long taskCount = threadPoolExecutor.getTaskCount();
      long queueSize = threadPoolExecutor.getQueue().size();
      System.out.println(" Thread name " + Thread.currentThread().getName() +
              "  taskcount "  + taskCount
              + " active count " + activeCount + " queue size " + queueSize);
      // System.out.println(" Service Thread name " + Thread.currentThread().getName());
    } catch (Exception e) {
      e.printStackTrace(); // do nothing
    }

    return response;
  }

  @Async
  public CompletableFuture<TotalCostResponse> getTotalCostsAsync() {
    TotalCostResponse response = new TotalCostResponse();
    try {
      var products = productRepository.findAll();

      var costs = products.stream()
              .collect(Collectors.toMap(
                      Product::getName,
                      p -> p.getPrice().multiply(new BigDecimal(p.getQuantity()))));

      Thread.sleep(ThreadLocalRandom.current().nextInt(1000,5000));
      //Thread.currentThread().wait();
      response.setTotalCosts(costs);
      ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executorService;
      int activeCount = threadPoolExecutor.getActiveCount();
      long taskCount = threadPoolExecutor.getTaskCount();
      System.out.println(" Thread name " + Thread.currentThread().getName() + "  taskcount "  + taskCount + " active count " + activeCount);
    } catch (Exception e) {
      e.printStackTrace(); // do nothing
    }

    return  CompletableFuture.completedFuture(response);
  }

  public List<CardInfo> getCardInfos() throws InterruptedException {
    List<CardInfo> cardInfos = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      CardInfo cardInfo = new CardInfo();
      cardInfos.add(cardInfo);
      executorService.submit(cardInfo);
    }

    //List<Future<CardInfo>> answers = executorService.submit(cardInfos);
    List<CardInfo> results = new ArrayList<>();
    for (CardInfo future : cardInfos) {
      try {
        results.add(future.call());
      } catch (Exception e) {
        // Handle any exceptions during future.get()
        e.printStackTrace();
        // You might want to re-throw the exception or add a null value to results
      }
    }
    return results;
  }
}
