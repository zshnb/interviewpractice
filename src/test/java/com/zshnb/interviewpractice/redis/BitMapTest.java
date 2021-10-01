package com.zshnb.interviewpractice.redis;

import com.zshnb.interviewpractice.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class BitMapTest extends BaseTest {
    @Autowired
    private BitMap bitMap;

    int userId = 1;
    int secondUserId = 2;

    @Test
    public void signAndCount() {
        IntStream.range(20, 26).forEach(it -> {
            bitMap.sign(userId, LocalDate.of(2021, 9, it));
        });
        IntStream.range(20, 26).forEach(it -> {
            bitMap.sign(secondUserId, LocalDate.of(2021, 8, it));
        });
        int userIdSignCount = bitMap.count(userId, LocalDate.of(2021, 9, 1));
        assertThat(userIdSignCount).isEqualTo(6);

        int secondUserIdSignCount = bitMap.count(secondUserId, LocalDate.of(2021, 9, 1));
        assertThat(secondUserIdSignCount).isZero();

        secondUserIdSignCount = bitMap.count(secondUserId, LocalDate.of(2021, 8, 1));
        assertThat(secondUserIdSignCount).isEqualTo(6);
    }

    @Test
    public void longestContinuousSignInMonthUsers() {
        mockSign();
        int result = bitMap.countContinuousInMonth(userId, 9);
        assertThat(result).isEqualTo(5);
    }

    private void mockSign() {
        IntStream.range(1, 4).forEach(it -> bitMap.sign(userId, LocalDate.of(2021, 9, it)));

        IntStream.range(10, 15).forEach(it -> bitMap.sign(userId, LocalDate.of(2021, 9, it)));
    }
}
