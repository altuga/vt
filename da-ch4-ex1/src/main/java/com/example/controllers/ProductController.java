package com.example.controllers;

import com.example.model.dtos.TotalCostResponse;
import com.example.model.entities.CardInfo;
import com.example.services.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;




@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping("/total/costs/prime")
    public int findPrime() {

        // CPU intensive
        return productService.calculate();

    }

    @GetMapping("/total/costs/v1")
    //@SchemaMapping(typeName = "Query", fieldName = "bookById")
    public TotalCostResponse totalCostsv1() {

        System.out.println("Controller Thread name " + Thread.currentThread().getName());
        return productService.getTotalCosts();


    }

    @GetMapping("/total/costs/v2")
    //@SchemaMapping(typeName = "Query", fieldName = "bookById")
    public CompletableFuture<TotalCostResponse> totalCostsv2() {

        /**
         *
         */
        System.out.println(" Thread name " + Thread.currentThread().getName());
        return productService.getTotalCostsAsyncWay().thenApply(value -> {
            if (value != null) {
                System.out.println("Controller app is finished " + Thread.currentThread().getName());
                return value;
            } else {
                System.out.println("Controller is not finished");
                return null;
            }
        });


    }
    @GetMapping("/total/costs/v3")
    //@SchemaMapping(typeName = "Query", fieldName = "bookById")
    public CompletableFuture<TotalCostResponse> totalCostsv3() {

        return productService.getTotalCostsAsync().thenApply(value -> {
            if (value != null) {
                System.out.println("Controller app is finished " + Thread.currentThread().getName());
                return value;
            } else {
                System.out.println("Controller app is not finished");
                return null;
            }
        });

    }

    @GetMapping("/total/costs/v4")
    //@SchemaMapping(typeName = "Query", fieldName = "bookById")
    public CompletableFuture<TotalCostResponse> totalCostsv4() {

        /**
         *
         */

        return productService.getTotalCostsAsyncWayForkJoin().thenApply(value -> {
            if (value != null) {
                System.out.println("Controller app is finished " + Thread.currentThread().getName());
                return value;
            } else {
                System.out.println("Controller app is not finished");
                return null;
            }
        });


    }


    @GetMapping("/total/costs/v5")
    //@SchemaMapping(typeName = "Query", fieldName = "bookById")
    public List<CardInfo> totalCostsv5() {
        System.out.println("Controller Thread name " + Thread.currentThread().getName());
        try {
            return productService.getCardInfos();
        } catch (InterruptedException e) {
            Logger.getLogger("default").log(Level.SEVERE, "Error " + e.getMessage() );
            throw new RuntimeException(e);
        }


    }
}
