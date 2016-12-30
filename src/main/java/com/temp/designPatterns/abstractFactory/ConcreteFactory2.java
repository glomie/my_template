package com.temp.designPatterns.abstractFactory;

public class ConcreteFactory2 implements AbstractFactory {

	@Override
	public AbstractProductA createProductA() {
		return new ProductA2();
	}

	@Override
	public AbstractProductB createProductB() {
		return new ProductB2();
	}

	@Override
	public void work() {
		createProductA().workA();
		createProductB().workB();
	}

}
