package com.example.test.io;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class PrintStreamTest {

	public static void main(String[] args) {
		FileOutputStream fos = new FileOutputStream(FileDescriptor.out);
		PrintStream ps = new PrintStream(fos);
		ps.println("hello world");
		ps.close();
	}
}

