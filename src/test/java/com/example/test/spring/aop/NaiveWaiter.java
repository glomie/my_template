package com.example.test.spring.aop;

public class NaiveWaiter implements Waiter {

	@Override
	public String greetTo(String name) {
		System.out.println("doing。。。");
		return "ok";
	}

	@Override
	public String serveTo(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
