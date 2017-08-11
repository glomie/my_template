package com.temp.calculator.the.game;

public class ShiftRightCalculate extends AbstractCalculate implements Calculate {

	@Override
	public int result(int data) {
		if(data < 10) return data;
		String val = String.valueOf(data);
		int remain = Integer.valueOf(val.substring(0, val.length() - 1));
		int low = Integer.valueOf(val.substring(val.length() - 1, val.length()));
		int result = remain;
		if(low > 0) {
			result = Integer.valueOf(String.valueOf(low) + String.valueOf(remain));
		}
		return result;
	}
	
	@Override
	public String toString() {
		return "Shift>"; 
	}
}