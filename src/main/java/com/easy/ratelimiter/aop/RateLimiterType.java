package com.easy.ratelimiter.aop;

/**
 * @author chendd
 * @Date Created in 2018/11/30 15:25
 * @Description:限流类型
 */
public enum RateLimiterType {
    SINGLETON_RATELIMITER,//单机接口采用 google guava 限流实现
    DISTRIBUTED_RATELIMITER;//分布式 采用redis 限流实现
}
