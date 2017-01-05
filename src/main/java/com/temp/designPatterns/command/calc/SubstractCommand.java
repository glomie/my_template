package com.temp.designPatterns.command.calc;

public class SubstractCommand implements Command {
	
	private OperationApi operationApi;
	
	public SubstractCommand(OperationApi operationApi) {
		this.operationApi = operationApi;
	}

	@Override
	public void execute() {
		operationApi.substract(opeNum);
	}

	@Override
	public void undo() {
		operationApi.add(opeNum);
	}
	
	public int getOpeNum() {
		return opeNum;
	}

	public void setOpeNum(int opeNum) {
		this.opeNum = opeNum;
	}

	private int opeNum;

	
	

}
