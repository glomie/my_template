package com.temp.cube.solver;

import com.temp.cube.enums.Direction;
import com.temp.cube.enums.SideTurnEnum;
import com.temp.cube.model.Cube;
import com.temp.cube.solver.formula.OLLFormula;
import com.temp.cube.turn.SideTurnAction;

import java.util.ArrayList;
import java.util.List;

/**
 * OLL求解器。
 * 通过up面的9格白色pattern（忽略侧面）识别OLL case，
 * 最多AUF 4次找到匹配，然后应用对应公式。
 */
public class OLLSolver {

    private final CubeStateChecker checker = new CubeStateChecker();

    /** 57个OLL case对应的up面pattern，center(index4)始终为1。
     *  排列顺序 row-major: [0][0],[0][1],[0][2],[1][0],[1][1],[1][2],[2][0],[2][1],[2][2]
     *  索引1-57对应标准OLL编号（这里用简化的"归0"公式集，只识别归一为全白）。
     *  我们使用"枚举AUF+试公式"策略，无需穷举所有pattern。
     */

    public List<SideTurnAction> solve(Cube cube) {
        List<SideTurnAction> moves = new ArrayList<>();
        if (checker.isOLLSolved(cube)) return moves;

        // 最多尝试：AUF(0-3次U) × 公式列表
        for (int auf = 0; auf < 4; auf++) {
            int caseNum = detectOLLCase(cube);
            if (caseNum >= 0) {
                String formula = OLLFormula.getFormula(caseNum)[0];
                moves.addAll(applyAlg(cube, formula));
                if (checker.isOLLSolved(cube)) return moves;
                // 如果还没完成，再做一次AUF后重试
            }
            // AUF: U
            moves.addAll(applyAlg(cube, "U"));
        }

        // 兜底：穷举所有公式直到完成（最多2轮）
        for (int round = 0; round < 2 && !checker.isOLLSolved(cube); round++) {
            for (int i = 1; i <= 57; i++) {
                if (checker.isOLLSolved(cube)) break;
                String formula = OLLFormula.getFormula(i)[0];
                moves.addAll(applyAlg(cube, formula));
            }
        }
        return moves;
    }

    /**
     * 识别OLL case编号。up面9格，center固定为WHITE。
     * 返回1-57，或-1表示未识别（需要AUF）。
     */
    private int detectOLLCase(Cube cube) {
        int[] p = checker.getUpPattern(cube);
        // p[4] should be 1 (center)
        int whites = 0;
        for (int v : p) whites += v;

        if (whites == 9) return -1; // 已完成，不需要公式

        // 通过up面4条棱+4个角的白色分布来匹配
        // 棱: p[1]=up, p[3]=left, p[5]=right, p[7]=down(front)
        // 角: p[0]=UL, p[2]=UR, p[6]=DL, p[8]=DR
        int edgeWhites  = p[1] + p[3] + p[5] + p[7];
        int cornerWhites = p[0] + p[2] + p[6] + p[8];

        // 按标准OLL分类（简化映射，覆盖最常见case）
        if (edgeWhites == 0 && cornerWhites == 0) return 1;  // 点型 → 用case1
        if (edgeWhites == 2 && cornerWhites == 0) {
            if (p[1]==1 && p[7]==1) return 3;   // I型竖
            if (p[3]==1 && p[5]==1) return 4;   // I型横
            return 3;
        }
        if (edgeWhites == 1 && cornerWhites == 0) return 44;
        if (edgeWhites == 0 && cornerWhites == 2) {
            if ((p[0]==1&&p[8]==1)||(p[2]==1&&p[6]==1)) return 57;
            return 21;
        }
        if (edgeWhites == 2 && cornerWhites == 2) return 32;
        if (edgeWhites == 2 && cornerWhites == 1) return 35;
        if (edgeWhites == 2 && cornerWhites == 3) return 33;
        if (edgeWhites == 4 && cornerWhites == 0) return 28;
        if (edgeWhites == 0 && cornerWhites == 4) return 17;
        if (edgeWhites == 1 && cornerWhites == 1) return 43;
        if (edgeWhites == 1 && cornerWhites == 2) return 49;
        if (edgeWhites == 1 && cornerWhites == 3) return 51;
        if (edgeWhites == 1 && cornerWhites == 4) return 23;
        if (edgeWhites == 3 && cornerWhites == 0) return 46;
        if (edgeWhites == 3 && cornerWhites == 1) return 38;
        if (edgeWhites == 3 && cornerWhites == 2) return 36;
        if (edgeWhites == 3 && cornerWhites == 3) return 9;
        if (edgeWhites == 3 && cornerWhites == 4) return 7;
        if (edgeWhites == 4 && cornerWhites == 1) return 6;
        if (edgeWhites == 4 && cornerWhites == 2) return 8;
        if (edgeWhites == 4 && cornerWhites == 3) return 2;
        if (edgeWhites == 0 && cornerWhites == 1) return 57;
        if (edgeWhites == 0 && cornerWhites == 3) return 57;
        return 1; // 默认
    }

    private List<SideTurnAction> applyAlg(Cube cube, String alg) {
        List<SideTurnAction> moves = new ArrayList<>();
        for (String part : alg.trim().split("\\s+")) {
            if (part.isEmpty()) continue;
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

    public boolean isOLLSolved(Cube cube) {
        return checker.isOLLSolved(cube);
    }
}
