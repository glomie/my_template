package com.temp.designPatterns.singleton;

public class Singleton {

	
	private Singleton() {}

	private static class SingletonHolder {
		private static Singleton instance = new Singleton();
	}
	
	public static Singleton getInstance() {
		return SingletonHolder.instance;
	}
}
