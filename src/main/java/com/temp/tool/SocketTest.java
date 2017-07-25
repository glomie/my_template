package com.temp.tool;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketTest {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		Socket sock = new Socket();
		sock.setSoTimeout(0);
		sock.setReuseAddress(false);
		sock.setTcpNoDelay(true);
		sock.setKeepAlive(false);
		sock.setSoLinger(false, -1);
		long time1 = System.currentTimeMillis();
		InetSocketAddress remoteAddress = new InetSocketAddress("www.google.com", 80);
		try {
			System.out.println("连接中...");
			sock.connect(remoteAddress, 0);
		} catch (IOException e) {
			System.err.println("超时，执行时间" + (System.currentTimeMillis() - time1) + "ms");
			sock.close();
			throw e;
		}
		System.err.println("成功，执行时间" + (System.currentTimeMillis() - time1) + "ms");
	}
}
 