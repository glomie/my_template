package com.example.test.io;

import java.io.IOException;

public class PipedStreamTest {

	public static void main(String[] args) {
		Sender t1 = new Sender();
		Receiver t2 = new Receiver();
		MyPipedInputStream inputStream = t2.getInputStream();
		MyPipedOutputStream outputStream = t1.getOutputStream();
		
		try {
			inputStream.connect(outputStream);
			t1.start();
			t2.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
