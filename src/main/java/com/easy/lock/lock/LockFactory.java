package com.easy.lock.lock;

import com.easy.cache.parser.SpelKeyGenerator;
import com.easy.lock.annotation.ELock;
import com.easy.lock.entity.LockInfo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.redisson.api.RedissonClient;

/**
 * @author chendd
 * @Date Created in 2018/11/19 15:49
 * @Description:
 */
public class LockFactory {

    private RedissonClient redissonClient;

    public LockFactory(RedissonClient client) {
        this.redissonClient = client;
    }

    private SpelKeyGenerator lockKeyGenerator = new SpelKeyGenerator();


    public ILock getLock(ProceedingJoinPoint point, ELock klock) {
        LockInfo lockInfo = lockKeyGenerator.getLockInfo(point, klock);
        if (lockInfo == null || redissonClient == null) {
            return null;
        }
        switch (lockInfo.getType()) {
            case Reentrant:
                return new ReentrantLock(redissonClient, lockInfo);
            case Fair:
                return new FairLock(redissonClient, lockInfo);
            case Read:
                return new ReadLock(redissonClient, lockInfo);
            case Write:
                return new WriteLock(redissonClient, lockInfo);
            default:
                return new ReentrantLock(redissonClient, lockInfo);
        }
    }


}
