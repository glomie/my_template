package com.temp.designPatterns.observer;

public class Client {

	public static void main(String[] args) {
		Observer observer1 = new ConcreteObserver("observer1");
		Observer observer2 = new ConcreteObserver("observer2");
		Novel subject = new Novel();
		subject.attach(observer1);
		subject.attach(observer2);
		subject.pulish();
	}
	
}
