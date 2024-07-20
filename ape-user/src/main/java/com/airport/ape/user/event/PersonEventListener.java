package com.airport.ape.user.event;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class PersonEventListener {

    @TransactionalEventListener(fallbackExecution = true)
    public void listenPersonChange(PersonChangeEvent personChangeEvent){
        switch (personChangeEvent.getOperateType()){
            case "create":
                log.info("创建Person对象:{}", JSONObject.toJSONString(personChangeEvent.getPerson()));
                break;
            case "update":
                log.info("更新Person对象:{}",JSONObject.toJSONString(personChangeEvent.getPerson()));
                break;
            default:
                break;
        }
    }
}
