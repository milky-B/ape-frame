package com.airport.ape.user.backUp;

/**
 * 数据归档service
 *
 * @author: leen
 * @date: 2023/3/26
 */
public interface BackUpService {

    /**
     * 数据归档
     */
    void backUp(BackUpDataSceneEnum sceneEnum);

}