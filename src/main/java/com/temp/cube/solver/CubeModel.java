package com.temp.cube.solver;

import com.temp.cube.enums.Color;
import com.temp.cube.enums.Direction;
import com.temp.cube.enums.SideTurnEnum;
import com.temp.cube.model.Cube;
import com.temp.cube.model.Side;
import com.temp.cube.turn.SideTurnAction;

import java.util.ArrayList;
import java.util.List;

public class CubeModel {
    private Cube cube;
    private CubeStateChecker stateChecker;

    private String[] edgePositions;
    private String[] cornerPositions;
    private int[] edgeOrientation;
    private int[] cornerOrientation;

    public CubeModel(Cube cube) {
        this.cube = cube;
        this.stateChecker = new CubeStateChecker();
        initializePieces();
    }

    public Cube getCube() {
        return cube;
    }

    private void initializePieces() {
        edgePositions = new String[12];
        cornerPositions = new String[8];
        edgeOrientation = new int[12];
        cornerOrientation = new int[8];

        for (int i = 0; i < 12; i++) {
            edgeOrientation[i] = 0;
        }
        for (int i = 0; i < 8; i++) {
            cornerOrientation[i] = 0;
        }

        scanEdges();
        scanCorners();
    }

    public void scanEdges() {
        String[][] up = cube.getUpSide().getOutputArray();
        String[][] down = cube.getDownSide().getOutputArray();
        String[][] front = cube.getFrontSide().getOutputArray();
        String[][] back = cube.getBackSide().getOutputArray();
        String[][] right = cube.getRightSide().getOutputArray();
        String[][] left = cube.getLeftSide().getOutputArray();

        edgePositions[0] = getEdgeKey(up[0][1], front[0][1]);
        edgePositions[1] = getEdgeKey(up[1][2], right[0][1]);
        edgePositions[2] = getEdgeKey(up[2][1], back[0][1]);
        edgePositions[3] = getEdgeKey(up[1][0], left[0][1]);

        edgePositions[4] = getEdgeKey(down[0][1], front[2][1]);
        edgePositions[5] = getEdgeKey(down[1][2], right[2][1]);
        edgePositions[6] = getEdgeKey(down[2][1], back[2][1]);
        edgePositions[7] = getEdgeKey(down[1][0], left[2][1]);

        edgePositions[8] = getEdgeKey(front[0][1], right[0][1]);
        edgePositions[9] = getEdgeKey(right[0][1], back[0][1]);
        edgePositions[10] = getEdgeKey(back[0][1], left[0][1]);
        edgePositions[11] = getEdgeKey(left[0][1], front[0][1]);
    }

    public void scanCorners() {
        String[][] up = cube.getUpSide().getOutputArray();
        String[][] down = cube.getDownSide().getOutputArray();
        String[][] front = cube.getFrontSide().getOutputArray();
        String[][] back = cube.getBackSide().getOutputArray();
        String[][] right = cube.getRightSide().getOutputArray();
        String[][] left = cube.getLeftSide().getOutputArray();

        cornerPositions[0] = getCornerKey(up[0][0], left[0][2], front[0][0]);
        cornerPositions[1] = getCornerKey(up[0][2], front[0][2], right[0][0]);
        cornerPositions[2] = getCornerKey(up[2][2], right[0][2], back[0][0]);
        cornerPositions[3] = getCornerKey(up[2][0], back[0][2], left[0][0]);

        cornerPositions[4] = getCornerKey(down[0][0], front[2][2], right[2][0]);
        cornerPositions[5] = getCornerKey(down[0][2], right[2][2], back[2][0]);
        cornerPositions[6] = getCornerKey(down[2][2], back[2][2], left[2][0]);
        cornerPositions[7] = getCornerKey(down[2][0], left[2][2], front[2][0]);
    }

    private String getEdgeKey(String c1, String c2) {
        if (c1.compareTo(c2) < 0) {
            return c1 + "-" + c2;
        }
        return c2 + "-" + c1;
    }

    private String getCornerKey(String c1, String c2, String c3) {
        String[] arr = {c1, c2, c3};
        java.util.Arrays.sort(arr);
        return arr[0] + "-" + arr[1] + "-" + arr[2];
    }

    public boolean isCrossSolved() {
        return stateChecker.isCrossSolved(cube);
    }

    public boolean isF2LSolved() {
        return stateChecker.isF2LSolved(cube);
    }

    public boolean isOLLSolved() {
        return stateChecker.isOLLSolved(cube);
    }

    public boolean isPLLSolved() {
        return stateChecker.isPLLSolved(cube);
    }

    public boolean isSolved() {
        return stateChecker.isSolved(cube);
    }

    public void applyMove(SideTurnAction move) {
        cube.turn(move.getSideTurnEnum(), move.getDirection());
    }

    public void applyMoves(List<SideTurnAction> moves) {
        for (SideTurnAction move : moves) {
            applyMove(move);
        }
    }

    public CubeModel clone() {
        Side front = new Side();
        Side right = new Side();
        Side back = new Side();
        Side left = new Side();
        Side down = new Side();
        Side up = new Side();
        
        setSideFromArray(front, cube.getFrontSide().getOutputArray());
        setSideFromArray(right, cube.getRightSide().getOutputArray());
        setSideFromArray(back, cube.getBackSide().getOutputArray());
        setSideFromArray(left, cube.getLeftSide().getOutputArray());
        setSideFromArray(down, cube.getDownSide().getOutputArray());
        setSideFromArray(up, cube.getUpSide().getOutputArray());
        
        Cube newCube = Cube.init(front, right, back, left, down, up);
        return new CubeModel(newCube);
    }

    private void setSideFromArray(Side side, String[][] arr) {
        try {
            java.lang.reflect.Field field = Side.class.getDeclaredField("horizontalArray");
            field.setAccessible(true);
            String[][] dest = (String[][]) field.get(side);
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    dest[i][j] = arr[i][j];
                }
            }
            field.setAccessible(false);
            
            java.lang.reflect.Field vField = Side.class.getDeclaredField("verticalArray");
            vField.setAccessible(true);
            String[][] vDest = (String[][]) vField.get(side);
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    vDest[i][j] = arr[j][2 - i];
                }
            }
            vField.setAccessible(false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
