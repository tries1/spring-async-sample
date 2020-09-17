package com.example.springasyncsample.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.Future;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MyService {
    public String block() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "hello";
    }

    //@Async("myAsyncThreadPoolTaskExecutor")
    @Async
    public ListenableFuture<String> async() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new AsyncResult<>("hello");
    }
}