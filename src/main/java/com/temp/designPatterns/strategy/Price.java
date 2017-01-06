package com.temp.designPatterns.strategy;

public class Price {

	private Strategy strategy;
	
	public Price(Strategy strategy) {
		this.strategy = strategy;
	}
	
	public double quote(double price) {
		return this.strategy.calcPrice(price);
	}
}
