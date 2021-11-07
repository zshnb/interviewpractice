package com.zshnb.interviewpractice.multi_thread;

public class RoundRobinPrint {
    static Object lock = new Object();

    public static void main(String[] args) {
        new Thread(new VowelThread()).start();
        new Thread(new ConsonantThread()).start();
    }

    static class VowelThread implements Runnable {
        private int index = 0;
        private String[] words = new String[] {"a", "e", "i", "o", "u"};

        @Override
        public void run() {
            while (true) {
                synchronized (lock) {
                    System.out.printf("thread: %s, word: %s\n", Thread.currentThread().getName(), words[index % words.length]);
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    index += 1;
                    lock.notify();
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    static class ConsonantThread implements Runnable {
        private int index = 0;
        private String[] words = new String[] {"b", "c", "d", "f", "g"};

        @Override
        public void run() {
            while (true) {
                synchronized (lock) {
                    System.out.printf("thread: %s, word: %s\n", Thread.currentThread().getName(), words[index % words.length]);
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    index += 1;
                    lock.notify();
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
