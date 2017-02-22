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
		// 虽然buf的大小是2048个字节，但最多只会从“管道输入流”中读取1024个字节。
		// 因为，“管道输入流”的缓冲区大小默认只有1024个字节。
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
