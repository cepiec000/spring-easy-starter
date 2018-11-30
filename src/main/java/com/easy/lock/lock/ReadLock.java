package com.easy.lock.lock;

import com.easy.lock.entity.LockInfo;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @author chendd
 * @Date Created in 2018/11/19 15:44
 * @Description: 读锁
 */
public class ReadLock extends ILock {


    private RReadWriteLock rrLock;

    public ReadLock(RedissonClient redissonClient, LockInfo info) {
        this.redissonClient = redissonClient;
        this.lockInfo = info;
    }

    @Override
    public boolean acquire() {
        try {
            rrLock = redissonClient.getReadWriteLock(lockInfo.getLockKey());
            return rrLock.readLock().tryLock(lockInfo.getWaitTime(), lockInfo.getLeaseTime(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public void release() {
        if (rrLock.readLock().isHeldByCurrentThread()) {
            rrLock.readLock().unlockAsync();
        }
    }
}
