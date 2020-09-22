package com.example.springasyncsample;

import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoadTest {
    public static void main(String[] args) throws Exception {
        AtomicInteger ai = new AtomicInteger(1);
        AtomicInteger successCount = new AtomicInteger(0);
        RestTemplate rt = new RestTemplate();
        ExecutorService es = Executors.newFixedThreadPool(100);
        CyclicBarrier cb = new CyclicBarrier(101);
        for (int i = 1; i <= 100; i++) {
            es.execute(() -> {
                try {
                    int idx = ai.getAndIncrement();
                    cb.await();

                    StopWatch sw = new StopWatch();
                    sw.start();
                    //rt.getForObject("http://localhost:8080/async-annotation/block-api", String.class);
                    rt.getForObject("http://localhost:8080/async-annotation/async-api3", String.class);
                    //rt.getForObject("http://localhost:8081/some-async-api2", String.class, idx);
                    //rt.getForObject("http://localhost:8081/some-block-api", String.class, idx);
                    successCount.incrementAndGet();
                    sw.stop();
                    log.info("Elapsed: idx : {}, Sec : {}", idx, sw.getTotalTimeSeconds());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        cb.await();
        StopWatch main = new StopWatch();
        main.start();


        es.shutdown();
        es.awaitTermination(100, TimeUnit.SECONDS);

        main.stop();
        log.info("Running Sec : {}, Success Count : {}", main.getTotalTimeSeconds(), successCount);
    }
}
