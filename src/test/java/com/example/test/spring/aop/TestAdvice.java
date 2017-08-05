package com.example.test.spring.aop;

import org.springframework.aop.AfterAdvice;
import org.springframework.aop.BeforeAdvice;
import org.springframework.aop.framework.ProxyFactory;

public class TestAdvice {

	public static void main(String[] args) {
		BeforeAdvice beforeAdvice = new GreetingBeforeAdvice();
		AfterAdvice afterAdvice = new GreetingAfterAdvice();
		Waiter target = new NaiveWaiter();
		
		ProxyFactory factory = new ProxyFactory();
		factory.setProxyTargetClass(true);
		factory.setTargetClass(NaiveWaiter.class);
		factory.addAdvice(beforeAdvice);
		factory.addAdvice(afterAdvice);
		Waiter proxy = (Waiter) factory.getProxy();
		proxy.greetTo("ok");
	}
}
