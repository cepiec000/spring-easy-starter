package com.easy.cache.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import com.easy.cache.entity.CacheKey;
import com.easy.cache.entity.CacheWrapper;
import com.easy.cache.exception.CacheCenterConnectionException;
import com.google.common.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

/**
 * @author chendd
 * @Date Created in 2018/11/13 17:51
 * @Description:
 */
@Component
public class RedisCacheManager implements ICacheManager {

    private static final Logger logger = LoggerFactory.getLogger(RedisCacheManager.class);

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public void setCache(CacheKey cacheKey, CacheWrapper<Object> result, Method method, Object[] args) throws CacheCenterConnectionException {
        if (null == cacheKey) {
            return;
        }
        String key = cacheKey.getCacheKey();
        if (StringUtils.isBlank(key)) {
            return;
        }
        try {
            int expire = cacheKey.getExpire();
            String hField = cacheKey.gethField();
            if (StringUtils.isBlank(hField)) {
                if (expire == 0) {
                    redisTemplate.opsForValue().set(key, JSON.toJSONString(result));
                } else {
                    redisTemplate.opsForValue().set(key, JSON.toJSONString(result), expire, TimeUnit.SECONDS);
                }
            } else {
                if (expire == 0) {
                    redisTemplate.opsForHash().put(key, hField,JSON.toJSONString(result));
                } else if (expire > 0) {
                    redisTemplate.opsForHash().put(key, hField, JSON.toJSONString(result));
                    redisTemplate.expire(key, expire, TimeUnit.SECONDS);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public CacheWrapper<Object> get(CacheKey cacheKey, Method method, Object[] args) throws CacheCenterConnectionException {
        if (null == cacheKey) {
            return null;
        }
        String key = cacheKey.getCacheKey();
        if (StringUtils.isBlank(key)) {
            return null;
        }
        Object res = null;
        try {
            String hField = cacheKey.gethField();
            if (StringUtils.isBlank(hField)) {
                res = redisTemplate.opsForValue().get(key);
            } else {
                res = redisTemplate.opsForHash().get(key, hField);
            }
            if (res != null) {
                return JSON.parseObject(res.toString(), new TypeToken<CacheWrapper<Object>>(){}.getType());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void delete(CacheKey cacheKey) throws CacheCenterConnectionException {
        if (null == cacheKey) {
            return;
        }
        String key = cacheKey.getCacheKey();
        if (StringUtils.isBlank(key)) {
            return;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("delete cache {}", key);
        }
        try {
            String hField = cacheKey.gethField();
            if (StringUtils.isBlank(hField)) {
                redisTemplate.delete(key);
            } else {
                redisTemplate.opsForHash().delete(key, hField);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
