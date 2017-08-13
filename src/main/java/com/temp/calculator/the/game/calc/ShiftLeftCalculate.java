package com.temp.calculator.the.game.calc;

public class ShiftLeftCalculate extends AbstractCalculate implements Calculate {

	@Override
	public int result(int data) {
		if(data < 10) return data;
		String val = String.valueOf(data);
		int high = Integer.valueOf(val.substring(0,1));
		int remain = Integer.valueOf(val.substring(1));
		return remain * 10 + high;
	}
	
	@Override
	public String toString() {
		return "<Shift";
	}

	@Override
	public boolean withNumber() {
		return false;
	}
}
