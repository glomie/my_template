package com.example.test.io;

import java.io.IOException;

public class PipedStreamTest {

	public static void main(String[] args) {
		Sender t1 = new Sender();
		t1.setName("Sender");
		Receiver t2 = new Receiver();
		t2.setName("Receiver");
		MyPipedInputStream inputStream = t2.getInputStream();
		MyPipedOutputStream outputStream = t1.getOutputStream();
		
		try {
			inputStream.connect(outputStream);
			t2.start();
			t1.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
