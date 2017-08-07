package com.example.test.guice;

public class SimpleCalc implements Calc {

	@Override
	public int sum(int a, int b) {
		return a + b;
	}

}
