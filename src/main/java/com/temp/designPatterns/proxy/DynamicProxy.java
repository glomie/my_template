package com.temp.designPatterns.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class DynamicProxy implements InvocationHandler {
	
	private Api api;

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if(method.getName().contains("say")) {
			String str = (String)args[0];
			if(str.equals("hello")) {
				return method.invoke(api, args);
			}else {
				System.out.println("没有权限");
			}
		}
		else {
			return method.invoke(api, args);
		}
		return null;
	}

	public Api getApi() {
		return api;
	}

	public void setApi(Api api) {
		this.api = api;
	}

}
