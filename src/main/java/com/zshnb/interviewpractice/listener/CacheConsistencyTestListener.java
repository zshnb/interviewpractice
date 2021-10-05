package com.zshnb.interviewpractice.listener;

import com.zshnb.interviewpractice.cache_consistency.*;
import com.zshnb.interviewpractice.util.RandomUtil;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import java.util.stream.IntStream;

//@Component
public class CacheConsistencyTestListener implements ApplicationListener<ApplicationReadyEvent> {
    private final DelayedDeleteCacheStrategy delayedDeleteCacheStrategy;
    private final WithLockStrategy withLockStrategy;
    private final DisableInDatabaseStrategy disableInDatabaseStrategy;
    private final BinlogStrategy binlogStrategy;
    private final EntityRepository entityRepository;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final RandomUtil randomUtil;

    public CacheConsistencyTestListener(DelayedDeleteCacheStrategy delayedDeleteCacheStrategy, EntityRepository entityRepository, RedisTemplate<String, Integer> redisTemplate, WithLockStrategy withLockStrategy, DisableInDatabaseStrategy disableInDatabaseStrategy, BinlogStrategy binlogStrategy, RandomUtil randomUtil) {
        this.delayedDeleteCacheStrategy = delayedDeleteCacheStrategy;
        this.entityRepository = entityRepository;
        this.withLockStrategy = withLockStrategy;
        this.disableInDatabaseStrategy = disableInDatabaseStrategy;
        this.binlogStrategy = binlogStrategy;
        this.randomUtil = randomUtil;
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            connection.flushDb();
            return null;
        });
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // write threads;
        IntStream.range(0, 2).forEach(it -> {
            executorService.execute(() -> writeProcess(binlogStrategy::write));
        });
        // read threads;
        IntStream.range(0, 7).forEach(it -> {
            executorService.execute(() -> readProcess(binlogStrategy::read));
        });
    }

    private void writeProcess(Runner runner) {
        while (true) {
            int millis = randomUtil.getNumber(0, 2000);
            try {
                Thread.sleep(millis);
                runner.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void readProcess(Supplier<Integer> supplier) {
        while (true) {
            int millis = randomUtil.getNumber(0, 2000);
            try {
                Thread.sleep(millis);
                int count = supplier.get();
                if (count == 0) {
                    return;
                }
                int countInDB = (int) entityRepository.count();
                if (count != countInDB) {
                    System.out.printf("%s: inconsistent count\n", Thread.currentThread().getName());
                }
                System.out.printf("%s cache count: %d, DB count: %d\n", Thread.currentThread().getName(), count, countInDB);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
