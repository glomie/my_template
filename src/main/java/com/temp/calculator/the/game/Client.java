package com.temp.calculator.the.game;

import java.util.ArrayList;
import java.util.List;

import com.temp.calculator.the.game.calc.AppendCalculate;
import com.temp.calculator.the.game.calc.Calculate;
import com.temp.calculator.the.game.calc.Panel;

public class Client {

	public static void main(String[] args) {
		InputData inputData = new InputData();
		inputData.init();
		dfs(inputData, 0, inputData.getInitNum());
	}
	
	private static void dfs(InputData data, int layer, int currentNum) {
		if(layer > data.getSteps()) return;
		
		if(data.getIsGate()) currentNum = data.getGateCalculate().result(currentNum);
		
		if(currentNum == data.getResultNum()) {
			if(data.isHasStore()) {
				List<String> tempList = new ArrayList<String>(data.getPrintStack());
				List<Integer> storeUses = new ArrayList<>();
				for(String str : tempList) {
					if(str.contains("Append")) {
						storeUses.add(Integer.parseInt(str.split(" ")[1]));
					}
				}
				List<String> printList = new ArrayList<>();
				for(String str : tempList) {
					if(str.contains("Store")) {
						int st = Integer.parseInt(str.split(" ")[1]);
						if(storeUses.contains(st)) {
							printList.add(str);
						}
					}else {
						printList.add(str);
					}
				}
				System.out.println(printList);
			}else {
				System.out.println(data.getPrintStack());
			}
			
			return;
		}
		
		List<Calculate> funcList = data.getFuncList();
		if(data.isHasStore()) {
			funcList.add(new AppendCalculate(currentNum));
			data.getPrintStack().push("Store " + currentNum);
		}
		for(int i = 0; i < funcList.size(); i++) {
			Calculate calc = funcList.get(i);
			data.getPrintStack().push(calc.toString());
			if(calc instanceof Panel) {
				((Panel)calc).noticeAll(data.getFuncList());
			}
			dfs(data, layer + 1, calc.result(currentNum));
			if(calc instanceof Panel) {
				((Panel)calc).restoreAll(data.getFuncList());
			}
			data.getPrintStack().pop();
		}
		if(data.isHasStore()) {
			funcList.remove(funcList.size() - 1);
			data.getPrintStack().pop();
		}
	}
}


