package com.temp.designPatterns.observer;

public class Novel extends Subject {

	public void pulish() {
		this.setName("哈利波特");
		System.out.println(" 小说出版了");
		this.notifyAllObservers();
	}
}
