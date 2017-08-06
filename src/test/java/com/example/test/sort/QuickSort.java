package com.example.test.sort;

public class QuickSort implements ISort {

	@Override
	public void sort(int[] data) {
		sort(data, 0, data.length - 1);
	}

	private void sort(int[] data, int l, int r) {
		if(l >= r) return;
		int index = partition(data, l, r);
		sort(data, l, index - 1);
		sort(data, index + 1, r);
	}
	
	private int partition(int[] data, int l, int r) {
		int key = data[l];
		while(l < r) {
			while(data[r] >= key && r > l) r--;
			data[l] = data[r];
			while(data[l] <= key && l < r) l++;
			data[r] = data[l];
		}
		data[r] = key;
		return r;
	}
}
