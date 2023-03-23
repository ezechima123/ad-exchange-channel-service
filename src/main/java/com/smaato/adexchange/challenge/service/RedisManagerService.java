package com.smaato.adexchange.challenge.service;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RedisManagerService {
    @Autowired
    RedissonClient redissonClient;
    @Value("${redis.lock.timeout}")
    int redisLockTimeout;

    public boolean acquireLock(String referenceKey) {
        RLock lock = this.redissonClient.getLock(referenceKey);
        try {
            return lock.tryLock(0L, redisLockTimeout, TimeUnit.SECONDS);
        } catch (InterruptedException var7) {
            return false;
        }
    }

    public void unlock(String referenceKey) {
        RLock lock = redissonClient.getLock(referenceKey);
        try {
            if (lock.isLocked()) {
                lock.unlock();
            }
        } catch (IllegalMonitorStateException var7) {
        }
    }

    public void unlock(RLock lock) {
        try {
            if (lock.isLocked()) {
                lock.unlock();
            }
        } catch (IllegalMonitorStateException var3) {
        }
    }

}
