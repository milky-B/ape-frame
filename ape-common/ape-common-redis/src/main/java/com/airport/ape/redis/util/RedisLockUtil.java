package com.airport.ape.redis.util;

import com.airport.ape.redis.exception.RedisLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;

@Component
public class RedisLockUtil {
    @Autowired
    private RedisUtil redisUtil;
    private Long TIME_OUT = 10000L;
    /**
     * 解锁的lua脚本
     */
    private static final String UNLOCK_LUA;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("if redis.call(\"get\",KEYS[1]) == ARGV[1] ");
        sb.append("then ");
        sb.append("    return redis.call(\"del\",KEYS[1]) ");
        sb.append("else ");
        sb.append("    return 0 ");
        sb.append("end ");
        UNLOCK_LUA = sb.toString();
    }

    /**
     * @program:
     * @return:
     * @description: 上锁, 自旋
     **/
    public boolean lock(String key, String requestId, Long time) {
        if (!StringUtils.hasLength(key) || !StringUtils.hasLength(requestId) || time < 0) {
            throw new RedisLockException("redis-上锁-参数错误");
        }
        Long now = System.currentTimeMillis();
        Long outTime = now + TIME_OUT;
        while (now < outTime) {
            if (redisUtil.setNx(key, requestId, time)) {
                return true;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            now = System.currentTimeMillis();
        }
        return false;
    }

    /**
     * @program:
     * @return:
     * @description: 解锁
     **/
    public  boolean unlock(String key, String requestId) {
        if (!StringUtils.hasLength(key) || !StringUtils.hasLength(requestId)) {
            throw new RedisLockException("redis-解锁-参数错误");
        }
        return redisUtil.eval(UNLOCK_LUA, Collections.singletonList(key), Collections.singletonList(requestId));
    }

    /**
     * @program:
     * @return:
     * @description: 尝试上锁
     **/
    public boolean tryLock(String key, String requestId, Long time) {
        if (!StringUtils.hasLength(key) || !StringUtils.hasLength(requestId) || time < 0) {
            throw new RedisLockException("redis-上锁-参数错误");
        }
        return redisUtil.setNx(key, requestId, time);
    }
}
