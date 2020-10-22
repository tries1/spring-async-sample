package com.example.springasyncsample.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("block")
@RestController
public class BlockingSomeApiController {
    private final RestTemplate rt;

    @GetMapping("sample-api")
    public String sampleApi(String req) {
        return rt.getForObject("http://localhost:8081/some-api1?req={req}", String.class, req);
    }
}
