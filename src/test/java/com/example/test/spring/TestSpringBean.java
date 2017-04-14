package com.example.test.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestSpringBean {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		MyTestBean bean = applicationContext.getBean(MyTestBean.class);
		System.out.println(bean.getStr());
		applicationContext.start();
	}
}
