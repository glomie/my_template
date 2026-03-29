package com.temp.cube.solver;

import com.temp.cube.enums.Color;
import com.temp.cube.model.Cube;
import com.temp.cube.model.Side;

public class CubeStateChecker {

    public boolean isSolved(Cube cube) {
        return isSideSolved(cube.getUpSide(), Color.WHITE) &&
               isSideSolved(cube.getDownSide(), Color.YELLOW) &&
               isSideSolved(cube.getFrontSide(), Color.GREEN) &&
               isSideSolved(cube.getBackSide(), Color.BLUE) &&
               isSideSolved(cube.getRightSide(), Color.RED) &&
               isSideSolved(cube.getLeftSide(), Color.ORANGE);
    }

    public boolean isSideSolved(Side side, Color expectedColor) {
        String[][] array = side.getOutputArray();
        String expected = expectedColor.name();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!array[i][j].equals(expected)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isCrossSolved(Cube cube) {
        Side upSide = cube.getUpSide();
        String[][] upArray = upSide.getOutputArray();

        if (!upArray[0][1].equals(Color.WHITE.name()) || 
            !upArray[1][0].equals(Color.WHITE.name()) ||
            !upArray[1][2].equals(Color.WHITE.name()) || 
            !upArray[2][1].equals(Color.WHITE.name())) {
            return false;
        }

        Side frontSide = cube.getFrontSide();
        Side rightSide = cube.getRightSide();
        Side backSide = cube.getBackSide();
        Side leftSide = cube.getLeftSide();

        if (!frontSide.getOutputArray()[0][1].equals(Color.GREEN.name())) return false;
        if (!rightSide.getOutputArray()[0][1].equals(Color.RED.name())) return false;
        if (!backSide.getOutputArray()[0][1].equals(Color.BLUE.name())) return false;
        if (!leftSide.getOutputArray()[0][1].equals(Color.ORANGE.name())) return false;

        return true;
    }

    public boolean isF2LSolved(Cube cube) {
        Side upSide = cube.getUpSide();
        String[][] upArray = upSide.getOutputArray();

        if (!upArray[0][0].equals(Color.WHITE.name()) || !upArray[0][2].equals(Color.WHITE.name()) ||
            !upArray[2][0].equals(Color.WHITE.name()) || !upArray[2][2].equals(Color.WHITE.name())) {
            return false;
        }

        return isSideSolved(cube.getFrontSide(), Color.GREEN) &&
               isSideSolved(cube.getRightSide(), Color.RED) &&
               isSideSolved(cube.getBackSide(), Color.BLUE) &&
               isSideSolved(cube.getLeftSide(), Color.ORANGE);
    }

    public boolean isOLLSolved(Cube cube) {
        return isSideSolved(cube.getUpSide(), Color.WHITE);
    }

    public boolean isPLLSolved(Cube cube) {
        return isSolved(cube);
    }

    public int getOLLCase(Cube cube) {
        Side upSide = cube.getUpSide();
        String[][] upArray = upSide.getOutputArray();

        int[] pattern = new int[9];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (upArray[i][j].equals(Color.WHITE.name())) {
                    pattern[i * 3 + j] = 1;
                } else {
                    pattern[i * 3 + j] = 0;
                }
            }
        }

        return matchOLLCase(pattern);
    }

    public int getPLLCase(Cube cube) {
        if (isSolved(cube)) {
            return 0;
        }

        Side upSide = cube.getUpSide();
        Side frontSide = cube.getFrontSide();

        String up0 = upSide.getOutputArray()[0][0];
        String up2 = upSide.getOutputArray()[0][2];
        String up6 = upSide.getOutputArray()[2][0];
        String up8 = upSide.getOutputArray()[2][2];

        String front0 = frontSide.getOutputArray()[0][0];
        String front2 = frontSide.getOutputArray()[0][2];
        String front6 = frontSide.getOutputArray()[2][0];
        String front8 = frontSide.getOutputArray()[2][2];

        if (up0.equals(up2) && up6.equals(up8)) {
            if (front0.equals(front2) && front6.equals(front8)) {
                return matchPLLByEdges(cube);
            }
        }

        return matchPLLCase(cube);
    }

    private int matchOLLCase(int[] pattern) {
        int count = 0;
        for (int i = 0; i < 9; i++) {
            if (pattern[i] == 1) count++;
        }

        switch (count) {
            case 9:
                return 1;
            case 8:
                return 2;
            case 7:
                for (int i = 0; i < 9; i++) {
                    if (pattern[i] == 0) {
                        if (i == 4) return 3;
                    }
                }
                return 4;
            default:
                return matchDetailedOLL(pattern);
        }
    }

    private int matchDetailedOLL(int[] pattern) {
        int[][] dotCases = {
            {0,0,0,0,1,0,0,0,0},
            {1,0,0,0,0,0,0,0,1},
            {0,0,1,0,0,0,1,0,0},
            {0,1,0,0,0,0,0,1,0},
            {1,1,0,0,1,0,0,0,0},
            {0,0,1,0,1,0,1,0,0},
            {0,1,0,1,0,0,0,0,0},
            {0,0,0,0,0,1,0,1,0}
        };

        for (int i = 0; i < dotCases.length; i++) {
            if (matchesPattern(pattern, dotCases[i])) {
                return 10 + i;
            }
        }

        return 57;
    }

    private int matchPLLCase(Cube cube) {
        Side upSide = cube.getUpSide();
        String[][] upArray = upSide.getOutputArray();

        String[] corners = {
            upArray[0][0], upArray[0][2], upArray[2][2], upArray[2][0]
        };

        int sameCount = 0;
        for (int i = 0; i < 4; i++) {
            if (corners[i].equals(corners[(i + 1) % 4])) {
                sameCount++;
            }
        }

        if (sameCount == 2) {
            return 1;
        } else if (sameCount == 0) {
            Side frontSide = cube.getFrontSide();
            if (frontSide.getOutputArray()[0][0].equals(frontSide.getOutputArray()[0][2])) {
                return 2;
            }
            return 3;
        }

        return 10;
    }

    private int matchPLLByEdges(Cube cube) {
        Side frontSide = cube.getFrontSide();
        Side rightSide = cube.getRightSide();
        Side backSide = cube.getBackSide();
        Side leftSide = cube.getLeftSide();

        String f0 = frontSide.getOutputArray()[0][0];
        String r0 = rightSide.getOutputArray()[0][0];
        String b0 = backSide.getOutputArray()[0][0];
        String l0 = leftSide.getOutputArray()[0][0];

        if (f0.equals(r0) && r0.equals(b0) && b0.equals(l0)) {
            return 0;
        }

        int sameCount = 0;
        if (f0.equals(r0)) sameCount++;
        if (r0.equals(b0)) sameCount++;
        if (b0.equals(l0)) sameCount++;
        if (l0.equals(f0)) sameCount++;

        if (sameCount == 2) return 1;
        if (sameCount == 0) return 2;

        return 3;
    }

    private boolean matchesPattern(int[] actual, int[] expected) {
        for (int i = 0; i < 9; i++) {
            if (actual[i] != expected[i]) return false;
        }
        return true;
    }

    public String[] getSideColors(Cube cube) {
        return new String[]{
            cube.getUpSide().getOutputArray()[0][0],
            cube.getDownSide().getOutputArray()[0][0],
            cube.getFrontSide().getOutputArray()[0][0],
            cube.getBackSide().getOutputArray()[0][0],
            cube.getRightSide().getOutputArray()[0][0],
            cube.getLeftSide().getOutputArray()[0][0]
        };
    }
}
