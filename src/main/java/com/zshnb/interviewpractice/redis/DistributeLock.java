package com.zshnb.interviewpractice.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Component
public class DistributeLock {
    private final RedisTemplate<String, Serializable> redisTemplate;

    public DistributeLock(RedisTemplate<String, Serializable> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean lock(String key, long value) {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(key, value, 2L, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(result);
    }

    public void release(String key, long value) {
        Long mayExist = (Long) redisTemplate.opsForValue().get(key);
        if (mayExist != null && mayExist != value) {
            System.out.printf("%s rollback\n", Thread.currentThread().getName());
            return;
        }
        redisTemplate.delete(key);
    }
}
