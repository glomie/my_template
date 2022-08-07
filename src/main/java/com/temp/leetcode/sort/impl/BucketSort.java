package com.temp.leetcode.sort.impl;

import com.temp.leetcode.sort.ISort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BucketSort implements ISort {

    @Override
    public int[] sort(int[] arr) {
        if (arr.length == 0) {
            return arr;
        }
        int min = arr[0], max = arr[0];
        for (int i = 0; i < arr.length; i++) {
            min = Math.min(min, arr[i]);
            max = Math.max(max, arr[i]);
        }
        List<List<Integer>> buckets = new ArrayList<>(arr.length / 3);
        for (int i = 0; i < arr.length; i++) {
            buckets.add(new ArrayList<>());
        }
        for (int i = 0; i < arr.length; i++) {
            double interval = (double) (max - min) / (double) (arr.length - 1);
            int bucketIdx = (int) ((arr[i] - min) / interval);
            buckets.get(bucketIdx).add(arr[i]);
        }
        for (int i = 0; i < buckets.size(); i++) {
            Collections.sort(buckets.get(i));
        }
        int index = 0;
        for (List<Integer> bucket : buckets) {
            for (int sortedItem : bucket) {
                arr[index++] = sortedItem;
            }
        }
        return arr;
    }
}
