package com.easy.lock.annotation;

import com.easy.lock.entity.LockType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author chendd
 * @Date Created in 2018/11/19 10:03
 * @Description: 锁注解
 */
@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ELock {
    /**
     * KEY 默认包名+方法名
     */
    String[] keys() default "";

    /**
     * 锁类型，默认可重入锁
     * @return
     */
    LockType lockType() default LockType.Reentrant;

    /**
     * 过期时间 单位：毫秒
     * <pre>
     *     过期时间一定是要长于业务的执行时间.
     * </pre>
     */
    long leaseTime() default 30000;

    /**
     * 获取锁超时时间 单位：毫秒
     * <pre>
     *     结合业务,建议该时间不宜设置过长,特别在并发高的情况下.
     * </pre>
     */
    long waitTime() default 3000;
}
