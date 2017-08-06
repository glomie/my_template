package com.example.test.sort;

import java.util.Arrays;

public class Client {

	private static int[] data = {49,38,65,97,76,13,27,49,55,04};
	
	public static void main(String[] args) {
		ISort sort = new HeapSort();
		sort.sort(data);
		System.out.println(Arrays.toString(data));
	}
}
