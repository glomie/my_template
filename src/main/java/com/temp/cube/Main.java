package com.temp.cube;

import com.temp.cube.constants.Color;
import com.temp.cube.model.Cube;
import com.temp.cube.model.Side;
import com.temp.cube.scramble.parser.ScrambleParser;
import com.temp.cube.turn.SideTurnAction;

import java.util.List;

public class Main {

    private static final String scramble_str = "B2 U2 D' R L' F D' L' F2 R2 L2 U' B2 U L2 D' B2 D R2 D2 F'";

    public static void main(String[] args) {
        List<SideTurnAction> turnActions = ScrambleParser.parse(scramble_str);
        Side frontSide = Side.initWithOneColor(Color.RED);
        Side rightSide = Side.initWithOneColor(Color.GREEN);
        Side backSide = Side.initWithOneColor(Color.ORANGE);
        Side leftSide = Side.initWithOneColor(Color.BLUE);
        Side downSide = Side.initWithOneColor(Color.WHITE);
        Side upSide = Side.initWithOneColor(Color.YELLOW);
        Cube cube = Cube.init(frontSide, rightSide, backSide, leftSide, downSide, upSide);
        turnActions.forEach(sideTurnAction -> cube.turn(sideTurnAction.getSideTurnEnum(), sideTurnAction.getDirection()));
        cube.output();
    }
}
