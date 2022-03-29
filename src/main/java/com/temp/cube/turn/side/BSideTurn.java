package com.temp.cube.turn.side;

import com.temp.cube.enums.Direction;
import com.temp.cube.model.Cube;
import com.temp.cube.model.Side;
import com.temp.cube.turn.SideTurn;
import com.temp.cube.turn.TurnChain;

public class BSideTurn implements SideTurn {

    @Override
    public void turn(Cube cube, Direction direction) {
        if (direction.isClockwise()) {
            TurnChain.make(
                new TurnChain.ChainNode(cube.getUpSide(), Side.LineType.UP, false),
                new TurnChain.ChainNode(cube.getLeftSide(), Side.LineType.LEFT, true),
                new TurnChain.ChainNode(cube.getDownSide(), Side.LineType.DOWN, false),
                new TurnChain.ChainNode(cube.getRightSide(), Side.LineType.RIGHT, true)
            ).process();
        } else {
            TurnChain.make(
                new TurnChain.ChainNode(cube.getUpSide(), Side.LineType.UP, true),
                new TurnChain.ChainNode(cube.getRightSide(), Side.LineType.RIGHT, false),
                new TurnChain.ChainNode(cube.getDownSide(), Side.LineType.DOWN, true),
                new TurnChain.ChainNode(cube.getLeftSide(), Side.LineType.LEFT, false)
            ).process();
        }
        cube.getBackSide().rotate(direction);
    }
}
