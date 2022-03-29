package com.temp.cube;

import com.temp.cube.enums.CubeTurnEnum;
import com.temp.cube.enums.Direction;
import com.temp.cube.model.Cube;
import com.temp.cube.parser.ScrambleParser;
import com.temp.cube.turn.SideTurnAction;

import java.util.List;

public class Main {

    private static final String inputScrambleString = "B U L' U F2 R' U2 B2 L' B' D2 R' D' U L' R2 B F' D' F D' B' D2 U2 R2";

    public static void main(String[] args) {
        List<SideTurnAction> turnActions = ScrambleParser.parse(inputScrambleString);
        Cube cube = Cube.init();
        cube.turn(CubeTurnEnum.x, Direction.CLOCKWISE);
        turnActions.forEach(turnAction -> cube.turn(turnAction.getSideTurnEnum(), turnAction.getDirection()));
        cube.output();
    }
}
