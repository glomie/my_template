package com.temp.cube.solver;

import com.temp.cube.enums.Direction;
import com.temp.cube.enums.SideTurnEnum;
import com.temp.cube.model.Cube;
import com.temp.cube.turn.SideTurnAction;

import java.util.ArrayList;
import java.util.List;

public class F2LSolver {

    private CubeStateChecker stateChecker;

    public F2LSolver() {
        this.stateChecker = new CubeStateChecker();
    }

    public List<SideTurnAction> solve(Cube cube) {
        List<SideTurnAction> moves = new ArrayList<>();
        
        if (stateChecker.isF2LSolved(cube)) {
            return moves;
        }

        for (int i = 0; i < 50; i++) {
            if (stateChecker.isF2LSolved(cube)) {
                break;
            }
            
            int scoreBefore = evaluateF2L(cube);
            
            List<SideTurnAction> bestMoves = null;
            String[] algorithms = {
                "R U R'",
                "R U' R'",
                "R' U' R",
                "R' U R",
                "U R U' R'",
                "U' R' U R"
            };
            
            for (String alg : algorithms) {
                List<SideTurnAction> testMoves = parseAlgorithm(alg);
                int score = applyAndEvaluate(cube, testMoves);
                
                if (score > scoreBefore) {
                    bestMoves = testMoves;
                    scoreBefore = score;
                    break;
                }
            }
            
            if (bestMoves != null) {
                moves.addAll(bestMoves);
            } else {
                moves.add(new SideTurnAction(SideTurnEnum.U, Direction.CLOCKWISE));
                cube.turn(SideTurnEnum.U, Direction.CLOCKWISE);
            }
        }

        return moves;
    }

    private int evaluateF2L(Cube cube) {
        int score = 0;
        
        String[][] front = cube.getFrontSide().getOutputArray();
        String[][] right = cube.getRightSide().getOutputArray();
        String[][] back = cube.getBackSide().getOutputArray();
        String[][] left = cube.getLeftSide().getOutputArray();
        
        score += countSolved(front, "GREEN") * 10;
        score += countSolved(right, "RED") * 10;
        score += countSolved(back, "BLUE") * 10;
        score += countSolved(left, "ORANGE") * 10;
        
        String[][] up = cube.getUpSide().getOutputArray();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!up[i][j].equals("WHITE")) {
                    score++;
                }
            }
        }
        
        return score;
    }

    private int countSolved(String[][] arr, String color) {
        int count = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (arr[i][j].equals(color)) {
                    count++;
                }
            }
        }
        return count;
    }

    private int applyAndEvaluate(Cube cube, List<SideTurnAction> moves) {
        for (SideTurnAction m : moves) {
            cube.turn(m.getSideTurnEnum(), m.getDirection());
        }
        
        int score = evaluateF2L(cube);
        
        for (int i = moves.size() - 1; i >= 0; i--) {
            SideTurnAction m = moves.get(i);
            Direction inverse = m.getDirection() == Direction.CLOCKWISE ? 
                Direction.COUNTERCLOCKWISE : Direction.CLOCKWISE;
            cube.turn(m.getSideTurnEnum(), inverse);
        }
        
        return score;
    }

    private List<SideTurnAction> parseAlgorithm(String algorithm) {
        List<SideTurnAction> moves = new ArrayList<>();
        String[] parts = algorithm.split(" ");
        
        for (String part : parts) {
            if (part.isEmpty()) continue;
            
            char base = part.charAt(0);
            Direction dir = Direction.CLOCKWISE;
            if (part.contains("'")) dir = Direction.COUNTERCLOCKWISE;
            int count = part.contains("2") ? 2 : 1;
            
            SideTurnEnum side = null;
            if (base == 'R') side = SideTurnEnum.R;
            else if (base == 'L') side = SideTurnEnum.L;
            else if (base == 'U') side = SideTurnEnum.U;
            else if (base == 'D') side = SideTurnEnum.D;
            else if (base == 'F') side = SideTurnEnum.F;
            else if (base == 'B') side = SideTurnEnum.B;
            
            if (side != null) {
                for (int i = 0; i < count; i++) {
                    moves.add(new SideTurnAction(side, dir));
                }
            }
        }
        return moves;
    }

    public boolean isF2LSolved(Cube cube) {
        return stateChecker.isF2LSolved(cube);
    }
}
