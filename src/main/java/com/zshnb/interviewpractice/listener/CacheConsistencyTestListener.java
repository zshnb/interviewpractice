package com.zshnb.interviewpractice.listener;

import com.zshnb.interviewpractice.cache_consistency.DelayedDeleteCacheStrategy;
import com.zshnb.interviewpractice.cache_consistency.EntityRepository;
import com.zshnb.interviewpractice.util.RandomUtil;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@Component
public class CacheConsistencyTestListener implements ApplicationListener<ApplicationReadyEvent> {
    private final DelayedDeleteCacheStrategy delayedDeleteCacheStrategy;
    private final EntityRepository entityRepository;
    private final RedisTemplate<String, Integer> redisTemplate;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final RandomUtil randomUtil;

    public CacheConsistencyTestListener(DelayedDeleteCacheStrategy delayedDeleteCacheStrategy, EntityRepository entityRepository, RedisTemplate<String, Integer> redisTemplate, RandomUtil randomUtil) {
        this.delayedDeleteCacheStrategy = delayedDeleteCacheStrategy;
        this.entityRepository = entityRepository;
        this.redisTemplate = redisTemplate;
        this.randomUtil = randomUtil;
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            connection.flushDb();
            return null;
        });
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // write threads;
        IntStream.range(0, 5).forEach(it -> {
            executorService.execute(() -> {
                while (true) {
                    int millis = randomUtil.getNumber(0, 2000);
                    try {
                        Thread.sleep(millis);
                        delayedDeleteCacheStrategy.write();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        });

        // read threads;
        IntStream.range(0, 5).forEach(it -> {
            executorService.execute(() -> {
                while (true) {
                    int millis = randomUtil.getNumber(0, 2000);
                    try {
                        Thread.sleep(millis);
                        int count = delayedDeleteCacheStrategy.read();
                        int countInDB = (int) entityRepository.count();
                        if (count != countInDB) {
                            System.out.printf("%s: inconsistent count\n", Thread.currentThread().getName());
                        }
                        System.out.printf("%s, cache count: %d, DB count: %d\n", Thread.currentThread().getName(), count, countInDB);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        });
    }
}
