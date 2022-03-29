package com.temp.cube.turn.cube;

import com.temp.cube.enums.Direction;
import com.temp.cube.model.Cube;
import com.temp.cube.model.Side;
import com.temp.cube.turn.CubeTurn;

public class XCubeTurn implements CubeTurn {

    @Override
    public void turn(Cube cube, Direction direction) {
        if (direction.isClockwise()) {
            Side tempSide = cube.getUpSide();
            cube.setUpSide(cube.getFrontSide());
            cube.setFrontSide(cube.getDownSide());
            cube.setDownSide(cube.getBackSide().verticalReverse());
            cube.setBackSide(tempSide.verticalReverse());
            cube.getRightSide().rotate(Direction.CLOCKWISE);
            cube.getLeftSide().rotate(Direction.COUNTERCLOCKWISE);
        } else {
            Side tempSide = cube.getDownSide();
            cube.setDownSide(cube.getFrontSide());
            cube.setFrontSide(cube.getUpSide());
            cube.setUpSide(cube.getBackSide().verticalReverse());
            cube.setBackSide(tempSide.verticalReverse());
            cube.getRightSide().rotate(Direction.COUNTERCLOCKWISE);
            cube.getLeftSide().rotate(Direction.CLOCKWISE);
        }
    }
}
