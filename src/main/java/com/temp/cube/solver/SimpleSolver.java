package com.temp.cube.solver;

import com.temp.cube.enums.Direction;
import com.temp.cube.enums.SideTurnEnum;
import com.temp.cube.model.Cube;
import com.temp.cube.turn.SideTurnAction;

import java.util.ArrayList;
import java.util.List;

public class SimpleSolver {

    private static final String[] BASIC_MOVES = {
        "R", "R'", "R2",
        "L", "L'", "L2", 
        "U", "U'", "U2",
        "D", "D'", "D2",
        "F", "F'", "F2",
        "B", "B'", "B2"
    };

    public static List<SideTurnAction> solve(Cube cube) {
        List<SideTurnAction> moves = new ArrayList<>();
        
        int maxIterations = 2000;
        
        for (int iteration = 0; iteration < maxIterations; iteration++) {
            if (isSolved(cube)) break;
            
            int moveIdx = iteration % BASIC_MOVES.length;
            String move = BASIC_MOVES[moveIdx];
            
            doMove(cube, move);
            moves.add(parseMove(move));
        }
        
        return moves;
    }

    private static boolean isSolved(Cube cube) {
        return isSideSolved(cube.getUpSide(), "WHITE") &&
               isSideSolved(cube.getDownSide(), "YELLOW") &&
               isSideSolved(cube.getFrontSide(), "GREEN") &&
               isSideSolved(cube.getBackSide(), "BLUE") &&
               isSideSolved(cube.getRightSide(), "RED") &&
               isSideSolved(cube.getLeftSide(), "ORANGE");
    }

    private static boolean isSideSolved(com.temp.cube.model.Side side, String color) {
        String[][] arr = side.getOutputArray();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!arr[i][j].equals(color)) return false;
            }
        }
        return true;
    }

    private static void doMove(Cube cube, String move) {
        char base = move.charAt(0);
        Direction dir = move.contains("'") ? Direction.COUNTERCLOCKWISE : Direction.CLOCKWISE;
        int count = move.contains("2") ? 2 : 1;

        SideTurnEnum side = null;
        if (base == 'R') side = SideTurnEnum.R;
        else if (base == 'L') side = SideTurnEnum.L;
        else if (base == 'U') side = SideTurnEnum.U;
        else if (base == 'D') side = SideTurnEnum.D;
        else if (base == 'F') side = SideTurnEnum.F;
        else if (base == 'B') side = SideTurnEnum.B;

        for (int i = 0; i < count; i++) {
            cube.turn(side, dir);
        }
    }

    private static SideTurnAction parseMove(String move) {
        char base = move.charAt(0);
        Direction dir = move.contains("'") ? Direction.COUNTERCLOCKWISE : Direction.CLOCKWISE;
        int count = move.contains("2") ? 2 : 1;

        SideTurnEnum side = null;
        if (base == 'R') side = SideTurnEnum.R;
        else if (base == 'L') side = SideTurnEnum.L;
        else if (base == 'U') side = SideTurnEnum.U;
        else if (base == 'D') side = SideTurnEnum.D;
        else if (base == 'F') side = SideTurnEnum.F;
        else if (base == 'B') side = SideTurnEnum.B;

        return new SideTurnAction(side, dir);
    }
}
