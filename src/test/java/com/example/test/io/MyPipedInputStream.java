package com.example.test.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;

public class MyPipedInputStream extends InputStream {
	
	boolean closedByWriter = false;
	
	volatile boolean closedByReader = false;
	
	boolean connected = false;
	
	protected byte buffer[];
	
	protected int in = -1;
	
	protected int out = 0;
	
	Thread readSide;
	
	Thread writeSide;
	
	private static final int DEFAULT_PIPE_SIZE = 1024;
	
	protected static final int PIPE_SIZE = DEFAULT_PIPE_SIZE;
	
	public MyPipedInputStream() {
		this(DEFAULT_PIPE_SIZE);
	}
	
	public MyPipedInputStream(int pipeSize) {
		this.init(pipeSize);
	}
	
	public MyPipedInputStream(MyPipedOutputStream outputStream) throws IOException {
		this(outputStream, DEFAULT_PIPE_SIZE);
	}
	
	public MyPipedInputStream(MyPipedOutputStream outputStream, int pipeSize) throws IOException {
		this.init(pipeSize);
		this.connect(outputStream);
	}
	
	private void init(int pipeSize) {
		if (pipeSize <= 0) {
			throw new IllegalArgumentException("Pipe Size <= 0.");
		}
		this.buffer = new byte[pipeSize];
	}
	
	public void connect(MyPipedOutputStream outputStream) throws IOException {
		outputStream.connect(this);
	}
	
	protected synchronized void receive(int b) throws IOException {
		this.checkStateForReceive();
		writeSide = Thread.currentThread();
		
		if (in == out) {
			this.awaitSpace();
		}
		if (in < 0) {
			in = 0;
			out = 0;
		}
		
		buffer[in++] = (byte)(b & 0xFF);
		if (in >= buffer.length) {
			in = 0;
		}
	}
	
	synchronized void receive(byte[] b, int off, int len) throws IOException {
		this.checkStateForReceive();
		writeSide = Thread.currentThread();
		System.out.println(writeSide);
		int bytesToTransfer = len;
		while(bytesToTransfer > 0) {
			if (in == out) {
				awaitSpace();
			}
			
			int nextTransferAmount = 0;
			if (out < in) {
				nextTransferAmount = buffer.length - in;
			} else if (in < out) {
				if (in == -1) {
					in = out = 0;
					nextTransferAmount = buffer.length - in;
				} else {
					nextTransferAmount = out - in;
				}
			}
			
			if (nextTransferAmount > bytesToTransfer) {
				nextTransferAmount = bytesToTransfer;
			}
			assert (nextTransferAmount > 0);
			System.arraycopy(b, off, buffer, in, nextTransferAmount);
			bytesToTransfer -= nextTransferAmount;
			off += nextTransferAmount;
			in += nextTransferAmount;
			if (in >= buffer.length) {
				in = 0;
			}
			
		}
	}
	
	private void checkStateForReceive() throws IOException {
		if (!connected) {
			throw new IOException("Pipe not connected.");
		} else if (closedByWriter || closedByReader) {
			throw new IOException("Pipe closed.");
		} else if (readSide != null && !readSide.isAlive()) {
			throw new IOException("Read side dead.");
		}
	}
	
	private void awaitSpace() throws IOException {
		while (in == out) {
			this.checkStateForReceive();
			notifyAll();
			try {
				wait(1000);
			} catch (InterruptedException e) {
				throw new InterruptedIOException();
			}
		}
	}
	
	synchronized void receivedLast() {
		closedByWriter = true;
		notifyAll();
	}
	
	@Override
	public synchronized int read() throws IOException {
		if(!connected) {
			throw new IOException("Pipe not connected.");
		} else if (closedByReader) {
			throw new IOException("Pipe closed.");
		} else if (writeSide != null && !writeSide.isAlive()
                && !closedByWriter && (in < 0)) {
			throw new IOException("Write end dead.");
		}
		
		readSide = Thread.currentThread();
		System.out.println(readSide);
		int trials = 2;
		while (in < 0) {
			if (closedByWriter) {
				return -1;
			}
			if ((writeSide != null) && (!writeSide.isAlive()) && (--trials < 0)) {
				throw new IOException("Pipe broken.");
			}
			notifyAll();
			try {
				wait(1000);
			} catch (InterruptedException e) {
				throw new InterruptedIOException();
			}
		}
		int ret = buffer[out++] & 0xFF;
		if (out >= buffer.length) {
			out = 0;
		}
		if (in == out) {
			in = -1;
		}
		
		return ret;
	}
	
	@Override
	public synchronized int read(byte[] b, int off, int len) throws IOException {
		if (b == null) {
            throw new NullPointerException();
        } else if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }
		
		int c = this.read();
		if (c < 0) {
			return -1;
		}
		b[off] = (byte)c;
		int rlen = 1;
		while ((in >= 0) && (len > 1)) {
			int available;
			if (in > out) {
				available = Math.min((buffer.length - out), (in - out));
			} else {
				available = buffer.length - out;
			}
			
			if (available > (len - 1)) {
				available = len - 1;
			}
			
			System.arraycopy(buffer, out, b, off + rlen, available);
			out += available;
			rlen += available;
			len -= available;
			
			if (out >= buffer.length) {
				out = 0;
			}
			
			if (in == out) {
				in = -1;
			}
		}
		
		return rlen;
	}
	
	@Override
	public synchronized int available() throws IOException {
		if (in < 0) {
			return 0;
		} else if (in == out) {
			return buffer.length;
		} else if (in > out) {
			return in - out;
		} else {
			return in + buffer.length - out;
		}
	}
	
	@Override
	public void close() throws IOException {
		closedByReader = true;
		synchronized (this) {
			in = -1;
		}
	}

}
