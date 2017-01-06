package com.temp.designPatterns.strategy;

public class NormalCustomerStrategy implements Strategy {

	@Override
	public double calcPrice(double price) {
		System.out.println("新用户没有折扣");
		return price;
	}

}
