package com.temp.cube.solver.engine;

import com.temp.cube.constants.Algorithms;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * PLL 公式表覆盖性测试：保证 {@link Algorithms#PLL} 的 21 条标准公式恰好对应 21 种 PLL 情形，
 * 全部旋转中性（不残留整方块转向、保持下两层），且 {@link PllPhase} 对每种情形都能完全还原。
 * 同时校验采用标准 ergonomic 记法——不出现别扭的 B 面转动。
 */
public class PllCoverageTest {

    private static final int[][] ALGS = MoveSeq.parseAll(Algorithms.PLL);
    private static final int[] SIDE_TOP = {9, 10, 11, 18, 19, 20, 36, 37, 38, 45, 46, 47};
    private static final int[] CENTERS = {4, 13, 22, 31, 40, 49};

    /** 公式表恰好 21 条。 */
    @Test
    public void testExactly21Algs() {
        assertTrue("PLL 表应为 21 条标准公式，实际 " + ALGS.length, ALGS.length == 21);
    }

    /** 不含 B 面转动（允许 x/y/z 转体与 M 中层）。 */
    @Test
    public void testNoBackFaceMoves() {
        for (String alg : Algorithms.PLL) {
            for (String tok : alg.split("\\s+")) {
                assertTrue("PLL 公式不应含 B 面转动: " + alg, tok.isEmpty() || tok.charAt(0) != 'B');
            }
        }
    }

    /** 每条公式都旋转中性（施加到还原态后六个中心归位、下两层保持）。 */
    @Test
    public void testAllNeutral() {
        for (String alg : Algorithms.PLL) {
            FaceCube fc = new FaceCube();
            fc.apply(MoveSeq.parse(alg));
            for (int c : CENTERS) {
                assertTrue("公式残留整方块转向（中心错位）: " + alg, fc.s[c] == c / 9);
            }
            assertTrue("公式破坏了下两层: " + alg, fc.isF2LSolved());
        }
    }

    /** 21 条公式恰好覆盖 21 种不同 PLL 情形，且 PllPhase 对每种都能完全还原。 */
    @Test
    public void testCoversAll21Cases() {
        Set<String> cases = new HashSet<>();
        for (int[] alg : ALGS) {
            for (int auf = 0; auf < 4; auf++) {
                FaceCube st = new FaceCube();
                st.apply(MoveSeq.invert(alg));
                for (int i = 0; i < auf; i++) st.apply(FaceCube.U1);
                if (st.isSolved()) continue;
                cases.add(caseFp(st));

                FaceCube work = st.copy();
                work.apply(new PllPhase().solve(work.copy()));
                assertTrue("PllPhase 未能完全还原某 PLL 情形", work.isSolved());
            }
        }
        assertTrue("应恰好覆盖 21 种 PLL 情形，实际 " + cases.size(), cases.size() == 21);
    }

    /** PLL 情形指纹：12 个侧面顶行贴纸颜色，按 4 次 AUF 取字典序最小（AUF 无关）。 */
    private static String caseFp(FaceCube fc) {
        String best = null;
        FaceCube t = fc.copy();
        for (int auf = 0; auf < 4; auf++) {
            StringBuilder sb = new StringBuilder();
            for (int i : SIDE_TOP) sb.append((char) ('0' + t.s[i]));
            String s = sb.toString();
            if (best == null || s.compareTo(best) < 0) best = s;
            t.apply(FaceCube.U1);
        }
        return best;
    }
}
