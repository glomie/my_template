package com.temp.leetcode.sort.impl;

import com.temp.leetcode.sort.ISort;

public class CountSort implements ISort {

    @Override
    public int[] sort(int[] arr) {
        if (arr.length < 2) return arr;
        int n = arr.length, min = arr[0], max = arr[0];
        for (int i = 1; i < n; i++) {
            min = Math.min(min, arr[i]);
            max = Math.max(max, arr[i]);
        }
        int[] countArr = new int[max - min + 1]; // arr最多有max-min+1种数字
        for (int i = 0; i < n; i++) {
            countArr[arr[i] - min]++; // arr[i]的值出现一次，则countArr[arr[i]-min]加1
        }
        for (int i = 1; i < countArr.length; i++) { // 变形
            countArr[i] += countArr[i - 1];
        }
        int[] sortedArr = new int[n]; // 根据sortedArr, nums, countArr三者关系完成sortedArr的输出
        for (int i = n - 1; i >= 0; i--) {
            sortedArr[countArr[arr[i] - min] - 1] = arr[i];
            countArr[arr[i] - min]--;
        }
        return sortedArr;
    }
}
