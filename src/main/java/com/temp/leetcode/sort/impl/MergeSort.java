package com.temp.leetcode.sort.impl;

import com.temp.leetcode.sort.ISort;

public class MergeSort implements ISort {

    @Override
    public int[] sort(int[] arr) {
        int[] tmpArr = new int[arr.length];
        mergeSort(arr, tmpArr, 0, arr.length - 1);
        return arr;
    }

    private void mergeSort(int[] arr, int[] tmpArr, int left, int right) {
        if(left < right) {
            int center = (left + right) / 2;
            mergeSort(arr, tmpArr, left, center);
            mergeSort(arr, tmpArr, center + 1, right);
            merge(arr, tmpArr, left, center, right);
        }
    }

    // 非原地合并方法
    private void merge(int[] arr, int[] tmpArr, int leftPos, int leftEnd, int rightEnd) {
        int rightPos = leftEnd + 1;
        int startIdx = leftPos;
        int tmpPos = leftPos;
        while (leftPos <= leftEnd && rightPos <= rightEnd) {
            if (arr[leftPos] <= arr[rightPos]) {
                tmpArr[tmpPos++] = arr[leftPos++];
            }
            else {
                tmpArr[tmpPos++] = arr[rightPos++];
            }
        }
        // 比较完成后若左数组还有剩余，则将其添加到tmpArr剩余空间
        while (leftPos <= leftEnd) {
            tmpArr[tmpPos++] = arr[leftPos++];
        }
        // 比较完成后若右数组还有剩余，则将其添加到tmpArr剩余空间
        while (rightPos <= rightEnd) {
            tmpArr[tmpPos++] = arr[rightPos++];
        }
        // 容易遗漏的步骤，将tmpArr拷回arr中
        // 从小区间排序到大区间排序，大区间包含原来的小区间，需要从arr再对应比较排序到tmpArr中，
        // 所以arr也需要动态更新为排序状态，即随时将tmpArr拷回到arr中
        for(int i = startIdx; i <= rightEnd; i++) {
            arr[i] = tmpArr[i];
        }
    }
}
