package com.easy.cache.config;

import com.alibaba.fastjson.JSON;
import com.easy.cache.annotation.Cache;
import com.easy.cache.annotation.DelCache;
import com.easy.cache.aop.AspectJoinPointAdapter;
import com.easy.cache.entity.CacheKey;
import com.easy.cache.entity.CacheWrapper;
import com.easy.cache.enums.CacheOpType;
import com.easy.cache.manager.ICacheManager;
import com.easy.cache.parser.SpelKeyGenerator;
import com.easy.lock.annotation.ELock;
import com.easy.lock.entity.LockType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @author chendd
 * @Date Created in 2018/11/13 17:36
 * @Description:
 */
public class CacheHandler {

    private static Logger logger = LoggerFactory.getLogger(CacheHandler.class);

    private ICacheManager cacheManager;

    public CacheHandler(ICacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    private static SpelKeyGenerator spelKeyGenerator=new SpelKeyGenerator();
    /**
     * 获取最新数据并写入缓存
     *
     * @param pjp
     * @param cache
     * @return
     * @throws Throwable
     */
    private Object writeAndSetCache(AspectJoinPointAdapter pjp, Cache cache) throws Throwable {
        CacheKey cacheKey = spelKeyGenerator.getCacheKey(pjp, cache);
        Object[] arguments = pjp.getArgs();
        Method method = pjp.getMethod();
        Object result = getData(pjp);
        if (result != null) {
            cacheManager.setCache(cacheKey, new CacheWrapper<Object>(result, cache.expire()), method, arguments);
        }
        return result;
    }

    /**
     * 处理@Cache 拦截
     *
     * @param pjp   切面
     * @param cache 注解
     * @return T 返回值
     * @throws Exception 异常
     */
    public Object proceed(AspectJoinPointAdapter pjp, Cache cache) throws Throwable {
        CacheKey cacheKey = spelKeyGenerator.getCacheKey(pjp, cache);
        Object[] arguments = pjp.getArgs();
        CacheOpType opType = getCacheOpType(cache, arguments);

        if (opType == CacheOpType.LOAD) {
            return getData(pjp);
        } else if (opType == CacheOpType.WRITE) {
            return writeAndSetCache(pjp, cache);
        }
        //cacheKey 为空
        if (cacheKey == null) {
            return getData(pjp);
        }
        CacheWrapper<Object> result = this.get(cacheKey, pjp.getMethod(), arguments);
        this.setCache(cacheKey, pjp, arguments, result);
        if (logger.isTraceEnabled()) {
            logger.trace("cache key:{} , cache data is null{}", cacheKey.getCacheKey(), null == result);
        }
        if (result != null && result.getCacheObject() != null) {
            Type returnType = null;
            if (null != pjp.getMethod()) {
                returnType = pjp.getMethod().getGenericReturnType();
            }
            return JSON.parseObject(result.getCacheObject().toString(), returnType);
        } else {
            return null;
        }
    }

    /**
     * 异步写入缓存
     *
     * @param cacheKey
     * @param pjp
     * @param arguments
     * @param result
     * @throws Throwable
     */
    private void setCache(CacheKey cacheKey, AspectJoinPointAdapter pjp, Object[] arguments, CacheWrapper<Object> result) throws Throwable {
        //为了防止缓存穿透，高并发访问数据库，则短时间内 放一个空对象在 此KEY缓存中，并异步 不更新缓存。
        if (null == result) {
            result = new CacheWrapper<>(null, cacheKey.getExpire());
            cacheManager.setCache(cacheKey, result, pjp.getMethod(), arguments);
            lockCacheTask(new CacheTask(pjp, cacheManager, cacheKey));
        }
    }

    /**
     * 异步操作刷新缓存 增加分布式锁，需要@EnableEasyLock 开启锁，否则分布式锁不生效
     * @param cacheTask
     */
    @ELock(keys = {"#cacheTask.key"},lockType = LockType.Reentrant)
    private void lockCacheTask(CacheTask cacheTask){
        CacheConfig.getAddCacheThreadPool().execute(cacheTask);
    }

    /**
     * 获取缓存数据
     *
     * @param cacheKey
     * @param method
     * @param args
     * @return
     */
    public CacheWrapper<Object> get(CacheKey cacheKey, Method method, Object[] args) {
        CacheWrapper<Object> result = null;
        try {
            long startTime = System.currentTimeMillis();
            result = cacheManager.get(cacheKey, method, args);
            long useTime = System.currentTimeMillis() - startTime;
            String hField = cacheKey.gethField();
            if (result != null) {
                if (StringUtils.isBlank(hField)) {
                    logger.info("Hit cache key:{} use time: {}ms", cacheKey.getCacheKey(), useTime);
                } else {
                    logger.info("Hit hash cache key:{} hField:{} use time: {}ms", cacheKey.getCacheKey(), hField, useTime);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 处理@DelCache
     *
     * @param pjp
     * @param delCache
     * @return
     * @throws Throwable
     */
    public void delProceed(AspectJoinPointAdapter pjp, DelCache delCache, Object retVal) throws Throwable {
        CacheKey cacheKey = spelKeyGenerator.getCacheKey(pjp,delCache);
        try {
            cacheManager.delete(cacheKey);
            logger.info("del cache key:{}", cacheKey);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }



    /**
     * 直接加载数据（加载后的数据不往缓存放）
     *
     * @param pjp CacheAopProxyChain
     * @return Object
     * @throws Throwable
     */
    private static Object getData(AspectJoinPointAdapter pjp) throws Throwable {
        try {
            long startTime = System.currentTimeMillis();
            Object[] arguments = pjp.getArgs();
            Object result = pjp.doProceed(arguments);
            long useTime = System.currentTimeMillis() - startTime;
            String className = pjp.getTarget().getClass().getName();
            logger.info("load db data {}.{} use time: {}ms", className, pjp.getMethod().getName(), useTime);
            return result;
        } catch (Throwable e) {
            throw e;
        }
    }

    /**
     * 获取 cache 类型
     *
     * @param cache
     * @param arguments
     * @return
     */
    private CacheOpType getCacheOpType(Cache cache, Object[] arguments) {
        CacheOpType opType = cache.opType();
        if (null != arguments && arguments.length > 0) {
            for (Object tmp : arguments) {
                if (null != tmp && tmp instanceof CacheOpType) {
                    opType = (CacheOpType) tmp;
                    break;
                }
            }
        }
        if (null == opType) {
            opType = CacheOpType.READ_WRITE;
        }
        return opType;
    }

}
