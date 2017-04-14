package com.example.test.juc;

public class InterruptedTest {

	public static void main(String[] args) {
		Thread tc = new ThreadC();
		tc.start();
		//做耗时操作 
		for(int i = 0; i < 1_000_000; i++) 
			for(int j = 0; j < 1_000_000; j++);
		
		tc.interrupt();
	}
}

class ThreadC extends Thread {
	@Override
	public void run() {
		long start = System.currentTimeMillis();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			System.err.println("after " + (System.currentTimeMillis() - start) + "ms 线程被打断");
		}
		System.out.println("中断后继续运行");
	}
}