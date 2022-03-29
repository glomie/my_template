package com.temp.cube.turn;

import com.temp.cube.enums.Direction;
import com.temp.cube.enums.SideTurnEnum;

public class SideTurnAction {

    private SideTurnEnum sideTurnEnum;

    private Direction direction;

    public SideTurnAction(SideTurnEnum sideTurnEnum, Direction direction) {
        this.sideTurnEnum = sideTurnEnum;
        this.direction = direction;
    }

    public SideTurnEnum getSideTurnEnum() {
        return sideTurnEnum;
    }

    public Direction getDirection() {
        return direction;
    }
}
