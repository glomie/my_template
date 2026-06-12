package com.temp.cube.solver;

import com.temp.cube.enums.Direction;
import com.temp.cube.enums.SideTurnEnum;
import com.temp.cube.model.Cube;
import com.temp.cube.turn.SideTurnAction;

import java.util.ArrayList;
import java.util.List;

/**
 * PLL求解器。
 * 策略: AUF(0-3次U) × 公式列表，穷举匹配直到solved。
 * 覆盖最常见的PLL公式集。
 */
public class PLLSolver {

    private final CubeStateChecker checker = new CubeStateChecker();

    // 常用PLL公式，按命中率排序
    private static final String[] PLL_ALGS = {
        // T perm
        "R U R' U' R' F R2 U' R' U' R U R' F'",
        // Y perm
        "F R U' R' U' R U R' F' R U R' U' R' F R F'",
        // U perm (a)
        "R U' R U R U R U' R' U' R2",
        // U perm (b)
        "R2 U R U R' U' R' U' R' U R'",
        // H perm
        "M2 U M2 U2 M2 U M2",
        // Z perm
        "M2 U M2 U M' U2 M2 U2 M' U2",
        // A perm (a)
        "R' F R' B2 R F' R' B2 R2",
        // A perm (b)
        "R B' R F2 R' B R F2 R2",
        // E perm
        "R B' R' F R B R' F' R B R' F R B' R' F'",
        // F perm
        "R' U' F' R U R' U' R' F R2 U' R' U' R U R' U R",
        // G perm (a)
        "R2 U R' U R' U' R U' R2 U' D R' U R D'",
        // G perm (b)
        "R' U' R U D' R2 U R' U R U' R U' R2 D",
        // G perm (c)
        "R2 U' R U' R U R' U R2 U D' R U' R' D",
        // G perm (d)
        "R U R' U' D R2 U' R U' R' U R' U R2 D'",
        // J perm (a)
        "R' U L' U2 R U' R' U2 R L",
        // J perm (b)
        "R U R' F' R U R' U' R' F R2 U' R'",
        // N perm (a)
        "R' U R U' R' F' U' F R U R' F R' F' R U' R",
        // N perm (b)
        "R U' R' U R U' R' F' R U R' U' R' F R2 U' R' U2 R U' R'",
        // R perm (a)
        "R U' R' U' R U R D' R' U' R D R' U2 R'",
        // R perm (b)
        "R' U2 R U2 R' F R U R' U' R' F' R2",
        // V perm
        "R' U R' U' R D' R' D R' U D' R2 U' R2 D R2",
    };

    public List<SideTurnAction> solve(Cube cube) {
        List<SideTurnAction> moves = new ArrayList<>();
        if (checker.isPLLSolved(cube)) return moves;

        // 最多4次AUF，每次AUF后尝试所有公式
        for (int auf = 0; auf < 4; auf++) {
            for (String alg : PLL_ALGS) {
                if (checker.isPLLSolved(cube)) return moves;

                // 试着应用，如果不解决就撤销
                List<SideTurnAction> trial = applyAlg(cube, alg);
                if (checker.isPLLSolved(cube)) {
                    moves.addAll(trial);
                    return moves;
                }
                // 撤销
                undoMoves(cube, trial);
            }

            if (checker.isPLLSolved(cube)) return moves;
            // AUF
            moves.addAll(applyAlg(cube, "U"));
        }

        // 最后AUF对齐
        for (int i = 0; i < 3 && !checker.isPLLSolved(cube); i++) {
            moves.addAll(applyAlg(cube, "U"));
        }

        return moves;
    }

    private void undoMoves(Cube cube, List<SideTurnAction> moves) {
        for (int i = moves.size() - 1; i >= 0; i--) {
            SideTurnAction a = moves.get(i);
            Direction inv = a.getDirection() == Direction.CLOCKWISE
                ? Direction.COUNTERCLOCKWISE : Direction.CLOCKWISE;
            cube.turn(a.getSideTurnEnum(), inv);
        }
    }

    private List<SideTurnAction> applyAlg(Cube cube, String alg) {
        List<SideTurnAction> moves = new ArrayList<>();
        for (String part : alg.trim().split("\\s+")) {
            if (part.isEmpty()) continue;
            // 跳过slice moves (M等)
            SideTurnEnum side = parseSide(part.charAt(0));
            if (side == null) continue;
            Direction dir = part.contains("'") ? Direction.COUNTERCLOCKWISE : Direction.CLOCKWISE;
            int cnt = part.contains("2") ? 2 : 1;
            for (int i = 0; i < cnt; i++) {
                moves.add(new SideTurnAction(side, dir));
                cube.turn(side, dir);
            }
        }
        return moves;
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
