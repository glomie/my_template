package com.temp.tool;

import java.util.UUID;

public class Test {

	public static void main(String[] args) {
		System.out.println(UUID.randomUUID().toString().replaceAll("-", "").toUpperCase());
	}
}
