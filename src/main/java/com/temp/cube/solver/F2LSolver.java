package com.temp.cube.solver;

import com.temp.cube.enums.Direction;
import com.temp.cube.enums.SideTurnEnum;
import com.temp.cube.model.Cube;
import com.temp.cube.turn.SideTurnAction;

import java.util.ArrayList;
import java.util.List;

/**
 * F2L 求解器。
 *
 * 策略：对4个槽位分别做 IDA*，使用"未完成槽数×3"启发剪枝。
 * 每个槽位的搜索会保护已还原的十字和已还原的其他槽位。
 *
 * 坐标参考（up: row0=back, row2=front, col0=left, col2=right）：
 *   slot0 FR: front[row][2], right[row][0], down[0][2]
 *   slot1 RB: right[row][2], back[row][0], down[2][2]
 *   slot2 BL: back[row][2], left[row][2], down[2][0]
 *   slot3 LF: left[row][0], front[row][0], down[0][0]
 */
public class F2LSolver {

    private static final SideTurnEnum[] ALL_SIDES = {
        SideTurnEnum.R, SideTurnEnum.L, SideTurnEnum.U,
        SideTurnEnum.D, SideTurnEnum.F, SideTurnEnum.B
    };

    private static final int MAX_DEPTH = 14;
    private final CubeStateChecker checker = new CubeStateChecker();

    public List<SideTurnAction> solve(Cube cube) {
        List<SideTurnAction> all = new ArrayList<>();
        if (checker.isF2LSolved(cube)) return all;

        for (int slot = 0; slot < 4; slot++) {
            if (isSlotSolved(cube, slot)) continue;
            int solved = countSolvedSlots(cube);
            for (int depth = 1; depth <= MAX_DEPTH; depth++) {
                List<SideTurnAction> result = new ArrayList<>();
                if (dfs(cube, slot, solved, depth, result, null)) {
                    all.addAll(result);
                    break;
                }
            }
        }
        return all;
    }

    /** 当前槽的 IDA*，保护十字 + 已完成的其他槽不被破坏。 */
    private boolean dfs(Cube cube, int targetSlot, int minSolved,
                        int limit, List<SideTurnAction> moves, SideTurnEnum lastSide) {
        if (isSlotSolved(cube, targetSlot) && countSolvedSlots(cube) >= minSolved + 1
                && checker.isCrossSolved(cube)) return true;
        if (limit == 0) return false;
        int h = isSlotSolved(cube, targetSlot) ? 0 : 3;
        if (h > limit) return false;

        for (SideTurnEnum side : ALL_SIDES) {
            if (side == lastSide) continue;
            for (Direction dir : Direction.values()) {
                cube.turn(side, dir);
                moves.add(new SideTurnAction(side, dir));

                if (dfs(cube, targetSlot, minSolved, limit - 1, moves, side)) return true;

                moves.remove(moves.size() - 1);
                cube.turn(side, dir == Direction.CLOCKWISE
                        ? Direction.COUNTERCLOCKWISE : Direction.CLOCKWISE);
            }
        }
        return false;
    }

    private int countSolvedSlots(Cube cube) {
        int c = 0;
        for (int i = 0; i < 4; i++) if (isSlotSolved(cube, i)) c++;
        return c;
    }

    /**
     * 坐标对应关系：
     *   down row0=front, row2=back, col0=left, col2=right
     *   right face: col0 = adjacent to front, col2 = adjacent to back
     *   back face:  col0 = adjacent to right, col2 = adjacent to left
     *   left face:  col0 = adjacent to front, col2 = adjacent to back
     */
    boolean isSlotSolved(Cube cube, int slot) {
        String[][] f = cube.getFrontSide().getOutputArray();
        String[][] r = cube.getRightSide().getOutputArray();
        String[][] b = cube.getBackSide().getOutputArray();
        String[][] l = cube.getLeftSide().getOutputArray();
        String[][] d = cube.getDownSide().getOutputArray();
        switch (slot) {
            case 0: // FR
                return f[1][2].equals("GREEN") && f[2][2].equals("GREEN")
                    && r[1][0].equals("RED")   && r[2][0].equals("RED")
                    && d[0][2].equals("YELLOW");
            case 1: // RB
                return r[1][2].equals("RED")   && r[2][2].equals("RED")
                    && b[1][0].equals("BLUE")  && b[2][0].equals("BLUE")
                    && d[2][2].equals("YELLOW");
            case 2: // BL
                return b[1][2].equals("BLUE")   && b[2][2].equals("BLUE")
                    && l[1][2].equals("ORANGE") && l[2][2].equals("ORANGE")
                    && d[2][0].equals("YELLOW");
            case 3: // LF
                return l[1][0].equals("ORANGE") && l[2][0].equals("ORANGE")
                    && f[1][0].equals("GREEN")  && f[2][0].equals("GREEN")
                    && d[0][0].equals("YELLOW");
            default: return false;
        }
    }

    public boolean isF2LSolved(Cube cube) {
        return checker.isF2LSolved(cube);
    }
}
