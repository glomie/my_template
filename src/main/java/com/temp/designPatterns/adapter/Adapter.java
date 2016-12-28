package com.temp.designPatterns.adapter;

public class Adapter implements Target {
	
	private Adaptee adaptee;

	
	@Override
	public void request() {
		adaptee.specificRequest();
	}

	public Adaptee getAdaptee() {
		return adaptee;
	}

	public void setAdaptee(Adaptee adaptee) {
		this.adaptee = adaptee;
	}
}
