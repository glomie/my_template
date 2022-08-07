package com.temp.leetcode.sort;

import com.temp.leetcode.sort.impl.BucketSort;

import java.util.Arrays;

public class SortTest {

    private static ISort sorter = new BucketSort();

    public static void main(String[] args) {
        int[] arr1 = {3, 11, 6, 4, 9, 5, 7,8,10};
        int[] sortedArr1 = sorter.sort(arr1);
        System.out.println(Arrays.toString(sortedArr1));
        int[] arr2 = {6674,1560,5884,2977,2922,4127,5390,7870,1193,7163};
        int[] sortedArr2 = sorter.sort(arr2);
        System.out.println(Arrays.toString(sortedArr2));
        int[] arr3 = {0};
        int[] sortedArr3 = sorter.sort(arr3);
        System.out.println(Arrays.toString(sortedArr3));
        int[] arr4 = {2,1,2};
        int[] sortedArr4 = sorter.sort(arr4);
        System.out.println(Arrays.toString(sortedArr4));
        int[] arr5 = {};
        int[] sortedArr5 = sorter.sort(arr5);
        System.out.println(Arrays.toString(sortedArr5));
    }
}
