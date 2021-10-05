package com.zshnb.interviewpractice.cache_consistency;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DisableInDatabaseStrategy implements CacheConsistencyStrategy {
    private final CacheInfoRepository cacheInfoRepository;
    private final EntityRepository entityRepository;
    private final RedisTemplate<String, Integer> redisTemplate;

    public DisableInDatabaseStrategy(CacheInfoRepository cacheInfoRepository, EntityRepository entityRepository, RedisTemplate<String, Integer> redisTemplate) {
        this.cacheInfoRepository = cacheInfoRepository;
        this.entityRepository = entityRepository;
        this.redisTemplate = redisTemplate;
        CacheInfo cacheInfo = new CacheInfo();
        cacheInfo.setName(CacheConsistencyStrategy.key);
        cacheInfo.setValid(false);
        cacheInfoRepository.saveAndFlush(cacheInfo);
    }

    @Override
    @Transactional
    public void write() {
        entityRepository.saveAndFlush(new Entity(Thread.currentThread().getName()));
        CacheInfo cacheInfo = cacheInfoRepository.findByName(CacheConsistencyStrategy.key);
        cacheInfo.setValid(false);
        cacheInfoRepository.saveAndFlush(cacheInfo);
    }

    @Override
    public int read() {
        ValueOperations<String, Integer> valueOperations = redisTemplate.opsForValue();
        CacheInfo cacheInfo = cacheInfoRepository.findByName(CacheConsistencyStrategy.key);
        if (!cacheInfo.getValid()) {
            int count = (int) entityRepository.count();
            valueOperations.set(CacheConsistencyStrategy.key, count);
            return count;
        }
        return valueOperations.get(CacheConsistencyStrategy.key);
    }
}
