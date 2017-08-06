package com.example.test.sort;

public class ShellSort implements ISort {

	@Override
	public void sort(int[] data) {
		int d = data.length;
		while(true) {
			d /= 2;
			for(int i = 0; i < d; i++) {
				for(int j = i + d; j < data.length; j = j + d) {
					int temp = data[j];
					int k;
					for(k = j - d; k >= 0 && data[k] > temp; k = k - d) {
						data[k + d] = data[k];
					}
					data[k + d] = temp;
				}
			}
			if(d == 1) break;
		}
	}

}
