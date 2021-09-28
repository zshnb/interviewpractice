package com.zshnb.interviewpractice.redis;

import com.zshnb.interviewpractice.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class BloomFilterTest extends BaseTest {
    @Autowired
    private BloomFilter bloomFilter;
    @BeforeEach
    public void beforeSetup() {
        bloomFilter.init(3);
        bloomFilter.add("hello world");
        bloomFilter.add("make file");
        bloomFilter.add("heskell");
        bloomFilter.add("java");
        bloomFilter.add("eat lunch");
    }

    @Test
    public void addAndCheck() {
        assertThat(bloomFilter.contain("hello world")).isTrue();
        assertThat(bloomFilter.contain("hello2 world")).isFalse();
    }
}
