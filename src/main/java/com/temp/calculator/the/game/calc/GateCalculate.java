package com.temp.calculator.the.game.calc;

public class GateCalculate extends AbstractCalculate implements Calculate {
	
	public GateCalculate(int input1, int input2) {
		super(input1, input2);
	}

	@Override
	public int result(int data) {
		int p1 = getPow(input);
		int p2 = getPow(input2);
		if(data < p2) return data;
		int w2 = (data / p2) % 10;
		int gw = (data / p2) / 10;
		int temp = data % p2;
		int val = gw * p2 + (temp + w2 * p1);
		return result(val);
	}
	
	private int getPow(int input) {
		int res = 1;
		for(int i = 0 ; i < input - 1; i++) {
			res *= 10;
		}
		return res;
	}

	@Override
	public boolean withNumber() {
		return false;
	}
}
