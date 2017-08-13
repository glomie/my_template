package com.temp.calculator.the.game.calc;

public class SumCalculate extends AbstractCalculate implements Calculate {

	@Override
	public int result(int data) {
		int result = 0;
		boolean isPositive = true;
		if(data <= 0) {
			data = -data;
			isPositive = false;
		}
		while(data > 0) {
			result += data % 10;
			data /= 10;
		}
		return isPositive ? result : -result;
	}

	@Override
	public String toString() {
		return "Sum";
	}

	@Override
	public boolean withNumber() {
		return false;
	}
}
