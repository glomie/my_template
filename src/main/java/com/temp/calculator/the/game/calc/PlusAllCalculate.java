package com.temp.calculator.the.game.calc;

import java.util.List;

public class PlusAllCalculate extends AbstractCalculate implements Calculate,Panel {
	
	public PlusAllCalculate(int input) {
		super(input);
	}

	@Override
	public int result(int data) {
		return data;
	}

	@Override
	public void awareAll(List<Calculate> all) {
		
	}

	@Override
	public boolean withNumber() {
		return true;
	} 

}
