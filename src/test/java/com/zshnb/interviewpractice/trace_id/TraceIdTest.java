package com.zshnb.interviewpractice.trace_id;

import com.zshnb.interviewpractice.BaseTest;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

public class TraceIdTest extends BaseTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testTraceId() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(5);
        IntStream.range(0, 5).forEach(it -> {
            new Thread(() -> {
                restTemplate.getForEntity("/api/user/info", Void.class);
                countDownLatch.countDown();
            }).start();
        });
        countDownLatch.await();
    }
}
