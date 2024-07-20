package com.airport.ape.user.init;

import com.alibaba.fastjson.JSONObject;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ApplicationInit implements ApplicationListener<ApplicationReadyEvent> {
    Map<String, InitFunction> initMap = new HashMap<>();
    {
        initMap.put("initJson",this::initJson);
    }
    public void initJson(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","lee");
        jsonObject.put("age",18);
        System.out.println(jsonObject);
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        initMap.forEach((key,value)->{
            Long start = System.currentTimeMillis();
            //log.info("server {}init start",init.getKey());
            value.init();
            log.info("server{}init cost time{}",key,System.currentTimeMillis()-start);
        });
    }

    interface InitFunction{
        void init();
    }
}
