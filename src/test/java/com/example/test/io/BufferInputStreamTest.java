package com.example.test.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class BufferInputStreamTest {

	public static void main(String[] args) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File("E:/test.txt")));
		byte[] res = new byte[2048];
		int x = 0;
		while((x = bis.read()) != -1) {
			System.out.println((char)x);
		}
		bis.close();
		method();
	}
	
	public static final void method() {
		System.out.println("xxx");
	}
}


