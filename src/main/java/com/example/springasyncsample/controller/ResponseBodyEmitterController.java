package com.example.springasyncsample.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("rbe")
@RestController
public class ResponseBodyEmitterController {

    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseBodyEmitter sample1() {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();

        Executors.newSingleThreadExecutor().submit(() -> {
                    IntStream.rangeClosed(1, 100)
                            .forEach(i -> {
                                try {
                                    TimeUnit.SECONDS.sleep(1);
                                    emitter.send("number : " + i);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });

                    //emitter.complete();
                }

        );


        return emitter;
    }
}
