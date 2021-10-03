package com.zshnb.interviewpractice.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class DistributeLock {
    private final RedisTemplate<String, Long> redisTemplate;
    private final String key = "lock";

    public DistributeLock(RedisTemplate<String, Long> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean lock(long value) {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(key, value, 10L, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(result);
    }

    public void release(long value) {
        Long mayExist = redisTemplate.opsForValue().get(key);
        if (mayExist != null && mayExist != value) {
            System.out.printf("%s rollback\n", Thread.currentThread().getName());
            return;
        }
        redisTemplate.delete(key);
    }
}
