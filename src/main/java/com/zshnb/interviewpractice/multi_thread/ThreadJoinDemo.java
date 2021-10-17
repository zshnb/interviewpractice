package com.zshnb.interviewpractice.multi_thread;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadJoinDemo {
    public static void main(String[] args) throws InterruptedException {
        AtomicInteger integer = new AtomicInteger(0);
        Thread thread = new Thread(() -> {
            while (integer.getAndIncrement() < 10) {
                System.out.printf("integer: %d\n", integer.get());
                try {
                    Thread.sleep(200L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        thread.join();
        System.out.println("previous thread end, now is main thread");
    }
}
