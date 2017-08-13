package com.temp.calculator.the.game.calc;

public class CutTailCalculate extends AbstractCalculate implements Calculate {

	@Override
	public int result(int data) {
		return data / 10;
	}
	
	@Override
	public String toString() {
		return "<<";
	}

	@Override
	public boolean withNumber() {
		return false;
	}

}
