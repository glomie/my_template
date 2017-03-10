package com.example.test.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class BufferOutputStreamTest {

	public static void main(String[] args) throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File("E:/out.txt"), true));
		bos.write("我的世界".getBytes());
		bos.close();
		
		PrintStream ps = new PrintStream(new File("E:/out.txt"));
		ps.println("hello world");
		ps.close();
	}
}
