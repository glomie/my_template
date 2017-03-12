package com.example.test.juc;

public class DaemonTest {

	public static void main(String[] args) {
		boolean isDaemon = true;
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(isDaemon ? "是" : "非" + "守护进程。。。");
			}
		});
		thread.setDaemon(isDaemon);
		thread.start();
		System.out.println("主程序结束");
	}
}
