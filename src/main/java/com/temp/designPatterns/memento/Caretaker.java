package com.temp.designPatterns.memento;

public class Caretaker {

	private Memento memento;
	
	public void saveMemento(Memento memento) {
		this.memento = memento;
	}
	
	public Memento retriveMemento() {
		return this.memento;
	}
}
