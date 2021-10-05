package com.zshnb.interviewpractice.cache_consistency;

import com.zshnb.interviewpractice.redis.DistributeLock;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class WithLockStrategy implements CacheConsistencyStrategy {
    private final DistributeLock lock;
    private final EntityRepository entityRepository;
    private final RedisTemplate<String, Integer> redisTemplate;

    public WithLockStrategy(DistributeLock lock, EntityRepository entityRepository, RedisTemplate<String, Integer> redisTemplate) {
        this.lock = lock;
        this.entityRepository = entityRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void write() {
        long value = System.currentTimeMillis();
        try {
            lock.lock(value);
            entityRepository.saveAndFlush(new Entity(Thread.currentThread().getName()));
            redisTemplate.delete(key);
        } finally {
            lock.release(value);
        }
    }

    @Override
    public int read() {
        long value = System.currentTimeMillis();
        try {
            if (lock.lock(value)) {
                ValueOperations<String, Integer> valueOperations = redisTemplate.opsForValue();
                Integer countInCache = valueOperations.get(key);
                if (countInCache == null) {
                    int countInDB = (int) entityRepository.count();
                    valueOperations.set(key, countInDB);
                    return countInDB;
                }
                return countInCache;
            }
            return 0;
        } finally {
            lock.release(value);
        }
    }
}
