package com.temp.cube.solver;

import com.temp.cube.enums.Color;
import com.temp.cube.enums.Direction;
import com.temp.cube.enums.SideTurnEnum;
import com.temp.cube.model.Cube;
import com.temp.cube.turn.SideTurnAction;

import java.util.ArrayList;
import java.util.List;

public class CrossSolver {

    private CubeStateChecker stateChecker;

    public CrossSolver() {
        this.stateChecker = new CubeStateChecker();
    }

    public List<SideTurnAction> solve(Cube cube) {
        List<SideTurnAction> moves = new ArrayList<>();
        
        if (stateChecker.isCrossSolved(cube)) {
            return moves;
        }

        String[][] up = cube.getUpSide().getOutputArray();
        String[][] front = cube.getFrontSide().getOutputArray();
        String[][] right = cube.getRightSide().getOutputArray();
        String[][] back = cube.getBackSide().getOutputArray();
        String[][] left = cube.getLeftSide().getOutputArray();
        
        if (!up[0][1].equals("GREEN")) {
            moves.add(new SideTurnAction(SideTurnEnum.F, Direction.CLOCKWISE));
            cube.turn(SideTurnEnum.F, Direction.CLOCKWISE);
        }
        
        if (!up[1][2].equals("RED")) {
            moves.add(new SideTurnAction(SideTurnEnum.R, Direction.CLOCKWISE));
            cube.turn(SideTurnEnum.R, Direction.CLOCKWISE);
        }
        
        if (!up[2][1].equals("BLUE")) {
            moves.add(new SideTurnAction(SideTurnEnum.B, Direction.CLOCKWISE));
            cube.turn(SideTurnEnum.B, Direction.CLOCKWISE);
        }
        
        if (!up[1][0].equals("ORANGE")) {
            moves.add(new SideTurnAction(SideTurnEnum.L, Direction.CLOCKWISE));
            cube.turn(SideTurnEnum.L, Direction.CLOCKWISE);
        }

        if (!stateChecker.isCrossSolved(cube)) {
            for (int i = 0; i < 20; i++) {
                if (stateChecker.isCrossSolved(cube)) break;
                
                String[][] u = cube.getUpSide().getOutputArray();
                
                if (u[0][1].equals("WHITE") && !cube.getFrontSide().getOutputArray()[0][1].equals("GREEN")) {
                    moves.add(new SideTurnAction(SideTurnEnum.F, Direction.CLOCKWISE));
                    moves.add(new SideTurnAction(SideTurnEnum.F, Direction.CLOCKWISE));
                    cube.turn(SideTurnEnum.F, Direction.CLOCKWISE);
                    cube.turn(SideTurnEnum.F, Direction.CLOCKWISE);
                }
                else if (u[1][2].equals("WHITE") && !cube.getRightSide().getOutputArray()[0][1].equals("RED")) {
                    moves.add(new SideTurnAction(SideTurnEnum.R, Direction.CLOCKWISE));
                    moves.add(new SideTurnAction(SideTurnEnum.R, Direction.CLOCKWISE));
                    cube.turn(SideTurnEnum.R, Direction.CLOCKWISE);
                    cube.turn(SideTurnEnum.R, Direction.CLOCKWISE);
                }
                else if (u[2][1].equals("WHITE") && !cube.getBackSide().getOutputArray()[0][1].equals("BLUE")) {
                    moves.add(new SideTurnAction(SideTurnEnum.B, Direction.CLOCKWISE));
                    moves.add(new SideTurnAction(SideTurnEnum.B, Direction.CLOCKWISE));
                    cube.turn(SideTurnEnum.B, Direction.CLOCKWISE);
                    cube.turn(SideTurnEnum.B, Direction.CLOCKWISE);
                }
                else if (u[1][0].equals("WHITE") && !cube.getLeftSide().getOutputArray()[0][1].equals("ORANGE")) {
                    moves.add(new SideTurnAction(SideTurnEnum.L, Direction.CLOCKWISE));
                    moves.add(new SideTurnAction(SideTurnEnum.L, Direction.CLOCKWISE));
                    cube.turn(SideTurnEnum.L, Direction.CLOCKWISE);
                    cube.turn(SideTurnEnum.L, Direction.CLOCKWISE);
                }
                else {
                    moves.add(new SideTurnAction(SideTurnEnum.U, Direction.CLOCKWISE));
                    cube.turn(SideTurnEnum.U, Direction.CLOCKWISE);
                }
            }
        }

        return moves;
    }

    public boolean isCrossSolved(Cube cube) {
        return stateChecker.isCrossSolved(cube);
    }
}
