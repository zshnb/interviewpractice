package com.zshnb.interviewpractice.juc;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

public class CountTask extends RecursiveTask<Integer> {

    @Override
    protected Integer compute() {
        return 5;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        Future<Integer> future = forkJoinPool.submit(new CountTask());
        System.out.println(future.get());
    }
}
