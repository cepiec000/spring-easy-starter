package com.easy.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.PostConstruct;

/**
 * @author chendd
 * @Date Created in 2018/11/30 11:16
 * @Description:
 */
@Configuration
public class RedisTemplateConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisTemplateConfig.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @PostConstruct
    private void initRedisTemplate() {
        if (redisTemplate != null) {
            RedisSerializer<String> redisSerializer = new StringRedisSerializer();
            FastJson2RedisSerializer serializer=new FastJson2RedisSerializer(Object.class);
            redisTemplate.setValueSerializer(serializer);
            //使用StringRedisSerializer来序列化和反序列化redis的key值
            redisTemplate.setKeySerializer(redisSerializer);
            redisTemplate.setHashValueSerializer(serializer);
            redisTemplate.setHashKeySerializer(serializer);
            redisTemplate.afterPropertiesSet();
            LOGGER.info("Springboot RedisTemplate 加载完成");
        }
    }
}
