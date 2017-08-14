package com.temp.calculator.the.game.calc;

public abstract class AbstractCalculate implements Calculate {

	protected Integer input;
	
	protected Integer input2;
	
	protected String input3;
	
	protected String input4;
	
    AbstractCalculate() {}
	
    protected AbstractCalculate(int input) {
    	this.input = input;
    }
    
    protected AbstractCalculate(int input, int input2) {
    	this.input = input;
    	this.input2 = input2;
    }
    
    protected AbstractCalculate(String input3, String input4) {
		this.input3 = input3;
		this.input4 = input4;
	}
    
    public void setInput(int input) {
    	this.input = input;
    }
    
    public Integer getInput() {
    	return input;
    }
}
