package com.temp.calculator.the.game;

public class AppendCalculate extends AbstractCalculate implements Calculate {

	public AppendCalculate(int input) {
		super(input);
	}
	
	@Override
	public int result(int data) {
		return data * 10 + super.input;
	}

	@Override
	public String toString() {
		return "Append " + super.input;
	}
}
