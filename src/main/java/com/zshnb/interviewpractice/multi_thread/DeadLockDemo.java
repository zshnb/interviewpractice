package com.zshnb.interviewpractice.multi_thread;

/**
 * 死锁示例，通过jstack查看到以下信息
 * Found one Java-level deadlock:
 * =============================
 * "Thread-0":
 *   waiting to lock monitor 0x000002077f474700 (object 0x0000000620c2c808, a java.lang.String),
 *   which is held by "Thread-1"
 * "Thread-1":
 *   waiting to lock monitor 0x000002077f476700 (object 0x0000000620c2c7d8, a java.lang.String),
 *   which is held by "Thread-0"
 * 通常死锁出现在线程持有多个锁的情况，多个线程共同竞争锁，导致锁的获取顺序出现了环形
 * */
public class DeadLockDemo {
    private final String a = "a";
    private final String b = "b";
    public static void main(String[] args) {
        new DeadLockDemo().deadLock();
    }

    public void deadLock() {
        new Thread(() -> {
            synchronized (a) {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (b) {
                    System.out.println("lock with a then b");
                }
            }
        }).start();

        new Thread(() -> {
            synchronized (b) {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (a) {
                    System.out.println("lock with b then a");
                }
            }
        }).start();
    }
}
