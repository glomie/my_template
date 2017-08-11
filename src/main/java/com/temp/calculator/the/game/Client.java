package com.temp.calculator.the.game;

import java.util.Stack;

public class Client {

	public static void main(String[] args) {
		InputData inputData = new InputData();
		inputData.init();
		dfs(inputData, 0, inputData.getInitNum());
	}
	
	
	private static Stack<Calculate> stack = new Stack<>();
	
	private static void dfs(InputData data, int layer, int currentNum) {
		if(layer > data.getSteps()) return;
		
		if(data.getIsGate()) currentNum = data.getGateCalculate().result(currentNum);
		
		if(currentNum == data.getResultNum()) {
			System.out.println(stack);
			return;
		}
		
		for(Calculate calc : data.getFuncList()) {
			stack.push(calc);
			dfs(data, layer + 1, calc.result(currentNum));
			stack.pop();
		}
	}
}


