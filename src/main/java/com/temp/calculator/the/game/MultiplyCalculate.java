package com.temp.calculator.the.game;

public class MultiplyCalculate extends AbstractCalculate implements Calculate {
	
	public MultiplyCalculate(int input) {
		super(input);
	}

	@Override
	public int result(int data) {
		return data * super.input;
	}

}
