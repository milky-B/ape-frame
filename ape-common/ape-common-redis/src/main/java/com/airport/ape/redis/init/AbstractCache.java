package com.airport.ape.redis.init;

public abstract class AbstractCache {
    //进行数据交互，实现预热
    public abstract void initCache();
    //清楚缓存
    public abstract void clearCache();
    //重载
    public void reloadCache(){
        clearCache();
        initCache();
    }
    public<T> T getCache(String key){
        return null;
    }
}
