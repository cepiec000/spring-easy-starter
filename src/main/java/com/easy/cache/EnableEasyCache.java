package com.easy.cache;

import com.easy.cache.config.AutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;
/**
 * @author chendd
 * @Date Created in 2018/11/14 11:04
 * @Description:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(AutoConfiguration.class)
@Documented
@Inherited
public @interface EnableEasyCache {
}
