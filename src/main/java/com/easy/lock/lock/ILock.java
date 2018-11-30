package com.easy.lock.lock;

import com.easy.lock.entity.LockInfo;
import org.redisson.api.RedissonClient;

/**
 * @author chendd
 * @Date Created in 2018/11/19 15:39
 * @Description: 锁接口类
 */
public abstract class ILock {

    protected  LockInfo lockInfo;

    protected RedissonClient redissonClient;

    public abstract boolean acquire();

    public abstract void release();

    public LockInfo getLockInfo() {
        return lockInfo;
    }

    public void setLockInfo(LockInfo lockInfo) {
        this.lockInfo = lockInfo;
    }

    public RedissonClient getRedissonClient() {
        return redissonClient;
    }

    public void setRedissonClient(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

}
