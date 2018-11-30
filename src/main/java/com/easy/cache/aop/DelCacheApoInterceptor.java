package com.easy.cache.aop;

import com.easy.cache.annotation.DelCache;
import com.easy.cache.config.CacheHandler;
import com.easy.constant.EasyConstant;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author chendd
 * @Date Created in 2018/11/14 10:43
 * @Description:
 */
@Component
@Aspect
@Order(EasyConstant.DEFAULT_ORDER+2)
public class DelCacheApoInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(DelCacheApoInterceptor.class);

    @Autowired
    private CacheHandler cacheHandler;

    @Pointcut("@annotation(com.easy.cache.annotation.DelCache)")
    public void aspect() {
    }

    @AfterReturning(value = "aspect() && @annotation(delCache)", returning = "retVal")
    public void interception(JoinPoint point, DelCache delCache, Object retVal) throws Throwable {
        if (delCache != null) {
            cacheHandler.delProceed(new AspectJoinPointAdapter(point), delCache, retVal);
        }
    }

}
