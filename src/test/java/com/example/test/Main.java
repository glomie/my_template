package com.example.test;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		int prev = 2, count = 0;
		for(int i = 2; i <= n; i++) {
			if(isPrime(i)) {
				if(i - prev == 2) count++;
				prev = i;
			}
		}
		System.out.println(count);
	}
	
	private static boolean isPrime(int x) {
		for(int i = 2; i * i <= x; i++) {
			if(x % i == 0) return false;
		}
		return true;
	}
}
