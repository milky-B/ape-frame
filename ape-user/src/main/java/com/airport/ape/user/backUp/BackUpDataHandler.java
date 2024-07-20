package com.airport.ape.user.backUp;

/**
 * 数据归档接口
 * 
 * @author: leen
 * @date: 2023/3/26
 */
public interface BackUpDataHandler {

    /**
     * 获取场景
     */
    BackUpDataSceneEnum getScene();

    /**
     * 数据归档
     */
    void backUpData();

}