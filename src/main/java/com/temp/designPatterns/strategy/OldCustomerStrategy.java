package com.temp.designPatterns.strategy;

public class OldCustomerStrategy implements Strategy {

	@Override
	public double calcPrice(double price) {
		System.out.println("老用户折扣5%");
		return price * (1 - 0.05);
	}

}
