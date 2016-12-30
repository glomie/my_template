package com.temp.designPatterns.abstractFactory;

public interface AbstractFactory {
	
	public void work();

	public AbstractProductA createProductA();
	
	public AbstractProductB createProductB();
	
}
