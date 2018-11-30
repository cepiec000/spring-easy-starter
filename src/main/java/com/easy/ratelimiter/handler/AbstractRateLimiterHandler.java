package com.easy.ratelimiter.handler;

import com.easy.ratelimiter.annotation.RateLimiter;
import com.easy.utils.AOPUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author chendd
 * @Date Created in 2018/11/30 16:10
 * @Description:
 */
public abstract class AbstractRateLimiterHandler {
    private static final Logger LOGGER= LoggerFactory.getLogger(AbstractRateLimiterHandler.class);

    protected void initProceed(RateLimiter rateLimiter) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RateLimiter 分布式限流器 开始执行限流操作");
        }
        if (StringUtils.isBlank(rateLimiter.key())) {
            throw new IllegalArgumentException("rate limiter key is null");
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("rate limiter 分布式限流 参数为key={}, limitTimes={},limitTimeout={}", rateLimiter.key(), rateLimiter.expire(), rateLimiter.expire());
        }
    }

    /**
     * 降级方法处理
     * @param point
     * @param rateLimiter
     * @return
     * @throws Throwable
     */
    protected Object fallBackMethodResult(ProceedingJoinPoint point, RateLimiter rateLimiter) throws Throwable {
        if (StringUtils.isNotBlank(rateLimiter.fallBackMethod())) {
            Object object = point.getTarget();
            Method method = AOPUtils.getMethodFromTarget(point, rateLimiter.fallBackMethod());
            if (method != null) {
                Object result = method.invoke(object, point.getArgs());
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("fallBack method executed,class:{},method:{}", object.getClass().getName(), rateLimiter.fallBackMethod());
                }
                return result;
            }
        }
        return null;
    }

    /**
     * 处理方法
     * @param point
     * @param rateLimiter
     * @return
     * @throws Throwable
     */
    public abstract Object proceed(ProceedingJoinPoint point, RateLimiter rateLimiter) throws Throwable ;
}
