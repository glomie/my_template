package com.temp.calculator.the.game.calc;

public class TransferCalculate extends AbstractCalculate implements Calculate {
	
	public TransferCalculate(String input3, String input4) {
		super(input3, input4);
	}

	@Override
	public int result(int data) {
		String val = String.valueOf(data);
		val = val.replaceAll(input3, input4);
		try {
			return Integer.valueOf(val);
		}catch (Exception e) {
			return data;
		}
		
	}

	@Override
	public String toString() {
		return super.input3 + "=>" + super.input4;
	}

	@Override
	public boolean withNumber() {
		return false;
	}
}
