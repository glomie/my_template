package com.temp.cube.turn.side;

import com.temp.cube.enums.Direction;
import com.temp.cube.model.Cube;
import com.temp.cube.model.Side;
import com.temp.cube.turn.SideTurn;
import com.temp.cube.turn.TurnChain;

public class DSideTurn implements SideTurn {

    @Override
    public void turn(Cube cube, Direction direction) {
        if (direction.isClockwise()) {
            TurnChain.make(
                new TurnChain.ChainNode(cube.getFrontSide(), Side.LineType.DOWN, false),
                new TurnChain.ChainNode(cube.getRightSide(), Side.LineType.DOWN, false),
                new TurnChain.ChainNode(cube.getBackSide(), Side.LineType.DOWN, false),
                new TurnChain.ChainNode(cube.getLeftSide(), Side.LineType.DOWN, false)
            ).process();
        } else {
            TurnChain.make(
                new TurnChain.ChainNode(cube.getFrontSide(), Side.LineType.DOWN, false),
                new TurnChain.ChainNode(cube.getLeftSide(), Side.LineType.DOWN, false),
                new TurnChain.ChainNode(cube.getBackSide(), Side.LineType.DOWN, false),
                new TurnChain.ChainNode(cube.getRightSide(), Side.LineType.DOWN, false)
            ).process();
        }
        cube.getDownSide().rotate(direction);
    }
}
