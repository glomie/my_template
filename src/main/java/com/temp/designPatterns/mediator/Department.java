package com.temp.designPatterns.mediator;

public abstract class Department {

	private Mediator mediator;
	
	public Department(Mediator mediator) {
		this.mediator = mediator;
	}
	
	public Mediator getMediator() {
		return mediator;
	}
	
	abstract void selfAction();
	
	abstract void outAction();
}
