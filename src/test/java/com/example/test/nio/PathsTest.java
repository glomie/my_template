package com.example.test.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathsTest {

	public static void main(String[] args) throws IOException {
		Path path = Paths.get("C:/offline_FtnInfo.txt");
		FileChannel fileChannel = path.getFileSystem().provider().newFileChannel(path, null);
		ByteBuffer buf = ByteBuffer.allocate(48);
		int bytesRead = fileChannel.read(buf);
		while(bytesRead != -1) {
			buf.flip();
			while(buf.hasRemaining()) {
				System.out.print((char) buf.get());
			}
			buf.clear();
			bytesRead = fileChannel.read(buf);
		}
		fileChannel.close();
	}
}
