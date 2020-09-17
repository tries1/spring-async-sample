package com.example.springasyncsample.controller;

import com.example.springasyncsample.service.MyService;

import org.springframework.http.ResponseEntity;
import org.springframework.http.client.Netty4ClientHttpRequestFactory;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Future;

import io.netty.channel.nio.NioEventLoopGroup;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class NonBlockingController {
    private final MyService myService;
    RestTemplate rt = new RestTemplate();
    AsyncRestTemplate art = new AsyncRestTemplate(new Netty4ClientHttpRequestFactory(new NioEventLoopGroup(1)));

    @GetMapping("block1")
    public String block1(String req) {
        return rt.getForObject("http://localhost:8081/block?req={req}", String.class, req);
    }

    @GetMapping("nonblock1")
    public ListenableFuture<ResponseEntity<String>> nonblock1(String req) {
        return art.getForEntity("http://localhost:8081/block?req={req}", String.class, req);
    }

    @GetMapping("nonblock2")
    public String nonblock2() {
        myService.async();
        return "hello";
    }

    @GetMapping("nonblock3")
    public Future<String> nonblock3() {
        return new AsyncResult<>(myService.block());
    }
}
