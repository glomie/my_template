package com.temp.cube.solver;

import com.temp.cube.enums.Direction;
import com.temp.cube.enums.SideTurnEnum;
import com.temp.cube.model.Cube;
import com.temp.cube.turn.SideTurnAction;

import java.util.ArrayList;
import java.util.List;

/**
 * PLL 求解器（有界"宏动作"搜索）。
 *
 * <p>仅使用纯面转动的 PLL 公式。把"一次 AUF 预转 + 一条公式"视为一个宏动作，
 * 做深度不超过 3 的迭代加深搜索，每层结束尝试末尾 AUF 对齐判断是否整体还原。
 * 由于这组公式（角三循环、棱三循环、T/J/R/V/G/N/E/Y 等）能生成整个 PLL 置换群，
 * 任意 PLL 情形都能在 ≤3 个宏动作内解决（如 H/Z 由两条棱循环复合得到）。
 * 所有公式都保持 F2L 与顶层定向不变。
 */
public class PLLSolver {

    private final CubeStateChecker checker = new CubeStateChecker();

    private static final String[] PLL_ALGS = {
        "R U R' U' R' F R2 U' R' U' R U R' F'",              // T
        "F R U' R' U' R U R' F' R U R' U' R' F R F'",        // Y
        "R U' R U R U R U' R' U' R2",                        // Ua
        "R2 U R U R' U' R' U' R' U R'",                      // Ub
        "R' F R' B2 R F' R' B2 R2",                          // Aa
        "R B' R F2 R' B R F2 R2",                            // Ab
        "R B' R' F R B R' F' R B R' F R B' R' F'",           // E
        "R' U' F' R U R' U' R' F R2 U' R' U' R U R' U R",    // F
        "R2 U R' U R' U' R U' R2 U' D R' U R D'",            // Ga
        "R' U' R U D' R2 U R' U R U' R U' R2 D",             // Gb
        "R2 U' R U' R U R' U R2 U D' R U' R' D",             // Gc
        "R U R' U' D R2 U' R U' R' U R' U R2 D'",            // Gd
        "R' U L' U2 R U' R' U2 R L",                         // Ja
        "R U R' F' R U R' U' R' F R2 U' R'",                 // Jb
        "R U' R' U R U' R' F' R U R' U' R' F R2 U' R' U2 R U' R'", // Nb
        "R U' R' U' R U R D' R' U' R D R' U2 R'",            // Ra
        "R' U2 R U2 R' F R U R' U' R' F' R2",                // Rb
        "R' U R' U' R D' R' D R' U D' R2 U' R2 D R2",        // V
    };

    private static final int MAX_DEPTH = 3;

    public List<SideTurnAction> solve(Cube cube) {
        List<SideTurnAction> moves = new ArrayList<>();
        if (checker.isSolved(cube)) return moves;

        for (int depth = 1; depth <= MAX_DEPTH; depth++) {
            if (dfs(cube, depth, moves)) return moves;
        }
        return moves; // 理论上不会到达
    }

    /** 宏动作迭代加深：每个宏 = (0..3 次 U) + 一条公式；叶子处尝试末尾 AUF 对齐。 */
    private boolean dfs(Cube cube, int depth, List<SideTurnAction> moves) {
        if (solveByAUF(cube, moves)) return true;
        if (depth == 0) return false;

        for (int auf = 0; auf < 4; auf++) {
            for (String alg : PLL_ALGS) {
                List<SideTurnAction> macro = new ArrayList<>();
                for (int i = 0; i < auf; i++) macro.addAll(apply(cube, "U"));
                macro.addAll(apply(cube, alg));

                int mark = moves.size();
                moves.addAll(macro);
                if (dfs(cube, depth - 1, moves)) return true;
                // 回溯
                while (moves.size() > mark) moves.remove(moves.size() - 1);
                undo(cube, macro);
            }
        }
        return false;
    }

    /** 尝试末尾补 0..3 次 U 使整体还原；成功则把这些 U 写入 moves。 */
    private boolean solveByAUF(Cube cube, List<SideTurnAction> moves) {
        for (int i = 0; i < 4; i++) {
            if (checker.isSolved(cube)) return true;
            moves.addAll(apply(cube, "U"));
        }
        // 撤销刚才多加的 4 次 U（等于不变），并判断初始是否已解决
        for (int i = 0; i < 4; i++) {
            cube.turn(SideTurnEnum.U, Direction.COUNTERCLOCKWISE);
            moves.remove(moves.size() - 1);
        }
        return false;
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

    public boolean isPLLSolved(Cube cube) {
        return checker.isPLLSolved(cube);
    }
}
