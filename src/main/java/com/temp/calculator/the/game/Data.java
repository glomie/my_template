package com.temp.calculator.the.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Data {

	private int resultNum;
	
	private int steps;
	
	private int isGate;
	
	private List<Calculate> funcList = new ArrayList<>();
	
	public void init() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("请输入初始值:");
		this.initNum = scanner.nextInt();
		System.out.println("请输入结果值:");
		this.resultNum = scanner.nextInt();
		System.out.println("请输入步数:");
		this.steps = scanner.nextInt();
		System.out.println("是否为传送门(是 -- 1, 否 -- 0):");
		this.isGate = scanner.nextInt();
		
		while(true) {
			System.out.println("请输入九宫格的按键<0表示结束>:(1.加,2.减,3.乘,4.除,5.反转,6.镜像,7.补位,8.反10,9.求和,10.正负)");
			int input = scanner.nextInt();
			if(input == 0) break;
			Calculate calc = null;
			switch (input) {
			case 1:
				calc = new AddCalculate(scanner.nextInt());
				break;
			case 2:
				calc = new SubtractCalculate(scanner.nextInt());
				break;
			case 3:
				calc = new MultiplyCalculate(scanner.nextInt());
				break;
			case 4:
				calc = new DivideCalculate(scanner.nextInt());
				break;
			case 5:
				calc = new ReverseCalculate();
				break;
			case 6:
				calc = new MirrorCalculate();
				break;
			case 7:
				calc = new AppendCalculate(scanner.nextInt());
				break;
			case 8:
				calc = new Invert10Calculate();
				break;
			case 9:
				calc = new SumCalculate();
				break;
			case 10:
				calc = new OppositeCalculate();
				break;
			}
			
			this.funcList.add(calc);
		}
		scanner.close();
	}
	
private int initNum;
	
	public int getInitNum() {
		return initNum;
	}

	public void setInitNum(int initNum) {
		this.initNum = initNum;
	}

	public int getResultNum() {
		return resultNum;
	}

	public void setResultNum(int resultNum) {
		this.resultNum = resultNum;
	}

	public int getSteps() {
		return steps;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}

	public int getIsGate() {
		return isGate;
	}

	public void setIsGate(int isGate) {
		this.isGate = isGate;
	}

	public List<Calculate> getFuncList() {
		return funcList;
	}

	public void setFuncList(List<Calculate> funcList) {
		this.funcList = funcList;
	}
}
