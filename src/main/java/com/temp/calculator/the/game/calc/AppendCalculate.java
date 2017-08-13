package com.temp.calculator.the.game.calc;

public class AppendCalculate extends AbstractCalculate implements Calculate {

	public AppendCalculate(int input) {
		super(input);
	}
	
	@Override
	public int result(int data) {
		return data * getPow(super.input) + super.input;
	}
	
	private int getPow(int input) {
		int res = 1;
		while(input > 0) {
			res *= 10;
			input /= 10;
		}
		return res;
	}

	@Override
	public String toString() {
		return "Append " + super.input;
	}

	@Override
	public boolean withNumber() {
		return true;
	}
}
