package com.zshnb.interviewpractice.redis;

import com.zshnb.interviewpractice.util.BitUtil;
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
    private final BitUtil bitUtil;

    public BitMap(StringRedisTemplate redisTemplate, BitUtil bitUtil) {
        this.redisTemplate = redisTemplate;
        this.bitUtil = bitUtil;
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
            bits.append(bitUtil.byteToBit(aByte));
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
}
