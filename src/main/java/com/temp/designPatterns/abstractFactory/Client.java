package com.temp.designPatterns.abstractFactory;

public class Client {

	public static void main(String[] args) {
		AbstractFactory factory = new ConcreteFactory2();
		factory.work();
	}
}
