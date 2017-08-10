package com.temp.calculator.the.game;

public abstract class AbstractCalculate implements Calculate {

	protected int input;
	
    AbstractCalculate() {}
	
    public AbstractCalculate(int input) {
    	this.input = input;
    }
}
