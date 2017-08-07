package com.example.test.sort;

public class MergeSort implements ISort {

	@Override
	public void sort(int[] data) {
		sort(data, 0, data.length - 1);
	}
	
	private void sort(int[] data, int left, int right) {
		if(left >= right) return;
		int mid = (left + right) >> 1;
		sort(data, left, mid);
		sort(data, mid + 1, right);
		merge(data, left, mid, right);
	}
	
	private void merge(int[] data, int left, int mid, int right) {
		int[] temp = new int[right - left + 1];
		int i = left, j = mid + 1;
		int k = 0;
		while(i <= mid && j <= right) {
			temp[k++] = data[i] < data[j] ? data[i++] : data[j++];
		}
		while(i <= mid) temp[k++] = data[i++];
		while(j <= right) temp[k++] = data[j++];
		for(int x = 0; x < temp.length; x++) {
			data[left + x] = temp[x];
		}
	}

}
