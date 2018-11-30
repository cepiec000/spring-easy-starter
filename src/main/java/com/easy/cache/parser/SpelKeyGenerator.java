package com.easy.cache.parser;

import com.easy.cache.annotation.Cache;
import com.easy.cache.annotation.DelCache;
import com.easy.cache.aop.AspectJoinPointAdapter;
import com.easy.cache.config.CacheConfig;
import com.easy.cache.entity.CacheKey;
import com.easy.lock.annotation.ELock;
import com.easy.lock.entity.LockInfo;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author chendd
 * @Date Created in 2018/11/19 10:23
 * @Description: key生存规则 使用spring spel语法解析
 */
public class SpelKeyGenerator {

    private static final ParameterNameDiscoverer NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    private static final ExpressionParser PARSER = new SpelExpressionParser();


    /**
     * cache 生成 cachekey
     *
     * @param ajpa
     * @param cache
     * @return
     */
    public CacheKey getCacheKey(AspectJoinPointAdapter ajpa, Cache cache) {
        String key = getCacheKeyName(ajpa, cache, 1);
        if (StringUtils.isBlank(key)) {
            return null;
        }
        String hField = getCacheKeyName(ajpa, cache, 2);
        return new CacheKey(CacheConfig.namespace, key, hField, cache.expire());
    }

    /**
     * delcache 生成 cachekey
     *
     * @param ajpa
     * @param delCache
     * @return
     */
    public CacheKey getCacheKey(AspectJoinPointAdapter ajpa, DelCache delCache) {
        String key = getCacheKeyName(ajpa, delCache, 1);
        if (StringUtils.isBlank(key)) {
            return null;
        }
        String hField = getCacheKeyName(ajpa, delCache, 2);
        return new CacheKey(CacheConfig.namespace, key, hField, 0);
    }

    /**
     * 分布式锁 对象生成
     *
     * @param point
     * @param eLock
     * @return
     */
    public LockInfo getLockInfo(ProceedingJoinPoint point, ELock eLock) {
        String name = getKeyName(point, eLock);
        if (StringUtils.isBlank(name)) {
            return null;
        }
        return new LockInfo(eLock.lockType(), name, eLock.leaseTime(), eLock.waitTime());
    }

    private String getCacheKeyName(AspectJoinPointAdapter ajpa, Cache cache, int type) {
        StringBuilder sb = new StringBuilder();
        Method method = ajpa.getMethod();
        if (type == 1 && cache.keys().length > 0) {
            sb.append(getSpelDefinitionKey(cache.keys(), method, ajpa.getArgs()));
        } else if (type == 2 && cache.hFields().length > 0) {
            sb.append(getSpelDefinitionKey(cache.hFields(), method, ajpa.getArgs()));
        }
        return sb.toString();
    }

    private String getCacheKeyName(AspectJoinPointAdapter ajpa, DelCache delCache, int type) {
        StringBuilder sb = new StringBuilder();
        Method method = ajpa.getMethod();
        if (type == 1 && delCache.keys().length > 0) {
            sb.append(getSpelDefinitionKey(delCache.keys(), method, ajpa.getArgs()));
        } else if (type == 2 && delCache.hFields().length > 0) {
            sb.append(getSpelDefinitionKey(delCache.hFields(), method, ajpa.getArgs()));
        }
        return sb.toString();
    }

    private String getKeyName(ProceedingJoinPoint jp, ELock eLock) {
        StringBuilder sb = new StringBuilder();
        Signature signature = jp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        sb.append(method.getDeclaringClass().getName()).append(".").append(method.getName());
        if (eLock.keys().length > 0) {
            sb.append(getSpelDefinitionKey(eLock.keys(), method, jp.getArgs()));
        }
        return sb.toString();
    }

    private String getSpelDefinitionKey(String[] definitionKeys, Method method, Object[] parameterValues) {
        EvaluationContext context = new MethodBasedEvaluationContext(null, method, parameterValues, NAME_DISCOVERER);
        List<String> definitionKeyList = new ArrayList<>(definitionKeys.length);
        for (String definitionKey : definitionKeys) {
            if (StringUtils.isNotBlank(definitionKey)) {
                String key = definitionKey.contains("#") ? PARSER.parseExpression(definitionKey).getValue(context).toString() : definitionKey;
                definitionKeyList.add(key);
            }
        }
        return collectionToString(definitionKeyList);
    }

    /**
     * 拼接key
     *
     * @param coll
     * @return
     */
    private String collectionToString(@Nullable Collection<?> coll) {
        if (coll == null || coll.isEmpty()) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            Iterator it = coll.iterator();
            while (it.hasNext()) {
                sb.append(it.next());
            }
            return sb.toString();
        }
    }
}
