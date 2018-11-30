package com.easy.ratelimiter.aop;

import com.easy.constant.EasyConstant;
import com.easy.ratelimiter.handler.GuavaRateLimiterHandler;
import com.easy.ratelimiter.handler.RedisRateLimiterHandler;
import com.easy.ratelimiter.annotation.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author chendd
 * @Date Created in 2018/11/30 10:17
 * @Description:
 */
@Component
@Aspect
@Order(EasyConstant.DEFAULT_ORDER)
public class RateLimiterInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(RateLimiterInterceptor.class);

    @Autowired
    private RedisRateLimiterHandler rateLimterHandler;
    private GuavaRateLimiterHandler guavaRateLimiterHandler = new GuavaRateLimiterHandler();

    @Pointcut("@annotation(com.easy.ratelimiter.annotation.RateLimiter)")
    public void aspect() {
    }

    @Around("aspect() && @annotation(rateLimiter)")
    public Object interception(ProceedingJoinPoint point, RateLimiter rateLimiter) throws Throwable {
        Signature signature = point.getSignature();
        if (!(signature instanceof MethodSignature)) {
            throw new IllegalArgumentException("the Annotation @RateLimter must used on method!");
        }
        Object result = null;
        switch (rateLimiter.type()) {
            case SINGLETON_RATELIMITER:
                result = guavaRateLimiterHandler.proceed(point, rateLimiter);
                break;
            case DISTRIBUTED_RATELIMITER:
                result = rateLimterHandler.proceed(point, rateLimiter);
                break;
            default:
                result = guavaRateLimiterHandler.proceed(point, rateLimiter);
                break;
        }
        return result;
    }
}
