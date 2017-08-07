package com.example.test.guice;

import com.google.inject.Binder;
import com.google.inject.Module;

public class CalcModule implements Module {

	@Override
	public void configure(Binder binder) {
		binder.bind(Calc.class).to(SimpleCalc.class);
		binder.bind(Hello.class);
	}

}
