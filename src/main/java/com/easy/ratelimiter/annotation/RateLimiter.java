package com.easy.ratelimiter.annotation;

import com.easy.ratelimiter.aop.RateLimiterType;

import java.lang.annotation.*;

/**
 * @author chendd
 * @Date Created in 2018/11/30 10:14
 * @Description: 基于 redisTemplate 的 分布式限流注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {
    /**
     * 限流key
     * @return
     */
    String key() default "rate:limiter";

    /**
     * 过期时间内 允许 通过的请求数
     * @return
     */
    long limit() default 10;

    /**
     * 过期时间 秒
     * @return
     */
    long expire() default 1;

    /**
     * 降级方法
     * @return
     */
    String fallBackMethod();

    /**
     * 限流类型
     * @return
     */
    RateLimiterType type() default RateLimiterType.SINGLETON_RATELIMITER;
}
