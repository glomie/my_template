package com.temp.calculator.the.game.calc;

public class Invert10Calculate extends AbstractCalculate implements Calculate {

	@Override
	public int result(int data) {
		boolean isPositive = true;
		if(data <= 0) {
			data = -data;
			isPositive = false;
		}
		String val = String.valueOf(data);
		char[] arr = val.toCharArray();
		StringBuilder sb = new StringBuilder();
		for(char c : arr) {
			int temp = c - '0';
			if(temp > 0) {
				temp = 10 - temp;
			}
			sb.append(temp);
		}
		int result = Integer.valueOf(sb.toString());
		return isPositive ? result : -result;
	}

	@Override
	public String toString() {
		return "Inv 10";
	}

	@Override
	public boolean withNumber() {
		return false;
	}
}
