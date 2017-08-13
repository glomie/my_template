package com.temp.calculator.the.game.calc;

public class ReverseCalculate extends AbstractCalculate implements Calculate {

	@Override
	public int result(int data) {
		int result = 0;
		boolean isPositive = true;
		if(data <= 0) {
			data = -data;
			isPositive = false;
		}
		while(data > 0) {
			int temp = data % 10;
			result = result * 10 + temp;
			data /= 10;
		}
		return isPositive ? result : -result;
	}
	
	@Override
	public String toString() {
		return "Reverse";
	}

	@Override
	public boolean withNumber() {
		return false;
	}

}
