package com.temp.cube.solver.engine;

/**
 * CFOP 第三步：OLL（定向最后一层，使顶面全白）。
 *
 * <p>通过 12-bit 指纹在 O(1) 时间查表，直接套用对应公式（含前置AUF）。
 * 若未命中则保持当前状态（返回空序列）。</p>
 */
final class OllPhase {

    int[] solve(FaceCube fc) {
        if (fc.isOLLSolved()) return new int[0];
        int[] seq = OllRecognizer.tryRecognize(fc);
        return seq != null ? MoveSeq.simplify(seq) : new int[0];
    }
}
