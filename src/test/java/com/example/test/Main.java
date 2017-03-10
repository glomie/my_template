package com.example.test;

import java.io.*;
import java.util.*;

public class Main {
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		Scanner input = new Scanner(new FileInputStream(FileDescriptor.in));
		int n = input.nextInt(), m = input.nextInt();
		int[] arr = new int[100];
		int start = n - m % n;
		for(int i = 0; i < n; i++) arr[i] = input.nextInt();
		for(int i = start; i < n; i++) System.out.print(arr[i] + " ");
		for(int i = 0; i < start; i++) System.out.print(arr[i] + ((i + 1 < start) ? " " : "\n"));
	}
}
