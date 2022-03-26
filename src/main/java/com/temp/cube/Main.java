package com.temp.cube;

import com.temp.cube.constants.CubeTurnEnum;
import com.temp.cube.constants.Direction;
import com.temp.cube.model.Cube;
import com.temp.cube.parser.ScrambleParser;
import com.temp.cube.turn.SideTurnAction;

import java.util.List;

public class Main {

    private static final String inputScrambleString = "B2 U2 D' R L' F D' L' F2 R2 L2 U' B2 U L2 D' B2 D R2 D2 F'";

    public static void main(String[] args) {
        List<SideTurnAction> turnActions = ScrambleParser.parse(inputScrambleString);
        Cube cube = Cube.init();
        turnActions.forEach(turnAction -> cube.turn(turnAction.getSideTurnEnum(), turnAction.getDirection()));
        cube.turn(CubeTurnEnum.x, Direction.COUNTERCLOCKWISE);
        cube.output();
    }
}
