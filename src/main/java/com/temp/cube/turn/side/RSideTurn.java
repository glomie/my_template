package com.temp.cube.turn.side;

import com.temp.cube.constants.Direction;
import com.temp.cube.model.Cube;
import com.temp.cube.model.Side;
import com.temp.cube.turn.SideTurn;
import com.temp.cube.turn.TurnChain;

public class RSideTurn implements SideTurn {

    @Override
    public void turn(Cube cube, Direction direction) {
        if (direction.isClockwise()) {
            TurnChain.make(
                new TurnChain.ChainNode(cube.getFrontSide(), Side.LineType.RIGHT, false),
                new TurnChain.ChainNode(cube.getUpSide(), Side.LineType.RIGHT, false),
                new TurnChain.ChainNode(cube.getBackSide(), Side.LineType.LEFT, true),
                new TurnChain.ChainNode(cube.getDownSide(), Side.LineType.RIGHT, true)
            ).process();
        } else {
            TurnChain.make(
                new TurnChain.ChainNode(cube.getFrontSide(), Side.LineType.RIGHT, false),
                new TurnChain.ChainNode(cube.getDownSide(), Side.LineType.RIGHT, false),
                new TurnChain.ChainNode(cube.getBackSide(), Side.LineType.LEFT, true),
                new TurnChain.ChainNode(cube.getUpSide(), Side.LineType.RIGHT, true)
            ).process();
        }
        cube.getRightSide().rotate(direction);
    }
}
