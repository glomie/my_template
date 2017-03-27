package com.example.test.juc;

public class TestNum {

	private static ThreadLocal<Integer> seqNum = new ThreadLocal<Integer>() {
		protected Integer initialValue() {
			return 0;
		};
	};
	
	public int getNextNum() {
		seqNum.set(seqNum.get() + 1);
		return seqNum.get();
	}

	public static void main(String[] args) {
		TestNum sn = new TestNum();
		TestClient t1 = new TestClient(sn);
		TestClient t2 = new TestClient(sn);
		TestClient t3 = new TestClient(sn);
		
		t1.start();
		t2.start();
		t3.start();
	}
	
}

class TestClient extends Thread {
	private TestNum sn;
	
	public TestClient(TestNum sn) {
		this.sn = sn;
	}
	
	@Override
	public void run() {
		for (int i = 0; i < 3; i++) {  
            System.out.println("thread[" + Thread.currentThread().getName() + "] --> sn["  
                     + sn.getNextNum() + "]");  
        }  
	}
}
