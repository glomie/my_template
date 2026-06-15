package com.temp.cube.solver;

import com.temp.cube.enums.Direction;
import com.temp.cube.enums.SideTurnEnum;
import com.temp.cube.model.Cube;
import com.temp.cube.turn.SideTurnAction;

import java.util.ArrayList;
import java.util.List;

/**
 * IDA* 求解白色十字，使用"未完成棱数"作为可容纳（admissible）启发函数剪枝。
 *
 * <p>注意：本魔方模型只有 90° 转动（无 180° 双转），属于 QTM 度量，
 * 十字可能需要超过 8 步，故 MAX_DEPTH 取 12。启发函数必须取"未完成棱数×1"
 * （每条棱至少还需 1 步）；若用 ×2 会高估代价、把真实解路径剪掉，导致求解失败。
 */
public class CrossSolver {

    private static final SideTurnEnum[] ALL_SIDES = {
        SideTurnEnum.R, SideTurnEnum.L, SideTurnEnum.U,
        SideTurnEnum.D, SideTurnEnum.F, SideTurnEnum.B
    };
    private static final int MAX_DEPTH = 12;

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
     * 启发值 = 未还原的十字棱数（每条棱至少还需 1 步，是可容纳下界）。
     */
    private int heuristic(Cube cube) {
        int unsolved = 0;
        String[][] down  = cube.getDownSide().getOutputArray();
        String[][] front = cube.getFrontSide().getOutputArray();
        String[][] right = cube.getRightSide().getOutputArray();
        String[][] back  = cube.getBackSide().getOutputArray();
        String[][] left  = cube.getLeftSide().getOutputArray();
        if (!down[0][1].equals("YELLOW") || !front[2][1].equals("GREEN"))  unsolved++;
        if (!down[1][2].equals("YELLOW") || !right[2][1].equals("RED"))    unsolved++;
        if (!down[2][1].equals("YELLOW") || !back[2][1].equals("BLUE"))    unsolved++;
        if (!down[1][0].equals("YELLOW") || !left[2][1].equals("ORANGE"))  unsolved++;
        return unsolved;
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
