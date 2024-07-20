package com.airport.ape.redis.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;


@Component
public class CacheUtil<K, V> {
    //通过配置文件控制cache的启动
    @Value("${guava.cache.switch}")
    private boolean cacheSwitch;
    //初始化cache
    private Cache<String, String> localCache = CacheBuilder.newBuilder()
            .maximumSize(5000)
            .expireAfterWrite(10, TimeUnit.SECONDS)
            .build();

    /**
     * @param skuIdList id列表
     * @param prefix    前缀
     * @param suffix    后缀
     * @param clazz     类型，用作反序列化
     * @param function  实现rpc调用函数
     * @return Map<K, V>
     */

    public Map<K, V> getResult(List<K> skuIdList, String prefix, String suffix, Class<V> clazz, Function<List<K>, Map<K, V>> function) {
        //1.数据校验
        if (CollectionUtils.isEmpty(skuIdList)) {
            return Collections.emptyMap();
        }
        Map<K, V> map = new HashMap<>();
        //2.没有开启本地缓存,直接查
        if (!cacheSwitch) {
            map = function.apply(skuIdList);
            return map;
        }
        //记录keys中没有存储到guava中的key
        List<K> noCacheList = new ArrayList<>();
        //3.开启了就先去缓存中找
        for (K skuId : skuIdList) {
            String value = localCache.getIfPresent(prefix + "_" + skuId + "_" + suffix);
            if (StringUtils.hasLength(value)) {
                V v = JSON.parseObject(value, clazz);
                map.put(skuId, v);
            } else {
                noCacheList.add(skuId);
            }
        }
        //4.部分没找到的去rpc找,找到后加入结果集和本地缓存
        // 没找到的去rpc找
        if (noCacheList.size() != 0) {
            Map<K, V> noCacheMap = function.apply(noCacheList);
            //从rpc找到的加入结果集和本地缓存
            if (!CollectionUtils.isEmpty(noCacheMap)) {
                for (Map.Entry<K, V> entry : noCacheMap.entrySet()) {
                    K key = entry.getKey();
                    V value = entry.getValue();
                    if (value != null) {
                        map.put(key, value);
                        String v = JSON.toJSONString(value);
                        localCache.put(prefix + "_" + key + "_" + suffix, v);
                    }
                }
            }
        }
        return map;
    }
}
