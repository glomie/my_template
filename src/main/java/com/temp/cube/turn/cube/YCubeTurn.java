package com.temp.cube.turn.cube;

import com.temp.cube.enums.Direction;
import com.temp.cube.model.Cube;
import com.temp.cube.model.Side;
import com.temp.cube.turn.CubeTurn;

public class YCubeTurn implements CubeTurn {

    @Override
    public void turn(Cube cube, Direction direction) {
        if (direction.isClockwise()) {
            Side tempSide = cube.getLeftSide();
            cube.setLeftSide(cube.getFrontSide());
            cube.setFrontSide(cube.getRightSide());
            cube.setRightSide(cube.getBackSide());
            cube.setBackSide(tempSide);
            cube.getUpSide().rotate(Direction.CLOCKWISE);
            cube.getDownSide().rotate(Direction.COUNTERCLOCKWISE);
        } else {
            Side tempSide = cube.getRightSide();
            cube.setRightSide(cube.getFrontSide());
            cube.setFrontSide(cube.getLeftSide());
            cube.setLeftSide(cube.getBackSide());
            cube.setBackSide(tempSide);
            cube.getUpSide().rotate(Direction.COUNTERCLOCKWISE);
            cube.getDownSide().rotate(Direction.CLOCKWISE);
        }
    }
}
