package com.temp.cube.solver;

import com.temp.cube.enums.Color;
import com.temp.cube.enums.Direction;
import com.temp.cube.enums.SideTurnEnum;
import com.temp.cube.model.Cube;
import com.temp.cube.turn.SideTurnAction;

import java.util.ArrayList;
import java.util.List;

public class OLLSolver {

    private CubeStateChecker stateChecker;

    public OLLSolver() {
        this.stateChecker = new CubeStateChecker();
    }

    public List<SideTurnAction> solve(Cube cube) {
        List<SideTurnAction> moves = new ArrayList<>();
        
        if (stateChecker.isOLLSolved(cube)) {
            return moves;
        }

        int edgesOriented = countOrientedEdges(cube);
        
        if (edgesOriented == 4) {
            moves.addAll(solveCorners(cube));
        } else {
            moves.addAll(solveEdges(cube));
            if (!stateChecker.isOLLSolved(cube)) {
                moves.addAll(solveCorners(cube));
            }
        }

        return moves;
    }

    private int countOrientedEdges(Cube cube) {
        int count = 0;
        String[][] up = cube.getUpSide().getOutputArray();
        
        if (!up[0][1].equals("WHITE") && !up[2][1].equals("WHITE") &&
            !up[1][0].equals("WHITE") && !up[1][2].equals("WHITE")) {
            return 0;
        }
        
        if (up[0][1].equals("WHITE")) count++;
        if (up[2][1].equals("WHITE")) count++;
        if (up[1][0].equals("WHITE")) count++;
        if (up[1][2].equals("WHITE")) count++;
        
        return count;
    }

    private List<SideTurnAction> solveEdges(Cube cube) {
        List<SideTurnAction> moves = new ArrayList<>();
        
        int oriented = countOrientedEdges(cube);
        
        if (oriented == 0) {
            moves.addAll(applyAlgorithm(cube, "F R U R' U' F'"));
        } else if (oriented == 1) {
            moves.addAll(applyAlgorithm(cube, "F R U R' U' F'"));
        } else if (oriented == 2) {
            moves.addAll(applyAlgorithm(cube, "F R U R' U' F'"));
        }
        
        return moves;
    }

    private List<SideTurnAction> solveCorners(Cube cube) {
        List<SideTurnAction> moves = new ArrayList<>();
        
        String[][] up = cube.getUpSide().getOutputArray();
        int whiteCount = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (up[i][j].equals("WHITE")) whiteCount++;
            }
        }
        
        if (whiteCount == 9) {
            return moves;
        }
        
        if (whiteCount == 1) {
            moves.addAll(applyAlgorithm(cube, "R U R' U R U2 R'"));
        } else if (whiteCount == 2) {
            moves.addAll(applyAlgorithm(cube, "R U2 R' U' R U' R'"));
        } else if (whiteCount == 3) {
            moves.addAll(applyAlgorithm(cube, "R U R' U' R' F R2 U R' U' F'"));
        } else {
            for (int i = 0; i < 4; i++) {
                if (stateChecker.isOLLSolved(cube)) break;
                moves.addAll(applyAlgorithm(cube, "R U R' U R U2 R'"));
            }
        }
        
        return moves;
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

    public boolean isOLLSolved(Cube cube) {
        return stateChecker.isOLLSolved(cube);
    }
}
