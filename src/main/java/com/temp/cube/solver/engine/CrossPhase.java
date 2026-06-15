package com.temp.cube.solver.engine;

import com.temp.cube.constants.SolverConfig;

/**
 * CFOP 第一步：黄色十字（底面 down）。IDA* 搜索，启发 = 4 个底棱单块 god 距离的最大值。
 */
final class CrossPhase {

    /** 求解黄十字，返回 token 序列并施加到 fc。 */
    int[] solve(FaceCube fc) {
        if (fc.isCrossSolved()) return new int[0];
        int[] goal = Slots.edgeFacelets(Slots.DOWN_EDGES);
        int[] result = Search.ida(fc, goal, this::heuristic, SolverConfig.CROSS_MAX_DEPTH, SolverConfig.allFaces());
        return MoveSeq.simplify(result == null ? new int[0] : result);
    }

    private int heuristic(FaceCube fc) {
        int max = 0;
        for (int e : Slots.DOWN_EDGES) max = Math.max(max, fc.edgeDistance(e));
        return max;
    }
}
