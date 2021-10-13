package com.zshnb.interviewpractice.multi_thread;

import java.util.ArrayList;
import java.util.List;

public class WaitNotifyDemo {
    private Object object = new Object();
    private List<Object> objects = new ArrayList<>();

    public static void main(String[] args) {
        new WaitNotifyDemo().waitAndNotify();
    }

    public void waitAndNotify() {
        new Thread(() -> {
            while (true) {
                synchronized (object) {
                    while (objects.isEmpty()) {
                        try {
                            object.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    objects.remove(0);
                    System.out.printf("in consumer objects' size: %d\n", objects.size());
                }
            }
        }).start();
        new Thread(() -> {
            while (true) {
                synchronized (object) {
                    if (objects.isEmpty()) {
                        objects.add(new Object());
                        object.notify();
                        System.out.printf("in producer objects' size: %d\n", objects.size());
                    }
                }
            }
        }).start();
    }
}
