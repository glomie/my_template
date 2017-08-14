package com.temp.calculator.the.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import com.temp.calculator.the.game.calc.AddCalculate;
import com.temp.calculator.the.game.calc.AppendCalculate;
import com.temp.calculator.the.game.calc.Calculate;
import com.temp.calculator.the.game.calc.CutTailCalculate;
import com.temp.calculator.the.game.calc.DivideCalculate;
import com.temp.calculator.the.game.calc.GateCalculate;
import com.temp.calculator.the.game.calc.Invert10Calculate;
import com.temp.calculator.the.game.calc.MirrorCalculate;
import com.temp.calculator.the.game.calc.MultiplyCalculate;
import com.temp.calculator.the.game.calc.OppositeCalculate;
import com.temp.calculator.the.game.calc.PlusAllCalculate;
import com.temp.calculator.the.game.calc.ReverseCalculate;
import com.temp.calculator.the.game.calc.ShiftLeftCalculate;
import com.temp.calculator.the.game.calc.ShiftRightCalculate;
import com.temp.calculator.the.game.calc.SubtractCalculate;
import com.temp.calculator.the.game.calc.SumCalculate;
import com.temp.calculator.the.game.calc.TransferCalculate;

public class InputData {
	
	/** 4 750 7 1 2 4 1 6 7 4 3 3 8 0 */
	public void init() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("请输入初始值:");
		this.initNum = scanner.nextInt();
		System.out.println("请输入结果值:");
		this.resultNum = scanner.nextInt();
		System.out.println("请输入步数:");
		this.steps = scanner.nextInt();
		System.out.println("是否为传送门(是 -- 1, 否 -- 0):");
		this.isGate = scanner.nextInt() == 1 ? true : false;
		if(isGate) {
			System.out.println("请输入传送门位置(由小到大):");
			this.gateCalculate = new GateCalculate(scanner.nextInt(), scanner.nextInt());
		}
		
		while(true) {
			System.out.println("请输入九宫格的按键<0表示结束>:(1.加,2.减,3.乘,4.除,5.反转,6.镜像,7.补位,8.反10,9.求和,10.正负,11.转换,12.左移,13.右移,14.去尾,15.加成,16.存储)");
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
			case 11:
				calc = new TransferCalculate(scanner.next(), scanner.next());
				break;
			case 12:
				calc = new ShiftLeftCalculate();
				break;
			case 13:
				calc = new ShiftRightCalculate();
				break;
			case 14:
				calc = new CutTailCalculate();
				break;
			case 15:
				calc = new PlusAllCalculate(scanner.nextInt());
				break;
			case 16:
				this.hasStore = true;
			}
			
			if(input != 16) {
				this.funcList.add(calc);
			}
		}
		scanner.close();
	}
	
	private int initNum;

	private int resultNum;
	
	private int steps;
	
	private boolean isGate;
	
	private Calculate gateCalculate;

	private List<Calculate> funcList = new ArrayList<>();
	
	private Stack<String> printStack = new Stack<>();
	
	private boolean hasStore;
	
	public boolean isHasStore() {
		return hasStore;
	}

	public Calculate getGateCalculate() {
		return gateCalculate;
	}

	public Stack<String> getPrintStack() {
		return printStack;
	}

	public int getInitNum() {
		return initNum;
	}

	public int getResultNum() {
		return resultNum;
	}

	public int getSteps() {
		return steps;
	}

	public boolean getIsGate() {
		return isGate;
	}

	public List<Calculate> getFuncList() {
		return funcList;
	}
	
	public void setFuncList(List<Calculate> funcList) {
		this.funcList = funcList;
	}
}
