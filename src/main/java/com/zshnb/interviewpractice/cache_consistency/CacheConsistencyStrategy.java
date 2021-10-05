package com.zshnb.interviewpractice.cache_consistency;

public interface CacheConsistencyStrategy {
    String key = "entity-cache";
    void write() throws InterruptedException;
    int read();
}
