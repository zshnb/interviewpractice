package com.zshnb.interviewpractice.juc;

import static org.assertj.core.api.Assertions.assertThat;

import com.zshnb.interviewpractice.BaseTest;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

public class ExclusiveLockTest extends BaseTest {
    private ExclusiveLock exclusiveLock = new ExclusiveLock();
    private CountDownLatch countDownLatch = new CountDownLatch(10);
    private int count = 5;

    @Test
    public void lock() throws InterruptedException {
        IntStream.range(0, 100).forEach(it -> {
            new Thread(() -> {
                try {
                    exclusiveLock.lock();
                    if (count == 0) {
                        return;
                    }
                    count--;
                } finally {
                    exclusiveLock.unlock();
                    countDownLatch.countDown();
                }
            }).start();
        });
        countDownLatch.await();
        assertThat(count).isZero();
    }
}
