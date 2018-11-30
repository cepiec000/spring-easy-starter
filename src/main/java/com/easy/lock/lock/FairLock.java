package com.easy.lock.lock;

import com.easy.lock.entity.LockInfo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @author chendd
 * @Date Created in 2018/11/19 15:39
 * @Description: 公平锁
 */
public class FairLock extends ILock {

    private RLock rLock;

    public FairLock(RedissonClient redissonClient, LockInfo lockInfo) {
        this.redissonClient = redissonClient;
        this.lockInfo = lockInfo;
    }

    @Override
    public boolean acquire() {
        try {
            rLock = redissonClient.getFairLock(lockInfo.getLockKey());
            return rLock.tryLock(lockInfo.getWaitTime(), lockInfo.getLeaseTime(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public void release() {
        if (rLock.isHeldByCurrentThread()) {
            rLock.unlockAsync();
        }
    }
}
