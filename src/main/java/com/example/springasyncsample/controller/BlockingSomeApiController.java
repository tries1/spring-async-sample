package com.example.springasyncsample.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class BlockingSomeApiController {
    private final RestTemplate rt;

    //@GetMapping("block1")
    public String block1(String req) {
        return rt.getForObject("http://localhost:8081/some-api1?req={req}", String.class, req);
    }
}
