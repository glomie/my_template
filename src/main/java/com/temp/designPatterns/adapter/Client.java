package com.temp.designPatterns.adapter;

public class Client {

	public static void main(String[] args) {
		Adapter adapter = new Adapter();
		adapter.setAdaptee(new Adaptee());
		Target target = adapter;
		target.request();
	}
}
