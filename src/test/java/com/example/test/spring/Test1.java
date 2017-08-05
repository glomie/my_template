package com.example.test.spring;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;

public class Test1 {

	public static void main(String[] args) {
		ClassPathResource resource = new ClassPathResource("test/bean.xml");
		DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
		reader.loadBeanDefinitions(resource);
		MyTestBean bean = factory.getBean(MyTestBean.class);
		System.out.println(bean.getStr());
	}
	
}
