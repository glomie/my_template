package com.temp.leetcode.sort;

public interface ISort {

    int[] sort(int[] arr);

    default void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
