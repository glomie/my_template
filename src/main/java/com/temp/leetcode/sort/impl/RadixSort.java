package com.temp.leetcode.sort.impl;

import com.temp.leetcode.sort.ISort;

import java.util.Arrays;

public class RadixSort implements ISort {

    @Override
    public int[] sort(int[] arr) {
        if (arr.length == 0) {
            return arr;
        }
        int max = Math.abs(arr[0]);
        for (int i = 1; i < arr.length; i++) {
            max = Math.max(max, Math.abs(arr[i]));
        }
        int maxDigitLen = 0, base = 10;
        while (max != 0) {
            maxDigitLen++;
            max /= 10;
        }
        int[] sortedArr = new int[arr.length];
        for (int i = 0; i < maxDigitLen; i++) {
            int[] countArr = new int[19];
            for (int j = 0; j < arr.length; j++) {
                int bktIdx = (arr[j] % base) / (base / 10) + 9;
                countArr[bktIdx]++;
            }
            for (int j = 1; j < countArr.length; j++) {
                countArr[j] += countArr[j - 1];
            }
            for (int j = arr.length - 1; j >= 0; j--) {
                int thisBase = (arr[j] % base) / (base / 10)  + 9;
                sortedArr[countArr[thisBase] - 1] = arr[j];
                countArr[thisBase]--;
            }
            arr = Arrays.copyOf(sortedArr, sortedArr.length);
            base *= 10;
        }
        return arr;
    }
}
