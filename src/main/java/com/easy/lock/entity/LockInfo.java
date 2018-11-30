package com.easy.lock.entity;

/**
 * @author chendd
 * @Date Created in 2018/11/19 10:13
 * @Description:
 */
public class LockInfo {

    public LockInfo(LockType lockType,String lockKey, Long leaseTime, Long waitTime) {
        this.type=lockType;
        this.lockKey = lockKey;
        this.leaseTime = leaseTime;
        this.waitTime = waitTime;
    }

    private LockType type;
    /**
     * 锁名称
     */
    private String lockKey;

    /**
     * 过期释放时间
     */
    private Long leaseTime;

    /**
     * 获取锁阻塞超时时间
     */
    private Long waitTime;

    /**
     * 是否已经获取锁
     */
    private boolean lock=false;

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public String getLockKey() {
        return lockKey;
    }

    public void setLockKey(String lockKey) {
        this.lockKey = lockKey;
    }

    public Long getLeaseTime() {
        return leaseTime;
    }

    public void setLeaseTime(Long leaseTime) {
        this.leaseTime = leaseTime;
    }

    public Long getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(Long waitTime) {
        this.waitTime = waitTime;
    }

    public LockType getType() {
        return type;
    }

    public void setType(LockType type) {
        this.type = type;
    }
}
