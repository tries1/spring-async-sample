package com.example.springasyncsample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfiguration /*implements AsyncConfigurer*/ {
    private final int THREAD_POOL_SIZE = 5;

    /*@Override
    public Executor getAsyncExecutor() {
        return new ThreadPoolTaskExecutor();
    }*/

    @Bean(name = "myAsyncThreadPoolTaskExecutor")
    public Executor myAsyncThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(THREAD_POOL_SIZE);
        taskExecutor.setMaxPoolSize(THREAD_POOL_SIZE);
        taskExecutor.setQueueCapacity(Integer.MAX_VALUE);
        taskExecutor.setThreadNamePrefix("myAsyncThreadPoolTaskExecutor-");
        taskExecutor.initialize();
        return taskExecutor;
    }
}