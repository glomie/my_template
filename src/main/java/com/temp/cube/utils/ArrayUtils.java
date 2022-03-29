package com.temp.cube.utils;

import com.temp.cube.constants.Constants;

import java.util.Arrays;

public class ArrayUtils {

    public static String[][] copy(String[][] array) {
        String[][] destArray = new String[Constants.SIZE][Constants.SIZE];
        for (int i = 0; i < Constants.SIZE; i++) {
            destArray[i] = copy(array[i]);
        }
        return destArray;
    }

    public static String[] copy(String[] array) {
        return Arrays.copyOf(array, Constants.SIZE);
    }

    public static String[] reverse(String[] array) {
        String[] result = new String[Constants.SIZE];
        for (int i = 0; i < Constants.SIZE; i++) {
            int j = Constants.SIZE - 1 - i;
            result[j] = array[i];
        }
        return result;
    }

    public static void swap(String[][] array, String[][] changedArray) {
        for (int i = 0; i < Constants.SIZE; i++) {
            for (int j = 0; j < Constants.SIZE; j++) {
                array[j][i] = changedArray[i][j];
            }
        }
    }
}
