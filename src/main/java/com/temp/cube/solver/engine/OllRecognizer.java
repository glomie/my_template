package com.temp.cube.solver.engine;

import com.temp.cube.constants.Algorithms;

import java.util.HashMap;
import java.util.Map;

/**
 * OLL 情形识别器：类加载时把每条 OLL 公式逆向施加到已还原状态，
 * 自动生成 "12-bit 指纹 → 解法序列（含前置AUF）" 映射表。
 *
 * <p>指纹设计（12 bit）：
 * <ul>
 *   <li>bits 0–3：4 条顶层棱的定向（1=白色在 U 面）；</li>
 *   <li>bits 4–11：4 个顶层角各 2 bit，编码角块扭转方向（00=白色在U，01=白色在sideA，10=白色在sideB）。</li>
 * </ul>
 * 比 8-bit 方案多捕获了角块的具体扭转方向，消除 Sune/Antisune 等情形的指纹冲突。</p>
 *
 * <p>若查表结果施加后 OLL 仍未解决（极少数剩余冲突），则返回 null，交给 2-look 兜底。</p>
 */
final class OllRecognizer {

    private static final Map<Integer, int[]> TABLE = new HashMap<>(512);

    private static final int U_CW  = FaceCube.U1;
    private static final int U_CCW = FaceCube.U3;

    // 顶层四角的 facelet 下标：[U面, sideA, sideB]
    // UFR: U=s[8], R-side=s[11], F-side=s[20]
    // UFL: U=s[6], F-side=s[18], L-side=s[38]
    // UBR: U=s[2], R-side=s[9],  B-side=s[45]
    // UBL: U=s[0], L-side=s[36], B-side=s[47]
    private static final int[][] CORNER_IDX = {
        {8, 11, 20},   // UFR
        {6, 18, 38},   // UFL
        {2,  9, 45},   // UBR
        {0, 36, 47},   // UBL
    };

    // 顶层四棱在 U 面的 facelet 下标
    private static final int[] EDGE_IDX = {7, 5, 1, 3}; // UF, UR, UB, UL

    static {
        buildTable();
    }

    private static void buildTable() {
        for (String algStr : Algorithms.OLL) {
            int[] alg    = MoveSeq.parse(algStr);
            int[] invAlg = MoveSeq.invert(alg);

            for (int auf = 0; auf < 4; auf++) {
                FaceCube fc = new FaceCube(); // 已还原
                fc.apply(invAlg);
                for (int i = 0; i < auf; i++) fc.apply(U_CCW);

                int fp = fingerprint(fc);
                if (fp == 0 || TABLE.containsKey(fp)) continue;

                int[] solve = new int[auf + alg.length];
                for (int i = 0; i < auf; i++) solve[i] = U_CW;
                System.arraycopy(alg, 0, solve, auf, alg.length);
                int[] simplified = MoveSeq.simplify(solve);

                // 自验证：OLL 解决 且 F2L 未被破坏
                FaceCube check = fc.copy();
                check.apply(simplified);
                if (!check.isOLLSolved() || !check.isF2LSolved()) continue;

                TABLE.put(fp, simplified);
            }
        }
    }

    /**
     * 识别当前 OLL 情形并应用解法。
     * 命中且验证通过 → 施加解法并返回；未命中或验证失败 → 返回 null（交给 2-look 兜底）。
     */
    static int[] tryRecognize(FaceCube fc) {
        if (fc.isOLLSolved()) return new int[0];

        int fp = fingerprint(fc);
        int[] seq = TABLE.get(fp);
        if (seq == null) return null;

        // 安全验证：在副本上确认解法真的解决了 OLL
        FaceCube test = fc.copy();
        test.apply(seq);
        if (!test.isOLLSolved()) return null;

        fc.apply(seq);
        return seq;
    }

    static int tableSize() { return TABLE.size(); }

    /**
     * 12-bit OLL 指纹：
     *   bits 0-3  = 4 棱（白色在U为1）
     *   bits 4-11 = 4 角各2bit（00=白U，01=白sideA，10=白sideB）
     */
    static int fingerprint(FaceCube fc) {
        int fp = 0;
        for (int i = 0; i < 4; i++) {
            if (fc.s[EDGE_IDX[i]] == 0) fp |= (1 << i);
        }
        for (int i = 0; i < 4; i++) {
            int[] c = CORNER_IDX[i];
            int bits;
            if      (fc.s[c[0]] == 0) bits = 0b00;
            else if (fc.s[c[1]] == 0) bits = 0b01;
            else                      bits = 0b10;
            fp |= (bits << (4 + i * 2));
        }
        return fp;
    }
}
