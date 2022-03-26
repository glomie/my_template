package com.temp.cube.turn.cube;

import com.temp.cube.constants.Direction;
import com.temp.cube.model.Cube;
import com.temp.cube.model.Side;
import com.temp.cube.turn.CubeTurn;

public class ZCubeTurn implements CubeTurn {

    @Override
    public void turn(Cube cube, Direction direction) {
        if (direction.isClockwise()) {
            Side tempSide = cube.getRightSide();
            cube.setRightSide(cube.getUpSide().horizontalReverseClockwise());
            cube.setUpSide(cube.getLeftSide().horizontalReverseClockwise());
            cube.setLeftSide(cube.getDownSide().horizontalReverseClockwise());
            cube.setDownSide(tempSide.horizontalReverseClockwise());
            cube.getFrontSide().rotate(Direction.CLOCKWISE);
            cube.getBackSide().rotate(Direction.COUNTERCLOCKWISE);
        } else {
            Side tempSide = cube.getLeftSide();
            cube.setLeftSide(cube.getUpSide().horizontalReverseCounterClockwise());
            cube.setUpSide(cube.getRightSide().horizontalReverseCounterClockwise());
            cube.setRightSide(cube.getDownSide().horizontalReverseCounterClockwise());
            cube.setDownSide(tempSide.horizontalReverseCounterClockwise());
            cube.getFrontSide().rotate(Direction.COUNTERCLOCKWISE);
            cube.getBackSide().rotate(Direction.CLOCKWISE);
        }
    }
}
