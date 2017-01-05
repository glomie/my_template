package com.temp.designPatterns.command.calc;

public class AddCommand implements Command {

	private OperationApi operationApi;
	
	public AddCommand(OperationApi operationApi) {
		this.operationApi = operationApi;
	}
	
	@Override
	public void execute() {
		operationApi.add(opeNum);
	}

	@Override
	public void undo() {
		operationApi.substract(opeNum);
	}
	
	public int getOpeNum() {
		return opeNum;
	}

	public void setOpeNum(int opeNum) {
		this.opeNum = opeNum;
	}

	private int opeNum;
	
	

}
