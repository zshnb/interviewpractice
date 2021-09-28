package com.zshnb.interviewpractice.redis;

import com.zshnb.interviewpractice.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.text.BreakIterator;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Component
public class BloomFilter {
    /**
     * 位数组的大小
     */
    private final int DEFAULT_SIZE = 2 << 24;
    /**
     * 通过这个数组可以创建 6 个不同的哈希函数
     */
    private HashFunction[] hashFunctions;

    private RandomUtil randomUtil;
    private StringRedisTemplate stringRedisTemplate;

    public void init(int functionSize) {
        AtomicInteger min = new AtomicInteger(10);
        hashFunctions = new HashFunction[functionSize];
        IntStream.range(0, functionSize).forEach(it -> {
            int seed = randomUtil.getNumber(min.get(), min.get() * 2);
            min.set(seed);
            hashFunctions[it] = new HashFunction(DEFAULT_SIZE, seed);
        });
    }

    public void add(Object object) {
        Arrays.stream(hashFunctions).forEach(it -> {
            int hash = it.hash(object);
            stringRedisTemplate.opsForValue().setBit("bloom-filter", hash, true);
        });
    }

    public boolean contain(Object object) {
        for (HashFunction it : hashFunctions) {
            int hash = it.hash(object);
            if (!stringRedisTemplate.opsForValue().getBit("bloom-filter", hash)) {
                return false;
            }
        }
        return true;
    }

    @Autowired
    public BloomFilter(RandomUtil randomUtil, StringRedisTemplate stringRedisTemplate) {
        this.randomUtil = randomUtil;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public static class HashFunction {
        private final int cap;
        private final int seed;

        public HashFunction(int cap, int seed) {
            this.cap = cap;
            this.seed = seed;
        }
        public int hash(Object value) {
            int h;
            return (value == null) ? 0 : Math.abs(seed * (cap - 1) & ((h = value.hashCode()) ^ (h >>> 16)));
        }

    }
}
