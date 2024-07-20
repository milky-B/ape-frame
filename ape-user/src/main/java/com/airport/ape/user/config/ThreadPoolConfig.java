package com.airport.ape.user.config;

import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadPoolConfig {

    @Bean("mailThreadPool")
    public ThreadPoolExecutor getMailThreadPool(){
        CustomNameThreadFactory mail = new CustomNameThreadFactory("mail");
        return new ThreadPoolExecutor(20,50,5,
                TimeUnit.SECONDS,new LinkedBlockingQueue(),mail,new ThreadPoolExecutor.DiscardPolicy());
    }
}
