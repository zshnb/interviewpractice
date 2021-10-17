package com.zshnb.interviewpractice.multi_thread;

import com.zshnb.interviewpractice.util.RandomUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ThreadPool {
    private List<Runnable> runnables;
    private List<Worker> workers;

    public ThreadPool(int size) {
        runnables = new ArrayList<>();
        workers = new ArrayList<>(size);
        IntStream.range(0, size).forEach(it -> {
            Worker worker = new Worker();
            workers.add(worker);
            Thread thread = new Thread(worker, String.format("worker-%d", it));
            thread.start();
        });
    }

    public void execute(Runnable runnable) {
        synchronized (runnables) {
            runnables.add(runnable);
            runnables.notify();
        }
    }

    class Worker implements Runnable {
        @Override
        public void run() {
            while (true) {
                synchronized (runnables) {
                    while (runnables.isEmpty()) {
                        try {
                            runnables.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                System.out.printf("thread: %s running\n", Thread.currentThread().getName());
                runnables.remove(0).run();
            }
        }
    }

    public static void main(String[] args) {
        ThreadPool threadPool = new ThreadPool(2);
        IntStream.range(0, 10).forEach(it -> {
            threadPool.execute(() -> {
                try {
                    System.out.printf("job-%d execute\n", it);
                    Thread.sleep(new RandomUtil().getNumber(0, 5000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });
    }
}
