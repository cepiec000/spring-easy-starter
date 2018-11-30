package com.easy.lock.lock;

import com.easy.lock.entity.LockInfo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @author chendd
 * @Date Created in 2018/11/19 15:46
 * @Description: 可重入锁
 */
public class ReentrantLock extends ILock {
    private RLock rLock;
    public ReentrantLock(RedissonClient redissonClient, LockInfo lockInfo) {
        this.redissonClient = redissonClient;
        this.lockInfo = lockInfo;
    }
    @Override
    public boolean acquire() {
        try {
            rLock = redissonClient.getLock(lockInfo.getLockKey());
            return rLock.tryLock(lockInfo.getWaitTime(), lockInfo.getLeaseTime(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public void release() {
        if(rLock.isHeldByCurrentThread()){
            rLock.unlockAsync();
        }
    }
}
