package com.airport.ape.user.cache;

import com.airport.ape.redis.init.AbstractCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class  CategoryCache extends AbstractCache {
    private String Category_CACHE_KEY = "Category_";
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public void initCache() {
        redisTemplate.opsForValue().set(Category_CACHE_KEY,"user1");
    }

    @Override
    public void clearCache() {
        redisTemplate.delete(Category_CACHE_KEY);
    }
}
