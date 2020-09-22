package com.example.springasyncsample.controller;

import com.example.springasyncsample.service.MyService;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 1. @Async를 이용한 비동기 처리
 * 2.
 * 3.
 */

@Slf4j
@RequiredArgsConstructor
@RequestMapping("async-annotation")
@RestController
public class AsyncAnnotationController {
    private final MyService myService;

    private static boolean flag = true;
    Queue<DeferredResult<String>> drQueue = new ConcurrentLinkedQueue<>();
    ExecutorService es = Executors.newFixedThreadPool(10);

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Scheduled(cron = "* * * * * *")
    public void Scheduled() {
        log.info("Scheduled, flag : {},  Queue Size : {}", flag, drQueue.size());

        if (flag) {
            flag = false;

            drQueue.forEach(it -> {
                DeferredResult<String> dr = drQueue.poll();
                es.submit(() -> dr.setResult(myService.block()));
            });

            flag = true;
        }
    }

    /**
     * 일반적인 blocking api
     * tomcat.threads 기본설정일때 Thread의 개수확인예제
     *
     * @return
     * @throws InterruptedException
     */
    @GetMapping("block-api")
    public String someBlockAPI() {
        myService.block();
        return String.format("some-block-api call, timestamp : %s", LocalDateTime.now().format(dtf));
    }

    @GetMapping("async-api")
    public String someAsyncAPI() {
        // AsyncConfig에서 @EnableAsync가 없으면 @Async어노테이션은 작동하지 않는다.

        //CompletableFuture<String> cf = asyncService.async1().completable();
        myService.async().addCallback(System.out::println, ex -> System.out.println(ex.getMessage()));
        return String.format("some-block-api call, timestamp : %s", LocalDateTime.now().format(dtf));
    }

    @GetMapping("async-api2")
    public ListenableFuture<String> someAsyncAPI2() {
        return myService.async();
    }

    @GetMapping("async-api3")
    public DeferredResult<String> someAsyncAPI3() {
        DeferredResult<String> dr = new DeferredResult<>();
        drQueue.add(dr);
        return dr;
    }
}