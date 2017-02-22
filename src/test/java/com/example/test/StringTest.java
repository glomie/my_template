package com.example.test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import org.springframework.expression.AccessException;

public class StringTest {

	public static void main(String[] args) {
		String a = "a" + "b" + 1;
		String b = "ab1";
		String c = new String("ab1");
		System.out.println(a == b);
		System.out.println(a == c);
		System.out.println(a.hashCode());
		System.out.println("qweasd".startsWith("wea", 0));
		System.out.println("qwee".replace("e", "a"));
		List<String> list = new ArrayList<>();
		list.add("qwer");
		list.add("zxcv");
		System.out.println(list.indexOf(new String("qwer")) >= 0);
		Stack<String> stack = new Stack<>();
		stack.push(b);
		System.out.println(stack.peek());
		try {
			format("2");
		} catch (AccessException e) {
			System.out.println("AccessException");
		}
		
		InputStream is = System.in;
		System.out.println(is.toString());
	}
	
	public static int format(String str) throws AccessException {
		return Integer.valueOf(str);
	}
}

interface Inter1 {
	void say();
}

interface Inter2 extends Inter1 {
	void say(String str);
}

class Impl implements Inter2 {

	@Override
	public void say() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void say(String str) {
		// TODO Auto-generated method stub
		
	}
	
}