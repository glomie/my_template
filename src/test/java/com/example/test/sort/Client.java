package com.example.test.sort;

import java.util.Arrays;

public class Client {

	private static int[] data = {49,38,65,97,76,13,27,49,55,04};
	
	public static void main(String[] args) {
		Sort sort = new InsertSort();
		int[] data2 = {2,1,0};
		sort.sort(data2);
		System.out.println(Arrays.toString(data2));
	}
}
