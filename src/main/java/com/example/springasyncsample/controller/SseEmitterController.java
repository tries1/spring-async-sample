package com.example.springasyncsample.controller;

import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("sse")
@RestController
public class SseEmitterController {
    private static boolean flag = true;
    private static AtomicInteger ai = new AtomicInteger();
    private static List<SseEmitter> sseEmitters = new ArrayList<>();

    @Scheduled(fixedRate = 1000L)
    public void sseScheduled() {
        log.info("SSE Scheduled, flag: {}, Size : {}", flag, sseEmitters.size());

        Integer count = ai.getAndIncrement();
        if (flag) {
            flag = false;

            sseEmitters.forEach(it -> {
                try {
                    /*SseEmitter.SseEventBuilder builder = SseEmitter.event()
                                    .data(count)
                                    .id(count + "")
                                    .name("eventName" + count)
                                    .reconnectTime(10_000L);*/

                    it.send(count);
                } catch (Exception e) {
                    sseEmitters.remove(it);
                    it.completeWithError(e);
                }
            });

            flag = true;
        }
    }

    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter sample1() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        //emitter.onError(ex -> sseEmitters.remove(emitter));
        //emitter.onTimeout(() -> sseEmitters.remove(emitter));

        sseEmitters.add(emitter);

        return emitter;
    }

    @GetMapping(value = "/events/clear")
    public String clear() {
        sseEmitters.forEach(it -> {
            it.complete();
            sseEmitters.remove(it);
        });
        return "Success Clear";
    }
}
