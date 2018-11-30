package com.easy.cache.annotation;

import com.easy.cache.enums.CacheOpType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author chendd
 * @Date Created in 2018/11/13 16:32
 * @Description: 缓存注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.TYPE})
public @interface Cache {
    /**
     * key namespace_key_{}_type
     * @return key 默认 namespan:key:
     */
    String[] keys() default "";

    /**
     * 设置哈希表中的字段，如果设置此项，则用哈希表进行存储，支持表达式
     *
     * @return String
     */
    String [] hFields() default "";

    /**
     * 过期时间 秒 0为永不过期
     * @return int
     */
    int expire() default 600;

    /**
     * 描述
     * @return desc
     */
    String desc() default "";


    /**
     *
     * 缓存类型  默认 READ_WRITE
     *      READ_WRITE  ：先读取缓存数据，如果为空则从DB中取 并写入缓存；
     *      WRITE       : 从数据源中加载最新的数据，并写入缓存。
     *      LOAD        : 加载数据
     * @return CacheOpType
     */
    CacheOpType opType() default CacheOpType.READ_WRITE;
}
