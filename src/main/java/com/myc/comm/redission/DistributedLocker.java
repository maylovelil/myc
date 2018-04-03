package com.myc.comm.redission;

/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/4/2 18:03
 */

import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;

public interface DistributedLocker {
    RLock lock(String lockKey);

    RLock lock(String lockKey, int timeout);

    RLock lock(String lockKey, TimeUnit unit, int timeout);

    boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime);

    void unlock(String lockKey);

    void unlock(RLock lock);
}
