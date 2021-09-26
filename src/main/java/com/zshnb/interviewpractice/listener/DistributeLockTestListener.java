package com.zshnb.interviewpractice.listener;

import com.zshnb.interviewpractice.redis.DistributeLock;
import org.springframework.beans.factory.annotation.Autowired;
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

@Component
public class DistributeLockTestListener implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    private DistributeLock distributeLock;

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    private ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    private static int count = 10;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Random random = new Random();
        int max = 5000;
        int min = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        IntStream.range(0, 8).forEach(it -> {
            String name = String.format("thread-%d", it);
            executorService.execute(() -> {
                while (true) {
                    threadLocal.set(System.currentTimeMillis());
                    if (distributeLock.lock("key", threadLocal.get())) {
                        int millis = random.nextInt(max) % (max - min + 1) + min;
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
                        distributeLock.release("key", threadLocal.get());
                    }
                    threadLocal.remove();
                }
            });
        });
    }

    @Scheduled(fixedRate = 500)
    public void prolong() {
        Long expire = redisTemplate.getExpire("key", TimeUnit.SECONDS);
        if (expire != null && expire <= 1) {
            redisTemplate.expire("key", expire + 2L, TimeUnit.SECONDS);
            System.out.printf("prolong expire, now expire is %d\n", redisTemplate.getExpire("key", TimeUnit.SECONDS));
        }
    }
}
