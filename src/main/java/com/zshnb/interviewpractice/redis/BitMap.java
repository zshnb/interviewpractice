package com.zshnb.interviewpractice.redis;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class BitMap {
    private final RedisTemplate<String, Serializable> redisTemplate;

    public BitMap(RedisTemplate<String, Serializable> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void sign(int userId, LocalDate date) {
        String key = String.format("sing:%d:%s", userId, DateTimeFormatter.ofPattern("yyyyMM", Locale.CHINA).format(date));
        int dayOfMonth = date.getDayOfMonth();
        redisTemplate.opsForValue().setBit(key, dayOfMonth, true);
    }

    public int count(int userId, LocalDate date) {
        String key = String.format("sing:%d:%s", userId, DateTimeFormatter.ofPattern("yyyyMM", Locale.CHINA).format(date));
        return redisTemplate.execute((RedisCallback<Integer>) connection -> Math.toIntExact(connection.bitCount(key.getBytes(StandardCharsets.UTF_8))));
    }
}
