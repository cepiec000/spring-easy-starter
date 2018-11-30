package com.easy.cache.aop;

import com.easy.cache.annotation.Cache;
import com.easy.cache.config.CacheHandler;
import com.easy.constant.EasyConstant;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author chendd
 * @Date Created in 2018/11/13 16:38
 * @Description:
 */
@Component
@Aspect
@Order(EasyConstant.DEFAULT_ORDER + 3)
public class CacheAopInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(CacheAopInterceptor.class);

    @Autowired
    private CacheHandler cacheHandler;

    @Pointcut("@annotation(com.easy.cache.annotation.Cache)")
    public void aspect() {
    }

    @Around("aspect() && @annotation(cache)")
    public Object interception(ProceedingJoinPoint point, Cache cache) throws Throwable {
        try {
            if (cache != null) {
                return cacheHandler.proceed(new AspectJoinPointAdapter(point), cache);
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return point.proceed();
    }
}
