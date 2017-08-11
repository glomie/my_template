package com.temp.calculator.the.game;

public class TransferCalculate extends AbstractCalculate implements Calculate {
	
	public TransferCalculate(int input1, int input2) {
		super(input1, input2);
	}

	@Override
	public int result(int data) {
		String val = String.valueOf(data);
		val = val.replaceAll(String.valueOf(input), String.valueOf(input2));
		return Integer.valueOf(val);
	}

	@Override
	public String toString() {
		return super.input + "=>" + super.input2;
	}
}
