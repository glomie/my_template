package com.temp.calculator.the.game.calc;

public class AddCalculate extends AbstractCalculate implements Calculate {
	
	public AddCalculate(int input) {
		super(input);
	}

	@Override
	public int result(int data) {
		return data + super.input;
	}
	
	@Override
	public String toString() {
		return "+" + super.input;
	}

	@Override
	public boolean withNumber() {
		return true;
	}

}
