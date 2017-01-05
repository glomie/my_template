package com.temp.designPatterns.command.calc;

public class Operation implements OperationApi {

	private int result = 0;
	
	private int pastResult = 0;
	
	public Operation(int num) {
		this.result = num;
		this.pastResult = num;
	}
	
	@Override
	public int getResult() {
		return this.result;
	}

	@Override
	public int getPastResult() {
		return this.pastResult;
	}

	@Override
	public void add(int num) {
		pastResult = result;
		result += num;
	}

	@Override
	public void substract(int num) {
		pastResult = result;
		result -= num;
	}

}
