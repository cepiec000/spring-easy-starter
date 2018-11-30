package com.easy.cache.manager;

import com.easy.cache.entity.CacheKey;
import com.easy.cache.entity.CacheWrapper;
import com.easy.cache.exception.CacheCenterConnectionException;

import java.lang.reflect.Method;

/**
 * 缓存管理
 * 
 * @author chendd
 */
public interface ICacheManager {

    /**
     * 往缓存写数据
     * 
     * @param cacheKey 缓存Key
     * @param result 缓存数据
     * @param method Method
     * @param args args
     * @throws CacheCenterConnectionException 缓存异常
     */
    void setCache(final CacheKey cacheKey, final CacheWrapper<Object> result, final Method method,
                  final Object args[]) throws CacheCenterConnectionException;

    /**
     * 根据缓存Key获得缓存中的数据
     * 
     * @param key 缓存key
     * @param method Method
     * @param args args
     * @return 缓存数据
     * @throws CacheCenterConnectionException 缓存异常
     */
    CacheWrapper<Object> get(final CacheKey key, final Method method, final Object args[])
            throws CacheCenterConnectionException;

    /**
     * 删除缓存
     * 
     * @param key 缓存key
     * @throws CacheCenterConnectionException 缓存异常
     */
    void delete(final CacheKey key) throws CacheCenterConnectionException;

}
