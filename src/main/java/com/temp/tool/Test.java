package com.temp.tool;

import java.util.UUID;

public class Test {

	public static void main(String[] args) {
		System.out.println(UUID.randomUUID().toString().replaceAll("-", "").toUpperCase());
		new ZiLei(1);
	}
}

class FuLei {
	
	public FuLei() {}
	
	public FuLei(String str) {
		System.out.println(str);
	}
}

class ZiLei extends FuLei {
	public ZiLei(int i) {
		super("sss");
		System.out.println("调用子类方法" + i);
	}
}
