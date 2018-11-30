package com.easy.lock;

import com.easy.lock.config.LockAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author chendd
 * @Date Created in 2018/11/19 16:42
 * @Description:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(LockAutoConfiguration.class)
@Documented
@Inherited
public @interface EnableEasyLock {
}
