package com.temp.calculator.the.game.calc;

public class TransferCalculate extends AbstractCalculate implements Calculate {
	
	public TransferCalculate(int input1, int input2) {
		super(input1, input2);
	}

	@Override
	public int result(int data) {
		String val = String.valueOf(data);
		val = val.replaceAll(String.valueOf(input), String.valueOf(input2));
		try {
			return Integer.valueOf(val);
		}catch (Exception e) {
			return data;
		}
		
	}

	@Override
	public String toString() {
		return super.input + "=>" + super.input2;
	}

	@Override
	public boolean withNumber() {
		return false;
	}
}
