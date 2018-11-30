package com.easy.cache.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chendd
 * @Date Created in 2018/11/14 18:02
 * @Description:
 */
@Component
public class CacheConfig {
    public static String namespace;
    public static Integer poolSize;
    public static Integer maxPoolSize;
    public static Integer keepAliveTime;
    public static Integer queueCapacity;

    private static ThreadPoolExecutor addCacheThreadPool;

    @Value("${easy.namespace}")
    public void setNamespace(String namespace) {
        CacheConfig.namespace = namespace;
    }

    @Value("${easy.poolSize}")
    public void setPoolSize(Integer poolSize) {
        CacheConfig.poolSize = poolSize;
    }

    @Value("${easy.maxPoolSize}")
    public void setMaxPoolSize(Integer maxPoolSize) {
        CacheConfig.maxPoolSize = maxPoolSize;
    }

    @Value("${easy.keepAliveTime}")
    public void setKeepAliveTime(Integer keepAliveTime) {
        CacheConfig.keepAliveTime = keepAliveTime;
    }

    @Value("${easy.queueCapacity}")
    public void setQueueCapacity(Integer queueCapacity) {
        CacheConfig.queueCapacity = queueCapacity;
    }

    public static ThreadPoolExecutor getAddCacheThreadPool() {
        if (addCacheThreadPool == null) {
            int corePoolSize = CacheConfig.poolSize;// 线程池的基本大小
            int maximumPoolSize = CacheConfig.maxPoolSize;// 线程池最大大小,线程池允许创建的最大线程数。如果队列满了，并且已创建的线程数小于最大线程数，则线程池会再创建新的线程执行任务。值得注意的是如果使用了无界的任务队列这个参数就没什么效果。
            int keepAliveTime = CacheConfig.keepAliveTime;
            TimeUnit unit = TimeUnit.MINUTES;
            int queueCapacity = CacheConfig.queueCapacity;// 队列容量
            LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(queueCapacity);
            addCacheThreadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, queue,
                    new ThreadFactory() {
                        private final AtomicInteger threadNumber = new AtomicInteger(1);
                        private final String namePrefix = "set-cache-hHandler-";
                        @Override
                        public Thread newThread(Runnable r) {
                            Thread t = new Thread(r, namePrefix + threadNumber.getAndIncrement());
                            t.setDaemon(true);
                            return t;
                        }
                    });
        }
        return addCacheThreadPool;
    }
}
