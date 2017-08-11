package com.temp.calculator.the.game;

public abstract class AbstractCalculate implements Calculate {

	protected int input;
	
	protected int input2;
	
    AbstractCalculate() {}
	
    AbstractCalculate(int input) {
    	this.input = input;
    }
    
    AbstractCalculate(int input, int input2) {
    	this.input = input;
    	this.input2 = input2;
    }
}
