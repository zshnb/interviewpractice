package com.zshnb.interviewpractice.multi_thread;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.stream.IntStream;

public class ConnectionPool {
    private LinkedList<Object> connections;
    private Object lock = new Object();

    public ConnectionPool(int n) {
        connections = new LinkedList<>();
        IntStream.range(0, n).forEach(it -> {
            connections.add(new Object());
        });
    }

    public Object getConnection(long millis) throws InterruptedException {
        synchronized (this) {
            long future = System.currentTimeMillis() + millis;
            long remain = millis;
            while (connections.isEmpty() && remain > 0) {
                wait(remain);
                remain = future - System.currentTimeMillis();
                System.out.printf("time: %s, thread: %s, remain: %d, empty: %b\n", LocalDateTime.now().toLocalTime(), Thread.currentThread().getName(), remain, connections.isEmpty());
            }
            return connections.poll();
        }
    }

    public void releaseConnection(Object connection) {
        synchronized (this) {
            connections.addLast(connection);
            notifyAll();
        }
    }

    public static void main(String[] args) {
        ConnectionPool connectionPool = new ConnectionPool(1);
        IntStream.range(0, 2).forEach(it -> {
            new Thread(() -> {
                try {
                    Object connection = connectionPool.getConnection(2000L);
                    System.out.printf("time: %s, thread: %s, connection is null: %b\n", LocalDateTime.now().toLocalTime(), Thread.currentThread().getName(), connection == null);
                    Thread.sleep(4000L);
                    connectionPool.releaseConnection(connection);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }
}
