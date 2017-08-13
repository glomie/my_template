package com.temp.calculator.the.game.calc;

public class OppositeCalculate extends AbstractCalculate implements Calculate {

	@Override
	public int result(int data) {
		return -data;
	}

	@Override
	public String toString() {
		return "+/-";
	}

	@Override
	public boolean withNumber() {
		return false;
	}
}
