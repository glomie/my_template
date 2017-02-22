package com.example.test.io;

import java.io.IOException;
import java.io.OutputStream;

public class MyPipedOutputStream extends OutputStream {
	
	private MyPipedInputStream inputStream;
	
	public MyPipedOutputStream() {}
	
	public MyPipedOutputStream(MyPipedInputStream inputStream) throws IOException {
		this.connect(inputStream);
	}
	
	public synchronized void connect(MyPipedInputStream inputStream) throws IOException {
		if (inputStream == null) {
			throw new NullPointerException();
		} else if (this.inputStream != null || inputStream.connected) {
			throw new IOException("Already connected.");
		}
		
		inputStream.in = -1;
		inputStream.out = 0;
		inputStream.connected = true;
		this.inputStream = inputStream;
	}

	@Override
	public void write(int b) throws IOException {
		if (this.inputStream == null) {
			throw new IOException("Pipe not connected.");
		}
		this.inputStream.receive(b);
	}
	
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		if (this.inputStream == null) {
			throw new IOException("Pipe not connected.");
		} else if (b == null) {
			throw new NullPointerException();
		} else if ((off < 0) || (off > b.length) || (len < 0) ||
                ((off + len) > b.length) || ((off + len) < 0)) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) {
			return;
		}
		this.inputStream.receive(b, off, len);
	}
	
	@Override
	public void flush() throws IOException {
		if (this.inputStream != null) {
			synchronized (this.inputStream) {
				this.inputStream.notifyAll();
			}
		}
	}
	
	@Override
	public void close() throws IOException {
		if (this.inputStream != null) {
			this.inputStream.receivedLast();
		}
	}

}
