package com.easy.ratelimiter.config;

import com.easy.ratelimiter.handler.RedisRateLimiterHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author chendd
 * @Date Created in 2018/11/30 14:34
 * @Description:
 */
@ComponentScan({"com.easy.ratelimiter"})
@EnableAspectJAutoProxy
@Configuration
public class RateLimiterAutoConfig {

    @Bean
    RedisRateLimiterHandler rateLimiterHandler(RedisTemplate redisTemplate) {
        return new RedisRateLimiterHandler(redisTemplate);
    }
}
