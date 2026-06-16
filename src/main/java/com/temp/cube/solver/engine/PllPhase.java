package com.temp.cube.solver.engine;

import com.temp.cube.constants.Algorithms;

/**
 * CFOP 第四步：PLL（排列最后一层，完成还原）。
 *
 * <p>枚举前后 AUF，在副本上验证完全还原，套用匹配的标准公式（取最短）。</p>
 */
final class PllPhase {

    private static final int[][] PLL_ALGS = MoveSeq.parseAll(Algorithms.PLL);

    int[] solve(FaceCube fc) {
        if (fc.isSolved()) return new int[0];
        // Pure AUF: cube is already PLL-solved, just needs top-layer rotation.
        for (int auf = 1; auf < 4; auf++) {
            FaceCube t = fc.copy();
            for (int i = 0; i < auf; i++) t.apply(FaceCube.U1);
            if (t.isSolved()) {
                int[] seq = new int[auf];
                java.util.Arrays.fill(seq, FaceCube.U1);
                fc.apply(seq);
                return MoveSeq.simplify(seq);
            }
        }
        int[] pll = Search.tryAlgs(fc, PLL_ALGS, FaceCube::isSolved, true);
        return MoveSeq.simplify(pll != null ? pll : new int[0]);
    }
}
