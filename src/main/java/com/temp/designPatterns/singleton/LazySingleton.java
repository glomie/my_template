package com.temp.designPatterns.singleton;

public class LazySingleton {
	
	private static LazySingleton instance = null;

	private LazySingleton() {}

	public static synchronized LazySingleton getInstance() {
		if(instance == null) {
			instance = new LazySingleton();
		}
		
		return instance;
	}
	
	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	private String str;
	
	
}
