package com.temp.designPatterns.observer;

import java.util.HashSet;
import java.util.Set;

public class Subject {

	private Set<Observer> observers = new HashSet<>();
	
	private String name;
	
	public void attach(Observer observer) {
		observers.add(observer);
	}
	
	public void detach(Observer observer) {
		observers.remove(observer);
	}
	
	protected void notifyAllObservers() {
		for(Observer observer : observers) {
			observer.update(this);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
