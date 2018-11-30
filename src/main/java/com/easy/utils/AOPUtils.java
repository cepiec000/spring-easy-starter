package com.easy.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @author chendd
 * @Date Created in 2018/11/30 15:21
 * @Description:
 */
public class AOPUtils {
    /**
     * 获取指定方法名的Method对象
     *
     * @param joinPoint
     * @param methodName
     * @return
     */
    public static Method getMethodFromTarget(JoinPoint joinPoint, String methodName) {
        Method method = null;
        if (joinPoint.getSignature() instanceof MethodSignature) {
            method = getDeclaredMethod(joinPoint.getTarget().getClass(), methodName, getParameterTypes(joinPoint));
        }
        return method;
    }

    private static Method getDeclaredMethod(Class<?> type, String methodName, Class<?>... parameterTypes) {
        Method method = null;
        try {
            method = type.getDeclaredMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            Class<?> superclass = type.getSuperclass();
            if (superclass != null) {
                method = getDeclaredMethod(superclass, methodName, parameterTypes);
            }
        }
        return method;
    }

    private static Class[] getParameterTypes(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return method.getParameterTypes();
    }
}
