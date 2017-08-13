package com.temp.calculator.the.game.calc;

public class MirrorCalculate extends AbstractCalculate implements Calculate {

	@Override
	public int result(int data) {
		boolean isPositive = true;
		if(data <= 0) {
			data = -data;
			isPositive = false;
		}
		String val = String.valueOf(data);
		char[] arr = val.toCharArray();
		StringBuilder sb = new StringBuilder(val);
		for(int i = arr.length - 1; i >= 0; i--) {
			sb.append(arr[i]);
		}
		int result = Integer.valueOf(sb.toString());
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
