package com.example.test.lang;

import java.util.HashMap;

public class ThreadLocalTest {

	@SuppressWarnings("rawtypes")
	private static ThreadLocal<HashMap> map0 = new ThreadLocal<HashMap>() {
		
		@Override
		protected HashMap initialValue() {
			System.out.println(Thread.currentThread().getName() + "initialValue");
			return new HashMap();
		};
	};
	
	public void run() {
		Thread[] threads = new Thread[3];
		for(int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new T1(i));
		}
		
		for(int i = 0; i < threads.length; i++) {
			threads[i].start();
		}
	}
	
	public static void main(String[] args) {
		(new ThreadLocalTest()).run();
	}
	
	static class T1 implements Runnable {

		private int id;
		
		public T1(int id) {
			this.id = id;
		} 
		
		@Override
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public void run() {
			System.out.println(Thread.currentThread().getName() + ":start");
			
			HashMap map = map0.get();
			for(int i = 0; i < 10; i++) {
				map.put(i, i + id * 100);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			System.out.println(Thread.currentThread().getName() + ":" + map);
		}
		
	}
	
	
}
