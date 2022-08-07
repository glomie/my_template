package com.temp.leetcode.sort.impl;

import com.temp.leetcode.sort.ISort;

public class InsertSort implements ISort {

    @Override
    public int[] sort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] >= arr[i - 1]) {
                continue;
            }
            int target = arr[i];
            int l = 0, r = i - 1;
            while (l <= r) {
                int m = (l + r) / 2;
                if (arr[m] <= target) {
                    l = m + 1;
                } else {
                    r = m - 1;
                }
            }
            for (int j = i; j > l; j--) {
                arr[j] = arr[j - 1];
            }
            arr[l] = target;
        }
        return arr;
    }
}
