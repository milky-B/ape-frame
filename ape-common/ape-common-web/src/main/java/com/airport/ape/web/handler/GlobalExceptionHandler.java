package com.airport.ape.web.handler;

import com.airport.ape.web.entity.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public Result runtimeExceptionHandler(RuntimeException exception){
        exception.printStackTrace();
        return Result.fail(exception.getMessage());
    }
}
