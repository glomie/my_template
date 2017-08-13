package com.temp.calculator.the.game.calc;

public class MultiplyCalculate extends AbstractCalculate implements Calculate {
	
	public MultiplyCalculate(int input) {
		super(input);
	}

	@Override
	public int result(int data) {
		return data * super.input;
	}
	
	@Override
	public String toString() {
		return "X" + super.input;
	}

	@Override
	public boolean withNumber() {
		return true;
	}

}
