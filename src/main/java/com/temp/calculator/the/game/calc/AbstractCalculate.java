package com.temp.calculator.the.game.calc;

public abstract class AbstractCalculate implements Calculate {

	protected Integer input;
	
	protected Integer input2;
	
    AbstractCalculate() {}
	
    protected AbstractCalculate(int input) {
    	this.input = input;
    }
    
    protected AbstractCalculate(int input, int input2) {
    	this.input = input;
    	this.input2 = input2;
    }
    
    public Integer getInput() {
    	return input;
    }
    
    public Integer getInput2() {
    	return input2;
    }
}
