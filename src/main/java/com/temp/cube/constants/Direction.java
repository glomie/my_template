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

    public boolean isClockwise() {
        return Objects.equals(this, CLOCKWISE);
    }


}
