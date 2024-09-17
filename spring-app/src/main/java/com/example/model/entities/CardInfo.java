package com.example.model.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class CardInfo implements Callable<CardInfo> {


    private Integer cardId;

    public CardInfo(){
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(1000,5000));
            cardId =   ThreadLocalRandom.current().nextInt(1000,5000);
            System.out.println(
                    Thread.currentThread().getName() + ": Card Info created " + cardId);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public CardInfo call()  {
        System.out.println(
                Thread.currentThread().getName() + ": call  " );

        return this;
    }

    @Override
    public String toString() {
        return "CardInfo{" +
                "cardId=" + cardId +
                '}';
    }
}