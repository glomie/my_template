package com.example.test.lang;

import java.util.Arrays;
import java.util.TreeMap;

public class StringTest {

	public static void main(String[] args) {
		char[] value = new char[4];
		value[0] = 'a';
		value[1] = 'b';
		value[2] = '\0';
		System.out.println(Arrays.toString(value));
		TreeMap<MyClass, String> map = new TreeMap<>();
		MyClass mc = new MyClass();
		map.put(mc, "cccc");
	}
}

class MyClass {
	private String str;

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}
	
}
