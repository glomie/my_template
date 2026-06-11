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
                if (!array[i][j].equals(expected)) return false;
            }
        }
        return true;
    }

    /**
     * 白色十字在up面，且4个棱块颜色与相邻侧面center匹配。
     * up面坐标系: row0=靠近back, row2=靠近front, col0=靠近left, col2=靠近right
     * 所以 up[2][1]=front棱, up[0][1]=back棱, up[1][0]=left棱, up[1][2]=right棱
     */
    public boolean isCrossSolved(Cube cube) {
        String[][] up = cube.getUpSide().getOutputArray();
        if (!up[2][1].equals(Color.WHITE.name())) return false;
        if (!up[0][1].equals(Color.WHITE.name())) return false;
        if (!up[1][0].equals(Color.WHITE.name())) return false;
        if (!up[1][2].equals(Color.WHITE.name())) return false;

        if (!cube.getFrontSide().getOutputArray()[0][1].equals(Color.GREEN.name())) return false;
        if (!cube.getBackSide().getOutputArray()[0][1].equals(Color.BLUE.name())) return false;
        if (!cube.getLeftSide().getOutputArray()[0][1].equals(Color.ORANGE.name())) return false;
        if (!cube.getRightSide().getOutputArray()[0][1].equals(Color.RED.name())) return false;
        return true;
    }

    /**
     * F2L完成: down面全黄 + 四侧面底两行颜色正确
     */
    public boolean isF2LSolved(Cube cube) {
        if (!isSideSolved(cube.getDownSide(), Color.YELLOW)) return false;

        String[][] front = cube.getFrontSide().getOutputArray();
        String[][] right = cube.getRightSide().getOutputArray();
        String[][] back  = cube.getBackSide().getOutputArray();
        String[][] left  = cube.getLeftSide().getOutputArray();

        for (int row = 1; row <= 2; row++) {
            for (int col = 0; col < 3; col++) {
                if (!front[row][col].equals(Color.GREEN.name()))  return false;
                if (!right[row][col].equals(Color.RED.name()))    return false;
                if (!back[row][col].equals(Color.BLUE.name()))    return false;
                if (!left[row][col].equals(Color.ORANGE.name()))  return false;
            }
        }
        // 十字也要已完成（row0 of each side）
        return isCrossSolved(cube);
    }

    /** OLL完成: up面全白 */
    public boolean isOLLSolved(Cube cube) {
        return isSideSolved(cube.getUpSide(), Color.WHITE);
    }

    /** PLL完成即整个魔方完成 */
    public boolean isPLLSolved(Cube cube) {
        return isSolved(cube);
    }

    /**
     * 返回up面9格颜色编码: 1=WHITE, 0=其他
     * 索引顺序 row-major: [0][0..2], [1][0..2], [2][0..2]
     */
    public int[] getUpPattern(Cube cube) {
        String[][] up = cube.getUpSide().getOutputArray();
        int[] pattern = new int[9];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                pattern[i * 3 + j] = up[i][j].equals(Color.WHITE.name()) ? 1 : 0;
        return pattern;
    }
}
