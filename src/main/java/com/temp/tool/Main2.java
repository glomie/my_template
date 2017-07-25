package com.temp.tool;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main2 {

	public static void main(String[] args) {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		for(int i = 0; i < n; i++) {
			char[] cc = new char[550];
			int m = 0;
			for(int j = 0; j < 26; j++) {
				int num = scanner.nextInt();
				for(int k = 0; k < num; k++) {
					cc[m++] = (char) ('A' + j);
				}
			}
			Map<String, Integer> map = new HashMap<String, Integer>();
			for(int j = 0; j < m; j++) {
				
			}
		}
		
	}
}

