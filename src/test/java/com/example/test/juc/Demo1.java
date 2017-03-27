package com.example.test.juc;

class Depot {

	private int capacity;
	
	private int size;
	
	public Depot(int capacity) {
		this.capacity = capacity;
		this.size = 0;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public synchronized void produce(int val) {
		int left = val;
		try {
			while (left > 0) {
				while (size >= capacity) {
					wait();
				}
				int inc = (size + left) > capacity ? (capacity - size) : left;
				size += inc;
				left -= inc;
				System.out.printf("%s produce(%3d) --> left=%3d, inc=%3d, size=%3d\n", 
                        Thread.currentThread().getName(), val, left, inc, size);
				notifyAll();
			}
		} catch(InterruptedException e) {}
	}
	
	public synchronized void consume(int val) {
		int left = val;
		try {
			while (left > 0) {
				while (size <= 0) {
					wait();
				}
				int dec = (size < left) ? size :left;
				size -= dec;
				left -= dec;
				System.err.printf("%s consume(%3d) <-- left=%3d, dec=%3d, size=%3d\n", 
                        Thread.currentThread().getName(), val, left, dec, size);
				notifyAll();
			}
		} catch(InterruptedException e) {}
	}
}

class Producer {
	private Depot depot;
	
	public Producer(Depot depot) {
		this.depot = depot;
	}
	
	public void produce(final int val) {
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				depot.produce(val);
			}
		});
		t.start();
	}
}

class Consumer {
	private Depot depot;
	
	public Consumer(Depot depot) {
		this.depot = depot;
	}
	
	public void consume(final int val) {
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				depot.consume(val);
			}
		});
		t.start();
	}
}

public class Demo1 {
	
	public static void main(String[] args) {
		Depot depot = new Depot(100);
		Producer producer = new Producer(depot);
		Consumer consumer = new Consumer(depot);
		
		producer.produce(60);
		consumer.consume(120);
		consumer.consume(90);
		producer.produce(220);
		consumer.consume(70);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("size=" + depot.getSize());
	}
}
