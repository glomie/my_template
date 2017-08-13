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
	public void noticeAll(List<Calculate> all) {
		for(Calculate calc : all) {
			if(calc.withNumber()) {
				AbstractCalculate aCalc = (AbstractCalculate)calc;
				aCalc.setInput(aCalc.getInput() + super.input);
			}
		}
	}
	
	@Override
	public String toString() {
		return "[+]" + super.input;
	}

	@Override
	public boolean withNumber() {
		return false;
	}

	@Override
	public void restoreAll(List<Calculate> all) {
		for(Calculate calc : all) {
			if(calc.withNumber()) {
				AbstractCalculate aCalc = (AbstractCalculate)calc;
				aCalc.setInput(aCalc.getInput() - super.input);
			}
		}
	} 

}
