package com.temp.designPatterns.command.calc;

public interface OperationApi {

	public int getResult();
	
	public int getPastResult();
	
	public void add(int num);
	
	public void substract(int num);
}
