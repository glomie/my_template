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
        try {
        	outputStream.write(strInfo.getBytes());
        	outputStream.close();   
        } catch (IOException e) {   
            e.printStackTrace();   
        }   
    }
}
