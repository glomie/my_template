package com.temp.cube.model;

import com.temp.cube.enums.Color;
import com.temp.cube.constants.Constants;
import com.temp.cube.enums.Direction;
import com.temp.cube.utils.ArrayUtils;

/**
 * 魔方的一个面
 */
public class Side {

    /**
     * 横着记录这一面
     */
    private String[][] horizontalArray = new String[Constants.SIZE][Constants.SIZE];

    /**
     * 竖着记录这一面
     */
    private String[][] verticalArray = new String[Constants.SIZE][Constants.SIZE];

    public static Side initWithOneColor(Color color) {
        Side side = new Side();
        for (int i = 0; i < Constants.SIZE; i++) {
            for (int j = 0; j < Constants.SIZE; j++) {
                side.horizontalArray[i][j] = color.name();
            }
        }
        side.sync(ArrayType.horizontal);
        return side;
    }

    public String[][] getOutputArray() {
        return ArrayUtils.copy(horizontalArray);
    }

    public String[] getLine(LineType lineType) {
        switch (lineType) {
            case LEFT:
                return verticalArray[0];
            case RIGHT:
                return verticalArray[Constants.SIZE - 1];
            case UP:
                return horizontalArray[0];
            case DOWN:
                return horizontalArray[Constants.SIZE - 1];
            default:
                throw new IllegalArgumentException("lineType is required");
        }
    }
    
    public Side verticalReverse() {
        String[][] newHorizontalArray = new String[Constants.SIZE][Constants.SIZE];
        for (int i = 0; i < Constants.SIZE; i++) {
            for (int j = 0; j < Constants.SIZE; j++) {
                newHorizontalArray[Constants.SIZE - 1 - i][Constants.SIZE - 1 - j] = horizontalArray[i][j];
            }
        }
        horizontalArray = newHorizontalArray;
        ArrayUtils.swap(verticalArray, horizontalArray);
        return this;
    }

    public Side horizontalReverseClockwise() {
        String[][] newHorizontalArray = new String[Constants.SIZE][Constants.SIZE];
        for (int i = 0; i < Constants.SIZE; i++) {
            for (int j = 0; j < Constants.SIZE; j++) {
                newHorizontalArray[j][Constants.SIZE - 1 - i] = horizontalArray[i][j];
            }
        }
        horizontalArray = newHorizontalArray;
        ArrayUtils.swap(verticalArray, horizontalArray);
        return this;
    }

    public Side horizontalReverseCounterClockwise() {
        String[][] newHorizontalArray = new String[Constants.SIZE][Constants.SIZE];
        for (int i = 0; i < Constants.SIZE; i++) {
            for (int j = 0; j < Constants.SIZE; j++) {
                newHorizontalArray[Constants.SIZE - 1 - j][i] = horizontalArray[i][j];
            }
        }
        horizontalArray = newHorizontalArray;
        ArrayUtils.swap(verticalArray, horizontalArray);
        return this;
    }

    public String[] changeAndGet(LineType lineType, String[] array) {
        String[] beChangedArray;
        switch (lineType) {
            case LEFT:
            case RIGHT:
                beChangedArray = verticalArray[lineType.getNum()];
                verticalArray[lineType.getNum()] = ArrayUtils.copy(array);
                sync(ArrayType.vertical);
                break;
            case UP:
            case DOWN:
                beChangedArray = horizontalArray[lineType.getNum()];
                horizontalArray[lineType.getNum()] = ArrayUtils.copy(array);
                sync(ArrayType.horizontal);
            break;
            default:
                throw new IllegalArgumentException("arrayType is required");
        }
        return beChangedArray;
    }

    public void rotate(Direction direction) {
        switch (direction) {
            case CLOCKWISE:
                for (int i = 0; i < Constants.SIZE; i++) {
                    int j = Constants.SIZE - 1 - i;
                    verticalArray[j] = ArrayUtils.copy(horizontalArray[i]);
                }
                break;
            case COUNTERCLOCKWISE:
                for (int i = 0; i < Constants.SIZE; i++) {
                    verticalArray[i] = ArrayUtils.reverse(horizontalArray[i]);
                }
                break;
            default:
                throw new IllegalArgumentException("direction is required");
        }
        sync(ArrayType.vertical);
    }


    private void sync(ArrayType changedArrayType) {
        switch (changedArrayType) {
            case horizontal:
                ArrayUtils.swap(verticalArray, horizontalArray);
                break;
            case vertical:
                ArrayUtils.swap(horizontalArray, verticalArray);
                break;
            default:
                throw new IllegalArgumentException("arrayType is required");
        }
    }

    public enum ArrayType {
        horizontal, vertical
    }

    public enum LineType {
        LEFT(0), RIGHT(Constants.SIZE - 1), UP(0), DOWN(Constants.SIZE - 1);

        private int num;

        LineType(int num) {
            this.num = num;
        }

        public int getNum() {
            return num;
        }
    }
}
