package com.temp.designPatterns.flyweight;

import java.util.HashMap;
import java.util.Map;

public class FlyweightFactory {
	
	private FlyweightFactory() {}

	private static class FlyweightFactoryHolder {
		private static final FlyweightFactory FACTORY = new FlyweightFactory();
	}
	
	public static FlyweightFactory getInstance() {
		return FlyweightFactoryHolder.FACTORY;
	}
	
	private Map<String, Flyweight> fsMap = new HashMap<>();
	
	public Flyweight getFlyweight(String key) {
		Flyweight f = fsMap.get(key);
		if(f == null) {
			f= new AuthorizationFlyweight(key);
			fsMap.put(key, f);
		}
		
		return f;
	}
}
