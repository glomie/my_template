package com.temp.cube.constants;

import java.util.Objects;

public enum Direction {
    /**
     * 顺时针
     */
    CLOCKWISE,
    /**
     * 逆时针
     */
    COUNTERCLOCKWISE;

    public static boolean isClockwise(Direction direction) {
        return Objects.equals(direction, CLOCKWISE);
    }


}
