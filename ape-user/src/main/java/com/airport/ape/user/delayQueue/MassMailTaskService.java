package com.airport.ape.user.delayQueue;

import com.airport.ape.redis.util.RedisUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class MassMailTaskService {
    @Autowired
    private RedisUtil redisUtil;

    private String DELAY_QUEUE_KEY_MAIL_TASK="MAIL_DELAY_TASK_QUEUE";

    public void publishMassMailTask(MassMailTask massMailTask){
        Date startTime = massMailTask.getStartTime();
        if(startTime==null){
            return;
        }
        if(startTime.compareTo(new Date())<=0){
            return;
        }
        redisUtil.zAdd(DELAY_QUEUE_KEY_MAIL_TASK, massMailTask.getTaskId(), startTime.getTime());
        log.info("定时群发任务加入延时队列，massMailTask:{}", JSON.toJSON(massMailTask));
    }

    public Set<Long> pullMassMailTask(){
        Set<Object> objects = redisUtil.rangeByCore(DELAY_QUEUE_KEY_MAIL_TASK, 0, System.currentTimeMillis());
        log.info("获取群发任务,massMailTask:{}",objects);
        if(CollectionUtils.isEmpty(objects)){
            return Collections.EMPTY_SET;
        }
        redisUtil.removeZSetList(DELAY_QUEUE_KEY_MAIL_TASK, objects);
        return objects.stream().filter(object->object instanceof Long).map(object->(Long)object).collect(Collectors.toSet());
    }
}
