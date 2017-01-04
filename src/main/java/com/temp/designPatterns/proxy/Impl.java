package com.temp.designPatterns.proxy;

public class Impl implements Api {
	
	private String tag;
	
	public Impl(String tag) {
		this.tag = tag;
	}

	@Override
	public void say(String str) {
		System.out.println(tag + "say " + str);
	}

	@Override
	public void walk() {
		System.out.println("I'm walking!");
	}

}
