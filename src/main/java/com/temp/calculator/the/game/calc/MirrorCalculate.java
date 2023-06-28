package com.temp.calculator.the.game.calc;

public class MirrorCalculate extends AbstractCalculate implements Calculate {

	@Override
	public int result(int data) {
		boolean isPositive = true;
		if (data < 0) {
			data = -data;
			isPositive = false;
		}
		int result = Integer.valueOf(new StringBuilder(data).reverse().toString());
		return isPositive ? result : -result;
	}

	@Override
	public String toString() {
		return "Mirror";
	}

	@Override
	public boolean withNumber() {
		return false;
	}
}
