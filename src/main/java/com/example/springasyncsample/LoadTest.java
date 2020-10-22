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
        final Integer RUNNER = 100;
        AtomicInteger ai = new AtomicInteger(1);
        AtomicInteger successCount = new AtomicInteger(0);
        RestTemplate rt = new RestTemplate();
        ExecutorService es = Executors.newFixedThreadPool(RUNNER);
        CyclicBarrier cb = new CyclicBarrier(RUNNER + 1);
        for (int i = 1; i <= RUNNER; i++) {
            es.execute(() -> {
                try {
                    int idx = ai.getAndIncrement();
                    cb.await();

                    StopWatch sw = new StopWatch();
                    sw.start();
                    rt.getForObject("http://localhost:8080/block/sample-api", String.class);
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
