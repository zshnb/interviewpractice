package com.zshnb.interviewpractice.rate_limit;

public class FixedTimeRateLimit implements RateLimit {
    private int limit;
    private volatile int count;
    private long upperTime;

    public FixedTimeRateLimit(int limit) {
        this.limit = limit;
        count = 0;
        upperTime = System.currentTimeMillis() + 1000L;
    }

    @Override
    public boolean acquire() {
        if (System.currentTimeMillis() > upperTime) {
            count = 0;
            upperTime = System.currentTimeMillis() + 1000L;
        }
        if (count >= limit) {
            return false;
        }
        count += 1;
        return true;
    }
}
