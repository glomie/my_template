package com.example.test.sort;

public class HeapSort implements ISort {

	@Override
	public void sort(int[] data) {
		buildMaxHeap(data);
		for(int i = data.length - 1; i >= 1; i--) {
			swap(data, 0, i);
			maxHeap(data, i, 0);
		}
	}

	private void buildMaxHeap(int[] data) {
		int mid = data.length >> 1;
		for(int i = mid; i >= 0; i--) {
			maxHeap(data, data.length, i);
		}
	}
	
	private void maxHeap(int[] data, int heapSize, int index) {
		int left = index * 2 + 1;  
        int right = index * 2 + 2;  

        int largest = index;  
        if (left < heapSize && data[left] > data[index]) {  
            largest = left;  
        }  

        if (right < heapSize && data[right] > data[largest]) {  
            largest = right;  
        }  

        if (index != largest) {  
            swap(data, index, largest);  
            maxHeap(data, heapSize, largest);  
        }  
	}
	
	private void swap(int[] data, int index1, int index2) {
		int temp = data[index1];
		data[index1] = data[index2];
		data[index2] = temp;
	}
}
