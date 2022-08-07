package com.temp.leetcode.sort.impl;

import com.temp.leetcode.sort.ISort;

public class BubbleSort implements ISort {

    @Override
    public int[] sort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            boolean swapped = false;
            for (int j = 1; j < arr.length - i; j++) {
                if (arr[j - 1] > arr[j]) {
                    swap(arr, j - 1, j);
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
        return arr;
    }
}
