package com.temp.designPatterns.chain;

public abstract class Handler {

	protected Handler successor;

	public void setSuccessor(Handler successor) {
		this.successor = successor;
	}
	
	public abstract String handleFeeRequest(String user, double fee);
	
}
