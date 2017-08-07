package com.example.test.guice;

import java.util.Map;
import java.util.logging.Logger;

import com.google.inject.Binding;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;

public class CalcClient {

	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new CalcModule());
		Calc calc = injector.getInstance(Calc.class);
		Map<Key<?>, Binding<?>> map = injector.getBindings();
		for(Map.Entry<Key<?>, Binding<?>> entry : map.entrySet()) {
			System.out.println(entry.getValue());
		}
		System.out.println(calc.sum(1, 2));
		Hello hello = injector.getInstance(Hello.class);
		Logger logger = injector.getInstance(Logger.class);
		logger.info("sadsadsadsadasd");
		hello.say();
	}
}
