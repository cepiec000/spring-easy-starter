package com.easy.ratelimiter.handler;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author chendd
 * @Date Created in 2018/11/30 16:08
 * @Description:
 */
public class GuavaRateLimiterHandler extends AbstractRateLimiterHandler{
    private static final Logger logger = LoggerFactory.getLogger(GuavaRateLimiterHandler.class);
    private ConcurrentHashMap<String, com.google.common.util.concurrent.RateLimiter> limiters = new ConcurrentHashMap<String, com.google.common.util.concurrent.RateLimiter>();


    @Override
    public Object proceed(ProceedingJoinPoint point, com.easy.ratelimiter.annotation.RateLimiter rateLimiter) throws Throwable {
        initProceed(rateLimiter);
        com.google.common.util.concurrent.RateLimiter value = createLimiter(rateLimiter);
        if (rateLimiter == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("RateLimterHandler[单例限流处理器]获取令牌失败，返回方法执行结果");
            }
            return point.proceed();
        }
        //拿到令牌执行方法
        if (value.tryAcquire(rateLimiter.expire(), TimeUnit.SECONDS)) {
            if (logger.isDebugEnabled()) {
                logger.debug("RateLimterHandler[单例限流处理器]限流执行获取令牌成功，返回方法执行结果");
            }
            return point.proceed();
        }
        return fallBackMethodResult(point, rateLimiter);
    }

    /**
     * 构造RateLimiter,保证多线程环境下相同key对应的value不会被覆盖,且返回值相同
     *
     * @return
     */
    private com.google.common.util.concurrent.RateLimiter createLimiter(com.easy.ratelimiter.annotation.RateLimiter rateLimiter) {
        com.google.common.util.concurrent.RateLimiter result = limiters.get(rateLimiter.key());
        if (result == null) {
            com.google.common.util.concurrent.RateLimiter value = com.google.common.util.concurrent.RateLimiter.create(rateLimiter.limit());
            result = value;
            com.google.common.util.concurrent.RateLimiter putByOtherThread = limiters.putIfAbsent(rateLimiter.key(), value);
            //有其他线程写入了值
            if (putByOtherThread != null) {
                result = putByOtherThread;
            }
        }
        return result;
    }
}
