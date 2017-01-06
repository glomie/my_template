package com.temp.designPatterns.strategy;

public class Client {

	public static void main(String[] args) {
		Strategy strategy = new LargeCustomerStrategy();
		Price p = new Price(strategy);
		double res = p.quote(100);
		System.out.println(res);
	}
}
