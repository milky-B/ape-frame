package com.airport.ape.tool;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;

public class CompletableFutureUtils {
    public static <T> T getResult(Future<T> future, long timeOut,
                                  TimeUnit timeUnit, T defaultValue, Logger logger){
        try{
            return future.get(timeOut,timeUnit);
        }catch (Exception e){
            logger.error("CompletableFutureUtils.getResult.error {}:",e.getMessage(),e);
            return defaultValue;
        }
    }
}
