package com.example.springasyncsample.controller;

import com.example.springasyncsample.service.MyService;

import org.springframework.http.ResponseEntity;
import org.springframework.http.client.Netty4ClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.CompletableToListenableFutureAdapter;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import io.netty.channel.nio.NioEventLoopGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("cf")
@RestController
public class CompletableFutureController {
    private final MyService myService;
    //AsyncRestTemplate art = new AsyncRestTemplate(new Netty4ClientHttpRequestFactory(new NioEventLoopGroup(1)));
    AsyncRestTemplate art = new AsyncRestTemplate();

    //non-blocking이 아님, 별도의 스레드에서 수행할뿐
    @GetMapping("sample1")
    public DeferredResult<String> sample1() {
        DeferredResult<String> dr = new DeferredResult<>();

        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> myService.block());
        CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(() -> myService.block());
        CompletableFuture<String> cf3 = CompletableFuture.supplyAsync(() -> myService.block());
        List<CompletableFuture<String>> cfList = Arrays.asList(cf1, cf2, cf3);

        CompletableFuture.allOf(cf1, cf2, cf3)
        .thenAccept(aVoid -> dr.setResult(cfList.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.joining(", ")))
        );

        return dr;
    }

    //non-blocking이 아님, 별도의 스레드에서 수행할뿐
    @GetMapping("sample2")
    public DeferredResult<String> sample2() {
        DeferredResult<String> dr = new DeferredResult<>();

        //CF 는 비동기의 상태를 가지고있는
        toCF(art.getForEntity("http://localhost:8081/some-api1", String.class))
                .thenCompose(it -> toCF(art.getForEntity("http://localhost:8081/some-api1", String.class)))
                .thenCompose(it -> toCF(art.getForEntity("http://localhost:8081/some-api1", String.class)))
                //.thenCompose(it -> toCF(myService.async()))
                .thenAccept(it -> dr.setResult(String.format("%s", it)))
                .exceptionally(e -> {
                    dr.setErrorResult(e.getMessage());
                    return (Void) null;
                });

        return dr;
    }

    <T> CompletableFuture<T> toCF(ListenableFuture<T> lf) {
        CompletableFuture<T> cf = new CompletableFuture<>();
        lf.addCallback(it -> cf.complete(it), e -> cf.completeExceptionally(e));
        return cf;
    }
}
