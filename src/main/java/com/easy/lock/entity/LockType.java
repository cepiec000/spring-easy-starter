package com.easy.lock.entity;

/**
 * @author chendd
 * @Date Created in 2018/11/19 15:15
 * @Description:锁类型
 */
public enum  LockType {
    /**
     * 可重入锁
     */
    Reentrant,
    /**
     * 公平锁
     */
    Fair,
    /**
     * 读锁
     */
    Read,
    /**
     * 写锁
     */
    Write;

    LockType() {
    }
}
