package com.example.test.juc;

public class WaitAndNotifyTest {

	public static void main(String[] args) {
		Thread t = new Thread() {
			@Override
			public void run() {
				synchronized (this) {
					System.out.println(Thread.currentThread().getName() + " call notify after 1s");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
					this.notify();
					System.out.println("主线程运行完了再运行");
				}
			}
		};
		
		synchronized (t) {
			try {
				System.out.println(Thread.currentThread().getName() + " start t");
				t.start();
				
				System.out.println(Thread.currentThread().getName() + " wait()");
				t.wait();
				
				System.out.println(Thread.currentThread().getName() + " continue");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
