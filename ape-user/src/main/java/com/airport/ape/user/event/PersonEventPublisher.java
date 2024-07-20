package com.airport.ape.user.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
@Component
public class PersonEventPublisher {
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    public void createPersonEvent(Person person){
        applicationEventPublisher.publishEvent(new PersonChangeEvent(person,"create"));
    }

    public void updatePersonEvent(Person person){
        applicationEventPublisher.publishEvent(new PersonChangeEvent(person,"update"));
    }

}
