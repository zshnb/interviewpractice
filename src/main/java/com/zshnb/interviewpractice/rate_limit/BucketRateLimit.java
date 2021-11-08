package com.zshnb.interviewpractice.rate_limit;

public class BucketRateLimit implements RateLimit {
    private int capacity;
    private long lastFlowTime;
    private int remain;
    private int rate;

    public BucketRateLimit(int capacity, int rate) {
        this.capacity = capacity;
        this.rate = rate;
        remain = 0;
        lastFlowTime = System.currentTimeMillis();
    }

    @Override
    public boolean acquire() {
        int outCount = (int) ((System.currentTimeMillis() - lastFlowTime) / 1000L * rate);
        remain = capacity - outCount;
        if (remain + 1 <= capacity) {
            lastFlowTime = System.currentTimeMillis();
            remain++;
            return true;
        }
        return false;
    }
}
