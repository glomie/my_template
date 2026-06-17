package com.temp.cube.solver.engine;

import com.temp.cube.constants.Algorithms;

/**
 * CFOP 第三步：OLL（定向最后一层，使顶面全白）。
 *
 * <p>做法与 PLL 一致：枚举 0–3 次<b>前置 AUF</b> 做朝向对齐，对 57 条标准公式逐一在副本上验证
 * （顶层定向且保持下两层），套用匹配且最短的一条。57 条公式覆盖全部 215 个 OLL 状态，
 * 故必有一条命中；公式表见 {@link Algorithms#OLL}。</p>
 */
final class OllPhase {

    private static final int[][] OLL_ALGS = MoveSeq.parseAll(Algorithms.OLL);

    private static final Search.Goal GOAL = fc -> fc.isOLLSolved() && fc.isF2LSolved();

    int[] solve(FaceCube fc) {
        if (fc.isOLLSolved()) return new int[0];
        int[] oll = Search.tryAlgs(fc, OLL_ALGS, GOAL, false);
        return MoveSeq.simplify(oll != null ? oll : new int[0]);
    }
}
