package com.temp.designPatterns.singleton;

public class Client {

	public static void main(String[] args) {
		LazySingleton lazySingleton = LazySingleton.getInstance();
		lazySingleton.setStr("asas");
		
		LazySingleton lazySingleton2 = LazySingleton.getInstance();
		System.out.println(lazySingleton2.getStr());
		lazySingleton2.setStr("asasasasassasas");
		
		LazySingleton lazySingleton3 = LazySingleton.getInstance();
		System.out.println(lazySingleton3.getStr());
	}
}
