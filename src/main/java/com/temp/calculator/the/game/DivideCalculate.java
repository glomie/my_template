package com.temp.calculator.the.game;

public class DivideCalculate extends AbstractCalculate implements Calculate {

	public DivideCalculate(int input) {
		super(input);
	}
	
	@Override
	public int result(int data) {
		return data / super.input;
	}

}
