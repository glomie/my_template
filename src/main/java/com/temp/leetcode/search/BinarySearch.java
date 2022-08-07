package com.temp.leetcode.search;

public class BinarySearch {

    int search(int[] arr, int target) {
        int l = 0, r = arr.length - 1;
        while (l <= r) {
            int m = (l + r) / 2;
            if (arr[m] < target) {
                l = m + 1;
            } else if (arr[m] > target) {
                r = m - 1;
            } else {
                return m;
            }
        }
        // 返回最靠近m的左边的index
        return l;
    }
}
