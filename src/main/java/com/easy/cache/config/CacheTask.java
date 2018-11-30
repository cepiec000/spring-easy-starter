package com.easy.cache.config;

import com.easy.cache.aop.AspectJoinPointAdapter;
import com.easy.cache.entity.CacheKey;
import com.easy.cache.entity.CacheWrapper;
import com.easy.cache.manager.ICacheManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * @author chendd
 * @Date Created in 2018/11/16 17:59
 * @Description:
 */
public class CacheTask implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(CacheTask.class);

    private ICacheManager cacheManager;
    private CacheKey cacheKey;
    private AspectJoinPointAdapter pjp;
    private int key;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public CacheTask(AspectJoinPointAdapter pjp, ICacheManager cacheManager, CacheKey cacheKey) {
        if (cacheKey != null) {
            this.cacheKey = cacheKey;
            this.key=cacheKey.hashCode();
            this.pjp = pjp;
            this.cacheManager = cacheManager;
        }
    }

    @Override
    public void run() {
        try {
            long startTime = System.currentTimeMillis();
            Object[] arguments = pjp.getArgs();
            Object data = pjp.doProceed(arguments);
            if (data == null) {
                return;
            }
            //判断线程是否重复添加
            CacheWrapper<Object> result = new CacheWrapper<>(data, cacheKey.getExpire());
            cacheManager.setCache(cacheKey, result, pjp.getMethod(), pjp.getArgs());
            String hField = cacheKey.gethField();
            long useTime = System.currentTimeMillis() - startTime;
            if (StringUtils.isBlank(hField)) {
                logger.info("refresh cache data success key:{} use time: {}ms", cacheKey.getCacheKey(), useTime);
            } else {
                logger.info("refresh cache data success key:{} hField:{} use time: {}ms", cacheKey.getCacheKey(), hField, useTime);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
