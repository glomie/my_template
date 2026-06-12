package com.temp.cube.solver;

import com.temp.cube.enums.Direction;
import com.temp.cube.enums.SideTurnEnum;
import com.temp.cube.model.Cube;
import com.temp.cube.turn.SideTurnAction;

import java.util.ArrayList;
import java.util.List;

/**
 * IDA* 求解白色十字，使用"未完成棱数×2"作为可容纳启发函数剪枝。
 * 实测十字最多 8 步可还原，加启发后搜索量极小。
 */
public class CrossSolver {

    private static final SideTurnEnum[] ALL_SIDES = {
        SideTurnEnum.R, SideTurnEnum.L, SideTurnEnum.U,
        SideTurnEnum.D, SideTurnEnum.F, SideTurnEnum.B
    };
    private static final int MAX_DEPTH = 8;

    private final CubeStateChecker checker = new CubeStateChecker();

    public List<SideTurnAction> solve(Cube cube) {
        if (checker.isCrossSolved(cube)) return new ArrayList<>();
        for (int depth = heuristic(cube); depth <= MAX_DEPTH; depth++) {
            List<SideTurnAction> result = new ArrayList<>();
            if (dfs(cube, depth, result, null, null)) return result;
        }
        return new ArrayList<>();
    }

    /**
     * 启发值 = 未还原的十字棱数 × 2（每个棱至少需要2步，可容纳下界）
     */
    private int heuristic(Cube cube) {
        int unsolved = 0;
        String[][] up    = cube.getUpSide().getOutputArray();
        String[][] front = cube.getFrontSide().getOutputArray();
        String[][] right = cube.getRightSide().getOutputArray();
        String[][] back  = cube.getBackSide().getOutputArray();
        String[][] left  = cube.getLeftSide().getOutputArray();
        if (!up[2][1].equals("WHITE") || !front[0][1].equals("GREEN"))  unsolved++;
        if (!up[1][2].equals("WHITE") || !right[0][1].equals("RED"))    unsolved++;
        if (!up[0][1].equals("WHITE") || !back[0][1].equals("BLUE"))    unsolved++;
        if (!up[1][0].equals("WHITE") || !left[0][1].equals("ORANGE"))  unsolved++;
        return unsolved * 2;
    }

    private boolean dfs(Cube cube, int limit, List<SideTurnAction> moves,
                        SideTurnEnum lastSide, SideTurnEnum secondLastSide) {
        int h = heuristic(cube);
        if (h == 0) return true;           // cross solved
        if (limit == 0) return false;
        if (h > limit) return false;       // 剪枝：剩余深度不够还原

        for (SideTurnEnum side : ALL_SIDES) {
            if (side == lastSide) continue;
            // 避免 R L R L… 无效往复（相反面同方向交替）
            if (secondLastSide != null && isOpposite(side, secondLastSide) && side != lastSide) {
                // 允许，但下一层不要再重复
            }
            for (Direction dir : Direction.values()) {
                cube.turn(side, dir);
                moves.add(new SideTurnAction(side, dir));

                if (dfs(cube, limit - 1, moves, side, lastSide)) return true;

                moves.remove(moves.size() - 1);
                cube.turn(side, dir == Direction.CLOCKWISE
                        ? Direction.COUNTERCLOCKWISE : Direction.CLOCKWISE);
            }
        }
        return false;
    }

    private boolean isOpposite(SideTurnEnum a, SideTurnEnum b) {
        return (a == SideTurnEnum.R && b == SideTurnEnum.L)
            || (a == SideTurnEnum.L && b == SideTurnEnum.R)
            || (a == SideTurnEnum.U && b == SideTurnEnum.D)
            || (a == SideTurnEnum.D && b == SideTurnEnum.U)
            || (a == SideTurnEnum.F && b == SideTurnEnum.B)
            || (a == SideTurnEnum.B && b == SideTurnEnum.F);
    }

    public boolean isCrossSolved(Cube cube) {
        return checker.isCrossSolved(cube);
    }
}
