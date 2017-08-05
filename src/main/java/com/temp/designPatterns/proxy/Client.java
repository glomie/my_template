package com.temp.designPatterns.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Client {

	public static void main(String[] args) {
		Impl obj = new Impl("junyan ");
		Api api = (Api) Proxy.newProxyInstance(Impl.class.getClassLoader(), Impl.class.getInterfaces(), new InvocationHandler() {
			
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				if(method.getName().contains("say")) {
					String str = (String)args[0];
					if(str.equals("hello")) {
						return method.invoke(obj, args);
					}else {
						System.out.println("没有权限");
						return null;
					}
				}else {
					System.out.println("before...");
					method.invoke(obj, args);
					System.out.println("after...");
					return null;
				}
			}
		});
		api.say("hello1");
		api.walk();
	}
}
