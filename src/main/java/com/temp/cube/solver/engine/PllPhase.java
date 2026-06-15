package com.temp.cube.solver.engine;

import com.temp.cube.constants.Algorithms;
import com.temp.cube.constants.SolverConfig;

/**
 * CFOP 第四步：PLL（排列最后一层，完成还原）。
 *
 * <p>用一条标准公式（枚举前后 AUF，在副本上验证完全还原，取最短）一次排列末层；
 * 未命中时回退到 {U, U', A-perm(纯角3循环), U-perm(纯棱3循环)} 宏搜索保证可解。</p>
 */
final class PllPhase {

    private static final int[][] PLL_ALGS = MoveSeq.parseAll(Algorithms.PLL);
    private static final int[] A_PERM = MoveSeq.parse(Algorithms.A_PERM);
    private static final int[] U_PERM = MoveSeq.parse(Algorithms.U_PERM);
    private static final int U_MOVE = FaceCube.U1;
    private static final int U_PRIME = FaceCube.U3;

    int[] solve(FaceCube fc) {
        if (fc.isSolved()) return new int[0];
        int[] pll = Search.tryAlgs(fc, PLL_ALGS, FaceCube::isSolved, true);
        if (pll == null) {
            pll = Search.macroSearch(fc, new int[][]{{U_MOVE}, {U_PRIME}, A_PERM, U_PERM},
                    SolverConfig.PLL_MACRO_DEPTH, FaceCube::isSolved);
        }
        return MoveSeq.simplify(pll == null ? new int[0] : pll);
    }
}
