package com.example.test.io;

import java.io.IOException;

public class Receiver extends Thread {

	private MyPipedInputStream inputStream = new MyPipedInputStream();
	
	public MyPipedInputStream getInputStream() {
		return this.inputStream;
	}
	
	@Override
	public void run() {
		readMessageOnce();
	}
	
	public void readMessageOnce() {
		byte[] buf = new byte[2048];
		try {
			int len = inputStream.read(buf);
			System.out.println(new String(buf, 0, len));
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
