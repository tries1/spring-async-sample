package com.example.springasyncsample.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class NonBlockingController {
    private final MyService myService;
    RestTemplate rt = new RestTemplate();
    AsyncRestTemplate art = new AsyncRestTemplate();

    @GetMapping("block1")
    public String block1(String req) {
        return rt.getForObject("http://localhost:8081/block?req={req}", String.class, req);
    }

    @GetMapping("nonblock1")
    public ListenableFuture<ResponseEntity<String>> nonblock1(String req) {
        return art.getForEntity("http://localhost:8081/block?req={req}", String.class, req);
    }

    @GetMapping("nonblock2")
    public String nonblock2() throws ExecutionException, InterruptedException {
        return myService.block().get();
    }


    @Service
    public static class MyService {
        @Async
        public Future<String> block() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return new AsyncResult<>("hello");
        }
    }
}
