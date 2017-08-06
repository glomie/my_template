package com.example.test.sort;

public class InsertSort implements ISort {

	@Override
	public void sort(int[] data) {
		for(int i = 1; i < data.length; i++) {
			int temp = data[i];
			int j;
			for(j = i - 1; j >= 0 && data[j] > temp; j--) {
				data[j + 1] = data[j];
			}
			data[j + 1] = temp;
		}
	}

}
