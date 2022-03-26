package com.temp.cube.turn.side;

import com.temp.cube.constants.Direction;
import com.temp.cube.model.Cube;
import com.temp.cube.model.Side;
import com.temp.cube.turn.SideTurn;
import com.temp.cube.turn.TurnChain;

public class USideTurn implements SideTurn {

    @Override
    public void turn(Cube cube, Direction direction) {
        if (direction.isClockwise()) {
            TurnChain.make(
                new TurnChain.ChainNode(cube.getFrontSide(), Side.LineType.UP, false),
                new TurnChain.ChainNode(cube.getLeftSide(), Side.LineType.UP, false),
                new TurnChain.ChainNode(cube.getBackSide(), Side.LineType.UP, false),
                new TurnChain.ChainNode(cube.getRightSide(), Side.LineType.UP, false)
            ).process();
        } else {
            TurnChain.make(
                new TurnChain.ChainNode(cube.getFrontSide(), Side.LineType.UP, false),
                new TurnChain.ChainNode(cube.getRightSide(), Side.LineType.UP, false),
                new TurnChain.ChainNode(cube.getBackSide(), Side.LineType.UP, false),
                new TurnChain.ChainNode(cube.getLeftSide(), Side.LineType.UP, false)
            ).process();
        }
        cube.getUpSide().rotate(direction);
    }
}
