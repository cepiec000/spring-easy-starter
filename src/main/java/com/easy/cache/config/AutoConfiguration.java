package com.easy.cache.config;

import com.easy.cache.manager.RedisCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author chendd
 * @Date Created in 2018/11/13 17:30
 * @Description:
 */
@ComponentScan({"com.easy.cache"})
@EnableAspectJAutoProxy
@Configuration
public class AutoConfiguration {
    @Autowired
    RedisCacheManager cacheManager;

    @Bean
    CacheHandler cacheHandler() {
        return new CacheHandler(cacheManager);
    }
}
