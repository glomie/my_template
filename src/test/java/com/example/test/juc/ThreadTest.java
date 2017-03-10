package com.example.test.juc;

public class ThreadTest {

	public static void main(String[] args) {
		Thread thread = new Thread(() -> {
			System.out.println(Thread.currentThread().getName() + "运行run方法");
			for(int i = 0; i < 10000000; i++) {}
		});
		thread.setPriority(10);
		synchronized (thread) {
			try {
				long start = System.currentTimeMillis();
				thread.wait(100);
				System.out.println("等待了" + (System.currentTimeMillis() - start) + "ms");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		thread.start();
		System.out.println("方法先走下去");
		
		Runnable r = new RunImpl();
		Thread t1 = new Thread(r);
		Thread t2 = new Thread(r);
		t1.start();
		t2.start();
	}
}

class RunImpl implements Runnable {

	private int limit = 0;
	
	@Override
	public void run() {
		for(int i = 0; i < 10; i++) {
			System.out.println(Thread.currentThread().getName() + " is running " + i);
			if(limit < 5) {
				System.err.println(Thread.currentThread().getName() + " is running limit of " + limit++);
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}