package com.airport.ape.redis.exception;

public class RedisLockException extends RuntimeException{

    public RedisLockException(String message) {
        super(message);
    }
}
