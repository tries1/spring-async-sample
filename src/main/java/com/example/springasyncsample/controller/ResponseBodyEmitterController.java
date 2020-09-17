package com.example.springasyncsample.controller;

import com.example.springasyncsample.service.MyService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.concurrent.CompletableFuture;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("rbe")
@RestController
public class ResponseBodyEmitterController {
    /*@RequestMapping("/events")
    public ResponseBodyEmitter<String> handle() {
        ResponseBodyEmitter<String> emitter = new ResponseBodyEmitter<String>();
        // Save the emitter somewhere..*return* emitter;
        // In some other thread
        emitter.send("Hello once");
        // and again later on
        emitter.send("Hello again");
        // and done at some point
        emitter.complete();
    }*/
}
