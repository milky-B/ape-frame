package com.airport.ape.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan(basePackages= {"com.airport.ape.user.mapper"})
@ComponentScan("com.airport.ape")
@EnableCaching
public class  UserApplication {
    public static void main(String[] args) {
        //配置异步日志上下文选择器,不配置的话，等于没开异步
        System.setProperty("log4jContextSelector","org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");
        SpringApplication.run(UserApplication.class);
    }
}