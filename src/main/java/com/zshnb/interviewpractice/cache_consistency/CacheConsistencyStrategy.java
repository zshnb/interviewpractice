package com.zshnb.interviewpractice.cache_consistency;

public interface CacheConsistencyStrategy {
    void write() throws InterruptedException;
    int read();
}
