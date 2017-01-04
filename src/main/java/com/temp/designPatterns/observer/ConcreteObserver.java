package com.temp.designPatterns.observer;

public class ConcreteObserver implements Observer {
	
	private String name;
	
	public ConcreteObserver(String name) {
		this.name = name;
	}

	@Override
	public void update(Subject subject) {
		System.out.println(this.name + "收到"+subject.getName() + " 小说的出版消息");
	}

}
