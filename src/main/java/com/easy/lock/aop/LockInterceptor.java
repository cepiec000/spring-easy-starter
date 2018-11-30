package com.easy.lock.aop;

import com.easy.cache.annotation.Cache;
import com.easy.constant.EasyConstant;
import com.easy.lock.annotation.ELock;
import com.easy.lock.lock.ILock;
import com.easy.lock.lock.LockFactory;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author chendd
 * @Date Created in 2018/11/19 10:07
 * @Description:
 */
@Component
@Aspect
@Order(EasyConstant.DEFAULT_ORDER + 1)
public class LockInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LockInterceptor.class);

    @Autowired
    private  LockFactory lockFactory;

    @Pointcut("@annotation(com.easy.lock.annotation.ELock)")
    public void aspect() {
    }

    @Around("aspect() && @annotation(elock)")
    public Object interception(ProceedingJoinPoint point, ELock elock) throws Throwable {
        Assert.isTrue(elock.waitTime() > 0, "tryWaitTime must more than 0");
        boolean currentThreadLock = false;
        ILock lock = null;
        try {
            if (lockFactory != null) {
                lock = lockFactory.getLock(point, elock);
            }
            if (lock != null) {
                currentThreadLock = lock.acquire();
            }
            return point.proceed();
        } finally {
            if (currentThreadLock) {
                lock.release();
            }
        }
    }

}
