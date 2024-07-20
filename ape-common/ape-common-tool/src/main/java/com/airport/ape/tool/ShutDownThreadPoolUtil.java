package com.airport.ape.tool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ShutDownThreadPoolUtil {
    public static void shutDownPool(ExecutorService executorService, long shutdownAwaitTimeout,
                                    long shutdownNowAwaitTimeout,TimeUnit timeUnit){
        executorService.shutdown();
        try {
            if(!executorService.awaitTermination(shutdownAwaitTimeout ,timeUnit)){
                log.info("ShutdownThreadPoolUnit.shutDownPool shutdown fail");
            }
            executorService.shutdownNow();
            if(!executorService.awaitTermination(shutdownNowAwaitTimeout,timeUnit)){
                log.error("ShutdownThreadPoolUnit.shutDownPool shutdownNow fail");
            }
        }catch (InterruptedException e) {
            log.error("shutdown thread pool interrupt:{}",e.getMessage(),e);
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
