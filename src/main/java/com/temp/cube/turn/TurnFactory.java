package com.temp.cube.turn;

import com.temp.cube.enums.CubeTurnEnum;
import com.temp.cube.enums.SideTurnEnum;
import com.temp.cube.turn.cube.XCubeTurn;
import com.temp.cube.turn.cube.YCubeTurn;
import com.temp.cube.turn.cube.ZCubeTurn;
import com.temp.cube.turn.side.*;

public class TurnFactory {

    private static SideTurn RSideTurn = new RSideTurn();
    private static SideTurn LSideTurn = new LSideTurn();
    private static SideTurn USideTurn = new USideTurn();
    private static SideTurn DSideTurn = new DSideTurn();
    private static SideTurn FSideTurn = new FSideTurn();
    private static SideTurn BSideTurn = new BSideTurn();
    private static CubeTurn XCubeTurn = new XCubeTurn();
    private static CubeTurn YCubeTurn = new YCubeTurn();
    private static CubeTurn ZCubeTurn = new ZCubeTurn();

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

    public static Turn getTurn(CubeTurnEnum cubeTurnEnum) {
        switch (cubeTurnEnum) {
            case x:
                return XCubeTurn;
            case y:
                return YCubeTurn;
            case z:
                return ZCubeTurn;
            default:
                throw new IllegalArgumentException("cubeTurnEnum is required");
        }
    }
}
