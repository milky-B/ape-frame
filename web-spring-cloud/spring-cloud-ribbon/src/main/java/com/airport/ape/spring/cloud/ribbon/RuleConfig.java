package com.airport.ape.spring.cloud.ribbon;

import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RuleConfig {

    @Bean
    public IRule myCustomRule(){
        return new MyCustomRule();
    }
}
