package com.zshnb.interviewpractice.redis;

import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class BitMap {
    private final String keyFormat = "sign:%d:%s";
    private final StringRedisTemplate redisTemplate;

    public BitMap(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void sign(int userId, LocalDate date) {
        String key = String.format(keyFormat, userId, DateTimeFormatter.ofPattern("yyyyMM", Locale.CHINA).format(date));
        int dayOfMonth = date.getDayOfMonth();
        redisTemplate.opsForValue().setBit(key, dayOfMonth, true);
    }

    /**
     * 统计指定月份用户签到天数
     * */
    public int count(int userId, LocalDate date) {
        String key = String.format(keyFormat, userId, DateTimeFormatter.ofPattern("yyyyMM", Locale.CHINA).format(date));
        return redisTemplate.execute((RedisCallback<Integer>) connection -> Math.toIntExact(connection.bitCount(key.getBytes(StandardCharsets.UTF_8))));
    }

    /**
     * 统计当月指定用户最长连续签到天数
     * */
    public int countContinuousInMonth(int userId, int month) {
        String date = DateTimeFormatter.ofPattern("yyyyMM", Locale.CHINA).format(LocalDate.of(LocalDate.now().getYear(), month, 1));
        String key = String.format(keyFormat, userId, date);
        byte[] bytes = redisTemplate.opsForValue().get(key).getBytes(StandardCharsets.UTF_8);
        StringBuilder bits = new StringBuilder();
        for (byte aByte : bytes) {
            bits.append(byteToBit(aByte));
        }
        return countLongestContinuousSequence(bits.toString());
    }

    private int countLongestContinuousSequence(String bits) {
        int max = 0;
        int count = 0;
        int index = 0;
        while (index < bits.length()) {
            if (bits.charAt(index) == '1') {
                count++;
            } else {
                if (count > max) {
                    max = count;
                }
                count = 0;
            }
            index++;
        }
        return max;
    }

    private String byteToBit(byte b) {
        return "" + (byte) ((b >> 7) & 0x1) +
            (byte) ((b >> 6) & 0x1) +
            (byte) ((b >> 5) & 0x1) +
            (byte) ((b >> 4) & 0x1) +
            (byte) ((b >> 3) & 0x1) +
            (byte) ((b >> 2) & 0x1) +
            (byte) ((b >> 1) & 0x1) +
            (byte) ((b) & 0x1);
    }
}
