package com.example.test.spring;

import java.io.IOException;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class TestDefaultResourceLoader {

	public static void main(String[] args) throws IOException {
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource("applicationContext.xml");
		System.out.println(resource.getURL().toString());
	}
}
