package com.example.springasyncsample.controller;

import com.example.springasyncsample.service.MyService;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureTask;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("lf")
@RestController
public class ListenableFutureController {
    private final MyService myService;
    RestTemplate rt = new RestTemplate();
    AsyncRestTemplate art = new AsyncRestTemplate();

    @GetMapping("sample1")
    public ListenableFuture<ResponseEntity<String>> sample1(String req) {
        return art.getForEntity("http://localhost:8081/some-api1?req={req}", String.class, req);
    }

    @GetMapping("sample2")
    public DeferredResult<String> sample2() {
        DeferredResult<String> dr = new DeferredResult<>();

        ListenableFuture<ResponseEntity<String>> lf1 = art.getForEntity("http://localhost:8081/some-api1", String.class);
        lf1.addCallback(res1 -> {
            ListenableFuture<ResponseEntity<String>> lf2 = art.getForEntity("http://localhost:8081/some-api1", String.class);
            lf2.addCallback(res2 -> {
                ListenableFuture<ResponseEntity<String>> lf3 = art.getForEntity("http://localhost:8081/some-api1", String.class);
                lf3.addCallback(res3 -> {
                    dr.setResult(String.format("%s, %s, %s", res1.getBody(), res2.getBody(), res3.getBody()));
                }, e -> log.error(e.getMessage()));
            }, e -> log.error(e.getMessage()));
        }, e -> log.error(e.getMessage()));

        return dr;
    }
}
