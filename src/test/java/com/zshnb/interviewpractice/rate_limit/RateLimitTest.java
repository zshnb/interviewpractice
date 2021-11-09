package com.zshnb.interviewpractice.rate_limit;

import com.zshnb.interviewpractice.BaseTest;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

public class RateLimitTest extends BaseTest {
    @Test
    public void fixedTimeRateLimit() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        RateLimit rateLimit = new FixedTimeRateLimit(5);
        IntStream.range(0, 10).forEach(it -> {
            new Thread(() -> {
                LocalDateTime now = LocalDateTime.now();
                boolean flag = rateLimit.acquire();
                if (flag) {
                    System.out.printf("thread: %s pass at %s\n", Thread.currentThread().getName(), now);
                } else {
                    System.out.printf("thread: %s reject %s\n", Thread.currentThread().getName(), now);
                }
                countDownLatch.countDown();
            }).start();
        });
        countDownLatch.await();
    }

    @Test
    public void BucketRateLimit() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        RateLimit rateLimit = new BucketRateLimit(5, 1);
        IntStream.range(0, 10).forEach(it -> {
            new Thread(() -> {
                LocalDateTime now = LocalDateTime.now();
                boolean flag = rateLimit.acquire();
                if (flag) {
                    System.out.printf("thread: %s pass at %s\n", Thread.currentThread().getName(), now);
                } else {
                    System.out.printf("thread: %s reject %s\n", Thread.currentThread().getName(), now);
                }
                countDownLatch.countDown();
            }).start();
        });
        countDownLatch.await();
    }
}
