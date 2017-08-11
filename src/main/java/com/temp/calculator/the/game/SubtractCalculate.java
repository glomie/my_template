package com.temp.calculator.the.game;

public class SubtractCalculate extends AbstractCalculate implements Calculate {
	
	public SubtractCalculate(int input) {
		super(input);
	}

	@Override
	public int result(int data) {
		return data - super.input;
	}
	
	@Override
	public String toString() {
		return "-" + super.input;
	}

}
