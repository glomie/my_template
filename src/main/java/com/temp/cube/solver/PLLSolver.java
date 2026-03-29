package com.temp.cube.solver;

import com.temp.cube.enums.Direction;
import com.temp.cube.enums.SideTurnEnum;
import com.temp.cube.model.Cube;
import com.temp.cube.turn.SideTurnAction;

import java.util.ArrayList;
import java.util.List;

public class PLLSolver {

    private CubeStateChecker stateChecker;

    public PLLSolver() {
        this.stateChecker = new CubeStateChecker();
    }

    public List<SideTurnAction> solve(Cube cube) {
        List<SideTurnAction> moves = new ArrayList<>();
        
        if (stateChecker.isPLLSolved(cube)) {
            return moves;
        }

        String pattern = getPLLType(cube);
        
        switch (pattern) {
            case "solved":
                break;
            case "adjacent":
                moves.addAll(applyAlgorithm(cube, "R U R' U' R' F R2 U' R' U' R U R' F'"));
                break;
            case "diagonal":
                moves.addAll(applyAlgorithm(cube, "R' F R' F2 R U' R' F2 R2"));
                break;
            case "H":
                moves.addAll(applyAlgorithm(cube, "R2 U2 R U2 R2"));
                break;
            case "Z":
                moves.addAll(applyAlgorithm(cube, "R' F R' F2 R U' R' F2 R2"));
                break;
            default:
                for (int i = 0; i < 4; i++) {
                    if (stateChecker.isPLLSolved(cube)) break;
                    moves.addAll(applyAlgorithm(cube, "U"));
                }
                pattern = getPLLType(cube);
                if (pattern.equals("adjacent")) {
                    moves.addAll(applyAlgorithm(cube, "R U R' U' R' F R2 U' R' U' R U R' F'"));
                } else if (pattern.equals("diagonal")) {
                    moves.addAll(applyAlgorithm(cube, "R' F R' F2 R U' R' F2 R2"));
                }
                break;
        }

        return moves;
    }

    private String getPLLType(Cube cube) {
        if (stateChecker.isPLLSolved(cube)) {
            return "solved";
        }
        
        String[][] up = cube.getUpSide().getOutputArray();
        String[][] front = cube.getFrontSide().getOutputArray();
        
        String up00 = up[0][0];
        String up02 = up[0][2];
        String up20 = up[2][0];
        String up22 = up[2][2];
        
        boolean cornersSolved = (up00.equals(up02)) && (up20.equals(up22));
        
        if (cornersSolved) {
            String f02 = front[0][2];
            String f20 = front[2][2];
            
            if (f02.equals(f20)) {
                return "H";
            }
        }
        
        int sameCorners = 0;
        if (up00.equals(up02)) sameCorners++;
        if (up20.equals(up22)) sameCorners++;
        
        if (sameCorners == 1) {
            return "adjacent";
        }
        
        if (sameCorners == 0) {
            return "diagonal";
        }
        
        return "unknown";
    }

    private List<SideTurnAction> applyAlgorithm(Cube cube, String algorithm) {
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
                    cube.turn(side, dir);
                }
            }
        }
        return moves;
    }

    public boolean isPLLSolved(Cube cube) {
        return stateChecker.isPLLSolved(cube);
    }
}
