package com.example.springasyncsample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.Netty4ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executor;

import io.netty.channel.nio.NioEventLoopGroup;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public AsyncRestTemplate asyncRestTemplate() {
        //SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        //factory.setTaskExecutor(threadPoolTaskExecutor());

        return new AsyncRestTemplate();
        //return new AsyncRestTemplate(factory);
        //return new AsyncRestTemplate(new Netty4ClientHttpRequestFactory(new NioEventLoopGroup(1)));
    }

    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        //
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(1);// 처음에 만들어지는 poolsize
        taskExecutor.setMaxPoolSize(80);// 최대 poolsize
        taskExecutor.setQueueCapacity(10);// pool이 모두 사용중일때 큐에 대기시킬 작업 최대수
        taskExecutor.setThreadNamePrefix("my-executor-");
        taskExecutor.initialize();
        return taskExecutor;
    }
}
