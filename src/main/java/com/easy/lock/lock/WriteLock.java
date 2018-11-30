package com.easy.lock.lock;

import com.easy.lock.entity.LockInfo;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @author chendd
 * @Date Created in 2018/11/19 15:48
 * @Description: 写锁
 */
public class WriteLock extends ILock {
    private RReadWriteLock rLock;

    public WriteLock(RedissonClient redissonClient, LockInfo info) {
        this.redissonClient = redissonClient;
        this.lockInfo = info;
    }

    @Override
    public boolean acquire() {
        try {
            rLock=redissonClient.getReadWriteLock(lockInfo.getLockKey());
            return rLock.writeLock().tryLock(lockInfo.getWaitTime(), lockInfo.getLeaseTime(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public void release() {
        if(rLock.writeLock().isHeldByCurrentThread()){
            rLock.writeLock().unlockAsync();
        }
    }
}
