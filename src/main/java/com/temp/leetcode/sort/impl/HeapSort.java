package com.temp.leetcode.sort.impl;

import com.temp.leetcode.sort.ISort;

public class HeapSort implements ISort {

    @Override
    public int[] sort(int[] arr) {
        heapify(arr);
        for (int i = arr.length - 1; i > 0; i--) {
            swap(arr, 0, i);
            siftDown(arr, 0, i - 1);
        }
        return arr;
    }

    private void heapify(int[] arr) {
        for (int hole = (arr.length - 2) / 2; hole >= 0; hole--) {
            siftDown(arr, hole, arr.length - 1);
        }
    }

    private void siftDown(int[] arr, int hole, int endIdx) {
        int target = arr[hole];
        int child = hole * 2 + 1;
        while (child <= endIdx) {
            if (child < endIdx && arr[child + 1] > arr[child]) {
                child = child + 1;
            }
            if (arr[child] > target) {
                arr[hole] = arr[child];
                hole = child;
                child = hole * 2 + 1;
            } else break;
        }
        arr[hole] = target;
    }
}
