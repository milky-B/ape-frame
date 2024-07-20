package com.airport.ape.web.entity;

import lombok.Data;

@Data
public class Result<T> {
    private Boolean isSuccess;
    private int resultCode;
    private String resultMessage;
    private T data;
    public Result(){}
    public static Result success(){
        Result result = new Result();
        result.isSuccess = true;
        result.resultCode = ResultCode.SUCCESS;
        result.resultMessage = ResultMessage.SUCCESS;
        return result;
    }
    public static<T> Result<T> success(T data){
        Result<T> result = new Result<T>();
        result.isSuccess = true;
        result.resultCode = ResultCode.SUCCESS;
        result.resultMessage = ResultMessage.SUCCESS;
        result.data = data;
        return result;
    }
    public static<T> Result<T> success(int resultCode,T data){
        Result<T> result = new Result<T>();
        result.isSuccess = true;
        result.resultCode = resultCode;
        result.resultMessage = ResultMessage.SUCCESS;
        result.data = data;
        return result;
    }
    public static Result fail(){
        Result result = new Result();
        result.isSuccess = false;
        result.resultCode = ResultCode.ERROR;
        result.resultMessage = ResultMessage.ERROR;
        return result;
    }
    public static Result fail(int resultCode){
        Result result = new Result();
        result.isSuccess = false;
        result.resultCode = resultCode;
        result.resultMessage = ResultMessage.ERROR;
        return result;
    }
    public static Result fail(String resultMessage){
        Result result = new Result();
        result.isSuccess = false;
        result.resultCode = ResultCode.ERROR;
        result.resultMessage = resultMessage;
        return result;
    }
}
