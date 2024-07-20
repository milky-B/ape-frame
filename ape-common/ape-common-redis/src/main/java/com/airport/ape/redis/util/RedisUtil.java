package com.airport.ape.redis.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private DefaultRedisScript<Boolean> casScript;

    @PostConstruct
    public void init(){
        casScript = new DefaultRedisScript<>();
        casScript.setResultType(Boolean.class);
        casScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("compareAndSet.lua")));
    }

    /**
     * 对hash类型的数据操作
     *
     * @return
     */
    public void hashPut(String key, Map<?, ?> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    public void hashPut(String key, Object hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 对redis字符串类型数据操作
     *
     * @return
     */
    public void set(String key,Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public boolean setNx(String key,String value,Long time){
        return redisTemplate.opsForValue().setIfAbsent(key,value,time, TimeUnit.MILLISECONDS);
    }
    public boolean eval(String lua, List<String> keys,List<Object> args){
        return redisTemplate.execute(RedisScript.of(lua,Long.class), keys, args).equals(1);
    }
    public Object get(String key){
        return redisTemplate.opsForValue().get(key);
    }
    public boolean zAdd(String key,Object value,double score){
        return redisTemplate.opsForZSet().add(key,value,score);
    }
    public Set<Object> rangeByCore(String key, double start, double end){
        return redisTemplate.opsForZSet().rangeByScore(key, start, end);
    }
    public Long removeZSet(String key, Object value){
        return redisTemplate.opsForZSet().remove(key,value);
    }
    public void removeZSetList(String key, Set<Object> values){
        /*values.forEach(value->{
            redisTemplate.opsForZSet().remove(key,value);
        });*/
        redisTemplate.opsForZSet().remove(key,values.toArray());
    }
    public boolean evalCas(String key,Long oldValue,Long newValue){
        return redisTemplate.execute(casScript,Collections.singletonList(key),oldValue,newValue);
    }

}
