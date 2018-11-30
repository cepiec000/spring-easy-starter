package com.easy.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author chendd
 * @Date Created in 2018/11/13 16:32
 * @Description: 删除缓存
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.TYPE})
public @interface DelCache {
    /**
     * key namespace_key_{}_type
     * @return key
     */
    String[] keys() default "";


    /**
     * 设置哈希表中的字段，如果设置此项，则用哈希表进行存储，支持表达式
     *
     * @return String
     */
    String[] hFields() default "";

    /**
     * 描述
     * @return desc
     */
    String desc() default "";
}
