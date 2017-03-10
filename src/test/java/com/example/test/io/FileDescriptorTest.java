package com.example.test.io;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class FileDescriptorTest {

	public static void main(String[] args) {
		FileOutputStream fileOutputStream = new FileOutputStream(FileDescriptor.out);
		PrintStream printStream = new PrintStream(fileOutputStream);
		printStream.println("hello world");
		printStream.close();
	}
}
