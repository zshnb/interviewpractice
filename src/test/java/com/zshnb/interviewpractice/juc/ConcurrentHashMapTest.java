package com.zshnb.interviewpractice.juc;

import com.zshnb.interviewpractice.BaseTest;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

public class ConcurrentHashMapTest extends BaseTest {
    private ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
    private CountDownLatch countDownLatch = new CountDownLatch(10);

    @Test
    public void test() throws InterruptedException {
        IntStream.range(0, 100).forEach(it -> {
            new Thread(() -> {
                try {
                    map.put("key", Thread.currentThread().getName());
                    String value = map.get("key");
                    if (!Thread.currentThread().getName().equals(value)) {
                        System.out.printf("current: %s, key: %s\n", Thread.currentThread().getName(), value);
                    }
                } finally {
                    countDownLatch.countDown();
                }
            }).start();
        });
        countDownLatch.await();
    }
}
