package com.temp.designPatterns.command.calc;

public class Client {

	public static void main(String[] args) {
		OperationApi operationApi = new Operation(10);
		
		Calculator calculator = new Calculator();
		
		AddCommand addCommand = new AddCommand(operationApi);
		addCommand.setOpeNum(5);
		calculator.setAddCmd(addCommand);
		calculator.addPressed();
		System.out.println(operationApi.getPastResult() + " + " + addCommand.getOpeNum() + " = " + operationApi.getResult());
		
		SubstractCommand substractCommand = new SubstractCommand(operationApi);
		substractCommand.setOpeNum(3);
		calculator.setSubCmd(substractCommand);
		calculator.substractPressed();
		System.out.println(operationApi.getPastResult() + " - " + substractCommand.getOpeNum() + " = " + operationApi.getResult());
		calculator.undoPressed();
		System.out.println("当前值" + operationApi.getPastResult() + "撤销后的值为" + operationApi.getResult());
		calculator.undoPressed();
		System.out.println("当前值" + operationApi.getPastResult() + "再次撤销后的值为" + operationApi.getResult());
		calculator.undoPressed();
	}
}
