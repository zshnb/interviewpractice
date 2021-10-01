package com.zshnb.interviewpractice.listener;

import com.zshnb.interviewpractice.redis.DistributeLock;
import com.zshnb.interviewpractice.util.RandomUtil;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

//@Component
public class DistributeLockTestListener implements ApplicationListener<ApplicationReadyEvent> {
    private final DistributeLock distributeLock;
    private final RedisTemplate<String, Long> redisTemplate;
    private final ThreadLocal<Long> threadLocal = new ThreadLocal<>();
    private final RandomUtil randomUtil;
    private static int count = 10;

    public DistributeLockTestListener(DistributeLock distributeLock,
                                      RedisTemplate<String, Long> redisTemplate,
                                      RandomUtil randomUtil) {
        this.distributeLock = distributeLock;
        this.redisTemplate = redisTemplate;
        this.randomUtil = randomUtil;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        IntStream.range(0, 8).forEach(it -> {
            String name = String.format("thread-%d", it);
            executorService.execute(() -> {
                while (true) {
                    threadLocal.set(System.currentTimeMillis());
                    if (distributeLock.lock(threadLocal.get())) {
                        int millis = randomUtil.getNumber(1000, 5000);
                        System.out.printf("%s get lock, execute time: %d\n", name, millis);
                        try {
                            Thread.sleep(millis);
                            if (count > 0) {
                                count --;
                            }
                            System.out.printf("count: %d\n", count);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        distributeLock.release(threadLocal.get());
                    }
                    threadLocal.remove();
                }
            });
        });
    }

//    @Scheduled(fixedRate = 500)
    public void prolong() {
        Long expire = redisTemplate.getExpire("key", TimeUnit.SECONDS);
        if (expire != null && expire <= 1) {
            redisTemplate.expire("key", expire + 2L, TimeUnit.SECONDS);
            System.out.printf("prolong expire, now expire is %d\n", redisTemplate.getExpire("key", TimeUnit.SECONDS));
        }
    }
}
