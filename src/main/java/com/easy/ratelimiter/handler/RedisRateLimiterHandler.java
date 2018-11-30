package com.easy.ratelimiter.handler;

import com.easy.constant.EasyConstant;
import com.easy.ratelimiter.annotation.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chendd
 * @Date Created in 2018/11/30 10:31
 * @Description: 分布式限流 处理类
 */
public class RedisRateLimiterHandler extends AbstractRateLimiterHandler{

    private static final Logger logger = LoggerFactory.getLogger(RedisRateLimiterHandler.class);

    private RedisTemplate redisTemplate;

    public RedisRateLimiterHandler(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * lau 脚本 表达式
     */
    private DefaultRedisScript<Long> redisScript;

    @PostConstruct
    public void init() {
        redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Long.class);
        redisScript.setScriptText(EasyConstant.RATE_LIMITER_LUA);
    }

    /**
     * 分布式 获取限流结果
     * @param limit
     * @param expire
     * @param keys
     * @return
     */
    private boolean checkRateLimiter(long limit, long expire, List<String> keys) {
        Long result = (Long) redisTemplate.execute(redisScript, keys, expire, limit);
        if (result == 0) {
            logger.debug("由于超过单位时间={}-允许的请求次数={}[触发限流]", expire, limit);
            return false;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("RateLimterHandler[分布式限流处理器]限流执行结果-result={},请求[正常]响应", result);
        }
        return true;
    }


    @Override
    public Object proceed(ProceedingJoinPoint point, RateLimiter rateLimiter) throws Throwable {
        initProceed(rateLimiter);
        List<String> keys = getKeys(rateLimiter);
        if (checkRateLimiter(rateLimiter.limit(), rateLimiter.expire(), keys)) {
            return point.proceed();
        }

        return fallBackMethodResult(point, rateLimiter);
    }

    private List<String> getKeys(com.easy.ratelimiter.annotation.RateLimiter rateLimiter) {
        List<String> keys = new ArrayList<>();
        keys.add(rateLimiter.key());
        return keys;
    }
}
