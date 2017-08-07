package com.example.test;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class Test3 {

	public static void main(String[] args) throws Exception {
		System.out.println(ClassLoader.getSystemClassLoader().getParent());
		@SuppressWarnings("resource")
		ClassLoader cl = new URLClassLoader(new URL[] {new URL("file:/E:/新建文件夹/workspace2/my-template/target/classes")});
		Class<?> clazz = cl.loadClass("com.temp.alg.TwentyFourAlg");
		Method method = clazz.getDeclaredMethod("say");
		method.setAccessible(true);
		method.invoke(clazz.newInstance());
	}
}
