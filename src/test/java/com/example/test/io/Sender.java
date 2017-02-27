package com.example.test.io;

import java.io.IOException;

public class Sender extends Thread {
	
	private MyPipedOutputStream outputStream = new MyPipedOutputStream();

	public MyPipedOutputStream getOutputStream() {
		return this.outputStream;
	}
	
	@Override
	public void run() {
		writeShortMessage();
	}
	
	private void writeShortMessage() {
        String strInfo = "this is a short message" ;
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < 100; i++) {
        	sb.append(strInfo).append('\n');
        }
        try {
        	outputStream.write(sb.toString().getBytes());
        	try {
				Thread.sleep(900);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	outputStream.write("plus this end".getBytes());
        	outputStream.close();   
        } catch (IOException e) {   
            e.printStackTrace();   
        }   
    }
}
