package com.temp.cube.output;

import com.temp.cube.constants.Constants;
import com.temp.cube.model.Cube;
import com.temp.cube.model.Side;

public class ConsolePrint {

    public static void output(Cube cube) {
        System.out.println("frontSide:");
        print(cube.getFrontSide());
        System.out.println("rightSide:");
        print(cube.getRightSide());
        System.out.println("backSide:");
        print(cube.getBackSide());
        System.out.println("leftSide:");
        print(cube.getLeftSide());
        System.out.println("upSide:");
        print(cube.getUpSide());
        System.out.println("downSide:");
        print(cube.getDownSide());
    }

    private static void print(Side side) {
        String[][] horizontalArray = side.getOutputArray();
        for (int i = 0; i < Constants.SIZE; i++) {
            for (int j = 0; j < Constants.SIZE; j++) {
                System.out.printf(horizontalArray[i][j] + " ");
            }
            System.out.println();
        }
    }
}
