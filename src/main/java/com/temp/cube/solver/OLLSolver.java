package com.temp.cube.solver;

import com.temp.cube.enums.Direction;
import com.temp.cube.enums.SideTurnEnum;
import com.temp.cube.model.Cube;
import com.temp.cube.turn.SideTurnAction;

import java.util.ArrayList;
import java.util.List;

/**
 * OLL 求解器（2-look，贪心收敛）。
 *
 * <p>分两步、只用面转动的少量公式：
 * <ol>
 *   <li>顶层棱定向：用 {@code F R U R' U' F'} 与 {@code F U R U' R' F'} 把 U 面凑出白色十字；</li>
 *   <li>顶层角定向：用 Sune / Anti-Sune 把 4 个角都翻成白色朝上。</li>
 * </ol>
 * 每步在 4 个 AUF（U 预转）× 可选公式里，挑选"能让已定向块数严格增加"的一个提交，
 * 循环直到该步目标达成。所有公式都保持 F2L 不被破坏。
 */
public class OLLSolver {

    private static final String EDGE_LINE = "F R U R' U' F'";
    private static final String EDGE_L    = "F U R U' R' F'";

    private static final int MAX_ITER = 30;

    private final CubeStateChecker checker = new CubeStateChecker();

    public List<SideTurnAction> solve(Cube cube) {
        List<SideTurnAction> moves = new ArrayList<>();
        if (checker.isOLLSolved(cube)) return moves;

        // 1) 顶层棱定向 -> 白色十字（贪心：两条公式 + AUF）
        greedy(cube, moves, new String[]{EDGE_LINE, EDGE_L}, true);
        // 2) 顶层角定向 -> U 面全白（初学者 R' D' R D 原地翻角法，必然收敛）
        orientCorners(cube, moves);

        return moves;
    }

    /**
     * 初学者角定向：把待定向角逐个转到 URF（U[2][2]），用 {@code R' D' R D} 原地翻正，
     * 再用 U 把下一个角转到 URF。该序列只翻 URF 角、不改变其它顶层角的位置，
     * 4 个角处理完且 4 次 U 累计成整圈后，F2L 与已成的顶层十字都恢复。
     */
    private void orientCorners(Cube cube, List<SideTurnAction> moves) {
        for (int i = 0; i < 4; i++) {
            int guard = 0;
            while (!cube.getUpSide().getOutputArray()[2][2].equals("WHITE") && guard < 6) {
                moves.addAll(apply(cube, "R' D' R D"));
                guard++;
            }
            moves.addAll(apply(cube, "U"));
        }
    }

    /**
     * 贪心：反复在 (AUF 0..3) × algs 中找一个能让计数严格变大的组合并提交。
     * @param crossPhase true=按顶层十字棱计数, false=按 U 面全白格数计数
     */
    private void greedy(Cube cube, List<SideTurnAction> moves, String[] algs, boolean crossPhase) {
        for (int iter = 0; iter < MAX_ITER; iter++) {
            int cur = crossPhase ? crossCount(cube) : whiteCount(cube);
            int target = crossPhase ? 4 : 9;
            if (cur >= target) return;

            boolean improved = false;
            search:
            for (int auf = 0; auf < 4 && !improved; auf++) {
                for (String alg : algs) {
                    List<SideTurnAction> trial = new ArrayList<>();
                    for (int i = 0; i < auf; i++) trial.addAll(apply(cube, "U"));
                    trial.addAll(apply(cube, alg));

                    int now = crossPhase ? crossCount(cube) : whiteCount(cube);
                    if (now > cur) {
                        moves.addAll(trial);
                        improved = true;
                        break search;
                    }
                    undo(cube, trial);
                }
            }
            if (!improved) return; // 兜底：无法再改进则停止（极少发生）
        }
    }

    /** U 面白色格数（含中心），9 表示 OLL 完成 */
    private int whiteCount(Cube cube) {
        String[][] up = cube.getUpSide().getOutputArray();
        int c = 0;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (up[i][j].equals("WHITE")) c++;
        return c;
    }

    /** 顶层十字棱（4 条）中白色的数量 */
    private int crossCount(Cube cube) {
        String[][] up = cube.getUpSide().getOutputArray();
        int c = 0;
        if (up[0][1].equals("WHITE")) c++;
        if (up[1][0].equals("WHITE")) c++;
        if (up[1][2].equals("WHITE")) c++;
        if (up[2][1].equals("WHITE")) c++;
        return c;
    }

    private List<SideTurnAction> apply(Cube cube, String alg) {
        List<SideTurnAction> applied = new ArrayList<>();
        for (String part : alg.trim().split("\\s+")) {
            if (part.isEmpty()) continue;
            SideTurnEnum side = parseSide(part.charAt(0));
            if (side == null) continue;
            Direction dir = part.contains("'") ? Direction.COUNTERCLOCKWISE : Direction.CLOCKWISE;
            int cnt = part.contains("2") ? 2 : 1;
            for (int i = 0; i < cnt; i++) {
                applied.add(new SideTurnAction(side, dir));
                cube.turn(side, dir);
            }
        }
        return applied;
    }

    private void undo(Cube cube, List<SideTurnAction> applied) {
        for (int i = applied.size() - 1; i >= 0; i--) {
            SideTurnAction a = applied.get(i);
            cube.turn(a.getSideTurnEnum(), a.getDirection() == Direction.CLOCKWISE
                    ? Direction.COUNTERCLOCKWISE : Direction.CLOCKWISE);
        }
    }

    private SideTurnEnum parseSide(char c) {
        switch (c) {
            case 'R': return SideTurnEnum.R;
            case 'L': return SideTurnEnum.L;
            case 'U': return SideTurnEnum.U;
            case 'D': return SideTurnEnum.D;
            case 'F': return SideTurnEnum.F;
            case 'B': return SideTurnEnum.B;
            default:  return null;
        }
    }

    public boolean isOLLSolved(Cube cube) {
        return checker.isOLLSolved(cube);
    }
}
