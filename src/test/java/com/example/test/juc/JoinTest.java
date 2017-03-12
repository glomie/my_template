package com.example.test.juc;

public class JoinTest {

	public static void main(String[] args) {
		Thread threadB = new ThreadB();
		threadB.setDaemon(true);
		threadB.start();
		try {
			threadB.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + " finish");
		System.out.println(threadB.isDaemon());
	}
}

class ThreadB extends Thread {
	
	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName() + " start");
		try {
			System.out.println("sleeping 1000ms");
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + " finish");
	}
}
