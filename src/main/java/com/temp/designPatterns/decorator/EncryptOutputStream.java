package com.temp.designPatterns.decorator;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class EncryptOutputStream extends FilterOutputStream {
	
	public EncryptOutputStream(OutputStream out) {
		super(out);
	}

	@Override
	public void write(int b) throws IOException {
		b = b + 2;
		if(b >= 97 + 26) {
			b = b - 26;
		}
		super.write(b);
	}
}
