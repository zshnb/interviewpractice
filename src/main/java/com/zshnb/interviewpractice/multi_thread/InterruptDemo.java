package com.zshnb.interviewpractice.multi_thread;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 通过interrupted()标记线程终止位，线程在执行过程中需要不断检测终止位的状态，做到安全停止
 * */
public class InterruptDemo {
    public static void main(String[] args) throws InterruptedException {
        new InterruptDemo().safeTerminateThread();
    }

    public void safeTerminateThread() throws InterruptedException {
        AtomicInteger count = new AtomicInteger();
        Thread thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                count.getAndIncrement();
            }
        });
        thread.start();
        Thread.sleep(2000L);
        thread.interrupt();
        System.out.printf("count: %d\n", count.get());
    }

    public void testInterrupt() throws InterruptedException {
        Thread busy = new Thread(() -> {
            while (true) {

            }
        }, "busy thread");

        Thread sleep = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "sleep thread");

        busy.start();
        sleep.start();
        Thread.sleep(2000L);
        busy.interrupt();
        sleep.interrupt();
        System.out.printf("busy's interrupted: %b\n", busy.isInterrupted());
        System.out.printf("sleep's interrupted: %b\n", sleep.isInterrupted());
    }
}
