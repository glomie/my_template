package com.temp.cube.turn;

import com.temp.cube.constants.Direction;
import com.temp.cube.constants.SideTurnEnum;

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
