package com.easy.cache.entity;

import java.io.Serializable;

/**
 * @author chendd
 * @Date Created in 2018/11/14 17:27
 * @Description:
 */
public class CacheWrapper<T> implements Serializable {
    private static final long serialVersionUID = -7429378488427823521L;

    /**
     * 缓存数据
     */
    private T cacheObject;

    /**
     * 最后加载时间
     */
    private long lastLoadTime;

    /**
     * 缓存时长
     */
    private int expire;

    public CacheWrapper() {
    }

    public CacheWrapper(T cacheObject, int expire) {
        this.cacheObject = cacheObject;
        this.lastLoadTime = System.currentTimeMillis();
        this.expire = expire;
    }

    /**
     * 判断缓存是否已经过期
     *
     * @return boolean
     */
    public boolean isExpired() {
        return expire > 0 && (System.currentTimeMillis() - lastLoadTime) > expire * 1000;
    }

    public T getCacheObject() {
        return cacheObject;
    }

    public void setCacheObject(T cacheObject) {
        this.cacheObject = cacheObject;
    }

    public long getLastLoadTime() {
        return lastLoadTime;
    }

    public void setLastLoadTime(long lastLoadTime) {
        this.lastLoadTime = lastLoadTime;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }
}
