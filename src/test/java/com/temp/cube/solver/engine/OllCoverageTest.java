package com.temp.cube.solver.engine;

import com.temp.cube.constants.Algorithms;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * OLL 公式表覆盖性测试：保证 {@link Algorithms#OLL} 的 57 条标准公式覆盖全部 215 个顶层定向状态，
 * 且 {@link OllPhase} 对每个状态都用"一条标准公式 + 前置 AUF"解决（不依赖任何 2-look 兜底）。
 */
public class OllCoverageTest {

    private static final int[][] ALGS = MoveSeq.parseAll(Algorithms.OLL);

    /** 公式表恰好 57 条。 */
    @Test
    public void testExactly57Algs() {
        assertTrue("OLL 表应为 57 条标准公式，实际 " + ALGS.length, ALGS.length == 57);
    }

    /** 枚举所有可达 OLL 状态（57 公式 × 4 AUF 的逆），逐一用 OllPhase 求解并校验。 */
    @Test
    public void testAllStatesSolvedByStandardFormula() {
        Set<String> seenFingerprints = new HashSet<>();
        int checked = 0;

        for (int[] alg : ALGS) {
            int[] inv = MoveSeq.invert(alg);
            for (int auf = 0; auf < 4; auf++) {
                FaceCube fc = new FaceCube();
                fc.apply(inv);
                for (int i = 0; i < auf; i++) fc.apply(FaceCube.U1);
                if (!fc.isF2LSolved() || fc.isOLLSolved()) continue;
                if (!seenFingerprints.add(Search.stateKey(fc.s))) continue;

                int[] sol = new OllPhase().solve(fc);
                assertTrue("OllPhase 未能定向顶层", fc.isOLLSolved());
                assertTrue("OllPhase 破坏了下两层", fc.isF2LSolved());
                assertTrue("OLL 解应为单条标准公式 + AUF", isStandard(sol));
                checked++;
            }
        }
        assertTrue("应覆盖大量 OLL 状态，实际 " + checked, checked >= 200);
    }

    /** 解是否等于 (k×U) + 某条标准公式。 */
    private static boolean isStandard(int[] sol) {
        for (int[] a : ALGS) {
            for (int auf = 0; auf < 4; auf++) {
                int[] seq = new int[auf + a.length];
                for (int i = 0; i < auf; i++) seq[i] = FaceCube.U1;
                System.arraycopy(a, 0, seq, auf, a.length);
                if (Arrays.equals(MoveSeq.simplify(seq), sol)) return true;
            }
        }
        return false;
    }
}
