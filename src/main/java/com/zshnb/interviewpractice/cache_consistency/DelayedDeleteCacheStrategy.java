package com.zshnb.interviewpractice.cache_consistency;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class DelayedDeleteCacheStrategy implements CacheConsistencyStrategy {
    private final EntityRepository entityRepository;
    private final RedisTemplate<String, Integer> redisTemplate;

    public DelayedDeleteCacheStrategy(EntityRepository entityRepository, RedisTemplate<String, Integer> redisTemplate) {
        this.entityRepository = entityRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void write() throws InterruptedException {
        entityRepository.saveAndFlush(new Entity(Thread.currentThread().getName()));
        redisTemplate.delete(key);
        Thread.sleep(200);
        redisTemplate.delete(key);
    }

    @Override
    public int read() {
        ValueOperations<String, Integer> valueOperations = redisTemplate.opsForValue();
        Integer countInCache = valueOperations.get(key);
        if (countInCache == null) {
            int countInDB = (int) entityRepository.count();
            valueOperations.set(key, countInDB);
            return countInDB;
        }
        return countInCache;
    }
}
