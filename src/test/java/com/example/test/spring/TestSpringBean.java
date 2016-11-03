package com.example.test.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestSpringBean {

	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		MyTestBean bean = applicationContext.getBean(MyTestBean.class);
		System.out.println(bean.getStr());
	}
}
