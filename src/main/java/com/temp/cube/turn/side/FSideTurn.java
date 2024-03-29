package com.temp.cube.turn.side;

import com.temp.cube.enums.Direction;
import com.temp.cube.model.Cube;
import com.temp.cube.model.Side;
import com.temp.cube.turn.SideTurn;
import com.temp.cube.turn.TurnChain;

public class FSideTurn implements SideTurn {

    @Override
    public void turn(Cube cube, Direction direction) {
        if (direction.isClockwise()) {
            TurnChain.make(
                new TurnChain.ChainNode(cube.getUpSide(), Side.LineType.DOWN, true),
                new TurnChain.ChainNode(cube.getRightSide(), Side.LineType.LEFT, false),
                new TurnChain.ChainNode(cube.getDownSide(), Side.LineType.UP, true),
                new TurnChain.ChainNode(cube.getLeftSide(), Side.LineType.RIGHT, false)
            ).process();
        } else {
            TurnChain.make(
                new TurnChain.ChainNode(cube.getUpSide(), Side.LineType.DOWN, false),
                new TurnChain.ChainNode(cube.getLeftSide(), Side.LineType.RIGHT, true),
                new TurnChain.ChainNode(cube.getDownSide(), Side.LineType.UP, false),
                new TurnChain.ChainNode(cube.getRightSide(), Side.LineType.LEFT, true)
            ).process();
        }
        cube.getFrontSide().rotate(direction);
    }
}
