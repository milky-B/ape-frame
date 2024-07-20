package com.airport.ape.redis.cache;

import com.airport.ape.redis.init.AbstractCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserCache extends AbstractCache {
    private String USER_CACHE_KEY = "USER_";
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public void initCache() {
        redisTemplate.opsForValue().set(USER_CACHE_KEY,"user1");
    }

    @Override
    public void clearCache() {
        redisTemplate.delete(USER_CACHE_KEY);
    }

    @Override
    public <T> T getCache(String key) {
        if (!redisTemplate.hasKey(key)) {
            return null;
        }
        return (T) redisTemplate.opsForValue().get(key);
    }
}
