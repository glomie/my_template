package com.temp.designPatterns.composite;

public abstract class Component {

	public abstract void someOperation();
	
	public void addChild(Component child) {
		throw new UnsupportedOperationException();
	}
	
	public void removeChild(Component child) {
		throw new UnsupportedOperationException();
	}
	
	public Component getChildren(int index) {
		throw new UnsupportedOperationException();
	}
}
