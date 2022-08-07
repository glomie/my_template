package com.temp.leetcode.sort.impl;

import com.temp.leetcode.sort.ISort;

public class ShellSort implements ISort {

    @Override
    public int[] sort(int[] arr) {
        int n = arr.length;
        for (int gap = n / 2; gap > 0; gap /= 2) { // gap 初始为 n/2，缩小gap直到1
            for(int start = 0; start < gap; start++) { // 步长增量是gap，当前增量下需要对gap组序列进行简单插入排序
                for (int i = start + gap; i < n; i += gap) { // 此for及下一个for对当前增量序列执行简单插入排序
                    int target = arr[i], j = i - gap;
                    for (; j >= 0; j -= gap) {
                        if (target < arr[j]) {
                            arr[j + gap] = arr[j];
                        } else break;
                    }
                    if (j != i - gap) arr[j + gap] = target;
                }
            }
        }
        return arr;
    }

}
