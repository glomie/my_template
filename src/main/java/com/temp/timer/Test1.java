package com.temp.timer;

/**
 * @author wujunyan
 * Created on 2025-10-31
 */
public class Test1 {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            Object o = new Object();
            synchronized (o) {
                try {
                    o.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.setName("xxxx");
        thread.start();

    }
}
