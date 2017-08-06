package com.example.test.sort;

public class BinaryInsertSort implements ISort {

	@Override
	public void sort(int[] data) {
		for(int i = 1; i < data.length; i++) {
			int temp = data[i];
			int low = 0, high = i - 1;
			while(low <= high) {
				int mid = (low + high) >> 1;
				if(temp < data[mid]) {
					high = mid - 1;
				}else {
					low = mid + 1;
				}
			}
			for(int j = i; j >= low + 1; j--) {
				data[j] = data[j - 1];
			}
			data[low] = temp;
		}
	}

}
