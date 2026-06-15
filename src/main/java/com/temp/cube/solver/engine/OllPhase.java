package com.temp.cube.solver.engine;

import com.temp.cube.constants.Algorithms;
import com.temp.cube.constants.SolverConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * CFOP 第三步：OLL（定向最后一层，使顶面全白）。
 *
 * <p>先试整体 OLL 公式（命中即一条，含非十字形状，省去翻棱步）；未覆盖时兜底 2-look：
 * 翻棱做出顶面十字 → 一条 OCLL 公式定向四角 → 仍未覆盖再用 {U,Sune,Antisune} 宏。
 * 公式均在副本上验证，错误条目只会被跳过、不会解错。</p>
 */
final class OllPhase {

    private static final int[][] OLL_ALGS = MoveSeq.parseAll(Algorithms.OLL);
    private static final int[] EO_CROSS = MoveSeq.parse(Algorithms.EO_CROSS);
    private static final int[] SUNE = MoveSeq.parse(Algorithms.SUNE);
    private static final int[] ANTISUNE = MoveSeq.parse(Algorithms.ANTISUNE);
    private static final int U_MOVE = FaceCube.U1;

    int[] solve(FaceCube fc) {
        if (fc.isOLLSolved()) return new int[0];

        // 1) 整体 OLL 公式：命中即一条还原顶面。
        int[] one = Search.tryAlgs(fc, OLL_ALGS, FaceCube::isOLLSolved, false);
        if (one != null) return MoveSeq.simplify(one);

        // 2) 兜底 2-look：翻棱出顶面十字，再定向四角。
        List<Integer> all = new ArrayList<>();
        if (!fc.isTopCrossSolved()) {
            int[] eo = Search.macroSearch(fc, new int[][]{{U_MOVE}, EO_CROSS},
                    SolverConfig.EO_MACRO_DEPTH, FaceCube::isTopCrossSolved);
            if (eo != null) for (int m : eo) all.add(m);
        }
        int[] oc = Search.tryAlgs(fc, OLL_ALGS, FaceCube::isOLLSolved, false);
        if (oc == null) {
            oc = Search.macroSearch(fc, new int[][]{{U_MOVE}, SUNE, ANTISUNE},
                    SolverConfig.OLL_MACRO_DEPTH, FaceCube::isOLLSolved);
        }
        if (oc != null) for (int m : oc) all.add(m);

        return MoveSeq.simplify(MoveSeq.toIntArray(all));
    }
}
