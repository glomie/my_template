package com.example.test.sort;

public class RadixSort implements ISort {

	@Override
	public void sort(int[] data) {
		int maxLen = 0;
		for(int i = 0; i < data.length; i++) {
			int len = String.valueOf(data[i]).length();
			if(len > maxLen) maxLen = len;
		}
		int[][] temp = new int[10][data.length];
		int[] col = new int[10];
		for(int m = 0; m < maxLen; m++) {
			for(int i = 0; i < data.length; i++) {
				int lsd = ((data[i] / (int)Math.pow(10, m)) % 10);
				temp[lsd][col[lsd]++] = data[i];
			}
			for(int i = 0, k = 0; i < 10; i++) {
				for(int j = 0; j < col[i]; j++) {
					data[k++] = temp[i][j];
				}
				col[i] = 0;
			}
		}
	}

}
