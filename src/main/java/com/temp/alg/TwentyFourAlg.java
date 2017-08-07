package com.temp.alg;

import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

public class TwentyFourAlg {

	public static void main(String[] args) {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		while(scanner.hasNextInt()) {
			int[] arr = new int[4];
			for(int i = 0; i < 4; i++) {
				arr[i] = scanner.nextInt();
			}
			func(arr);
		}
	}
	
	private static void func(int... num) {
		if(num.length == 1) {
			if(num[0] != 24) {
				return;
			}
		}
		for(int i = 0; i < num.length; i++) {
			for(int j = 0; j < num.length; j++) {
				if(i != j) {
					
				}
			}
		}
	}
	
	public void say() {
		System.out.println(StringUtils.left("hello,world", 6));
	}
}
