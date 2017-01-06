package com.temp.designPatterns.strategy;

public class LargeCustomerStrategy implements Strategy {

	@Override
	public double calcPrice(double price) {
		System.out.println("大客户折扣10%");
		return price * (1 - 0.1);
	}

}
