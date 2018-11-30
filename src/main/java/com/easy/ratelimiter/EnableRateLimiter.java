package com.easy.ratelimiter;

import com.easy.ratelimiter.config.RateLimiterAutoConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author chendd
 * @Date Created in 2018/11/30 14:33
 * @Description:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(RateLimiterAutoConfig.class)
@Documented
@Inherited
public @interface EnableRateLimiter {
}
