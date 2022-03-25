package com.temp.cube.turn;

import com.temp.cube.constants.SideTurnEnum;
import com.temp.cube.turn.side.*;

public class TurnFactory {

    private static SideTurn RSideTurn = new RSideTurn();
    private static SideTurn LSideTurn = new LSideTurn();
    private static SideTurn USideTurn = new USideTurn();
    private static SideTurn DSideTurn = new DSideTurn();
    private static SideTurn FSideTurn = new FSideTurn();
    private static SideTurn BSideTurn = new BSideTurn();

    public static Turn getTurn(SideTurnEnum sideTurnEnum) {
        switch (sideTurnEnum) {
            case R:
                return RSideTurn;
            case L:
                return LSideTurn;
            case U:
                return USideTurn;
            case D:
                return DSideTurn;
            case F:
                return FSideTurn;
            case B:
                return BSideTurn;
            default:
                throw new IllegalArgumentException("sideTurnEnum is required");
        }
    }
}
