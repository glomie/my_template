package com.example.test.juc;

public class SynchronizedTest {

	public static void main(String[] args) {
		Runnable r = new MyRunnable();
		Thread t1 = new Thread(r);
		Thread t2 = new Thread(r);
		t1.start();
		t2.start();
	}
}

class MyRunnable implements Runnable {

	@Override
	public void run() {
		Test.foo1(Thread.currentThread().getName());
	}
	
}

class Test {

	private static final Test instance = new Test();
	
	public static Test getInstance() {
		return instance;
	}
	
	public static synchronized void foo1(String threadName) {
		for(int i = 0; i < 5; i ++) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(threadName + " is looping " + i);
		}
	}
}
