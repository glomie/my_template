package com.temp.designPatterns.abstractFactory;

public class ConcreteFactory1 implements AbstractFactory {

	@Override
	public AbstractProductA createProductA() {
		return new ProductA1();
	}

	@Override
	public AbstractProductB createProductB() {
		return new ProductB1();
	}

	@Override
	public void work() {
		createProductA().workA();
		createProductB().workB();
	}

}
