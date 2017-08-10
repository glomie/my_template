package com.temp.calculator.the.game;

public class AddCalculate extends AbstractCalculate implements Calculate {
	
	public AddCalculate(int input) {
		super(input);
	}

	@Override
	public int result(int data) {
		return data + super.input;
	}

}
