package com.example.springasyncsample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
public class SpringAsyncSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAsyncSampleApplication.class, args);
    }

}
