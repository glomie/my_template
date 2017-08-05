package com.example.test.spring.aop;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.ProxyFactory;

public class TestSpringAop {

	private static final Logger logger = LoggerFactory.getLogger(TestSpringAop.class);
	
	public static void main(String[] args) {
		ProxyFactory factory = new ProxyFactory();
		factory.setTargetSource(new TargetSource() {
			
			@Override
			public void releaseTarget(Object target) throws Exception {
				logger.info("finally, release target");
			}
			
			@Override
			public boolean isStatic() {
				return false;
			}
			
			@Override
			public Class<?> getTargetClass() {
				return Waiter1.class;
			}
			
			@Override
			public Object getTarget() throws Exception {
				return new Waiter1() {
					
					@Override
					public String say(String str) {
						logger.info("doing... " + str);
						return "ok";
					}
				};
			}
		});
		
		factory.addAdvice(new MethodBeforeAdvice() {
			
			@Override
			public void before(Method method, Object[] args, Object target) throws Throwable {
				logger.info("before... " + args[0]);
			}
		});
		factory.addAdvice(new AfterReturningAdvice() {
			
			@Override
			public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
				logger.info("after... " + returnValue);
			}
		});
		Waiter1 proxy = (Waiter1) factory.getProxy();
		proxy.say("ok");
	}
}

interface Waiter1 {
	public String say(String str);
}



