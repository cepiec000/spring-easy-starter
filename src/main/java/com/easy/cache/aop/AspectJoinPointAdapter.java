package com.easy.cache.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @author chendd
 * @Date Created in 2018/11/14 10:55
 * @Description: joinPoin 适配类
 */
public class AspectJoinPointAdapter {
    private final ProceedingJoinPoint jp;

    private Method method;

    public AspectJoinPointAdapter(ProceedingJoinPoint jp) {
        this.jp = jp;
    }

    public AspectJoinPointAdapter(JoinPoint jp){
        this.jp=(ProceedingJoinPoint) jp;
    }

    public Object[] getArgs() {
        return jp.getArgs();
    }

    public Object getTarget() {
        return jp.getTarget();
    }

    public Method getMethod() {
        if (null == method) {
            Signature signature = jp.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            this.method = methodSignature.getMethod();
        }
        return method;
    }

    public Object doProceed(Object[] arguments) throws Throwable {
        return jp.proceed(arguments);
    }
}
