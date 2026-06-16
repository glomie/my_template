package com.temp.cube.solver.engine;

import com.temp.cube.constants.SolverConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * CFOP 第二步：F2L。把 4 个 角+中棱 对插入下两层槽位，达到 isF2LSolved（下两层全还原）。
 *
 * <p>黄十字已在底面、顶层(白)是空闲的最后一层，故全程不转 D，也<b>不做整把转体 y</b>——
 * 整个 F2L 保持同一朝向，每个槽就地求解，而不是先把槽转到右前方再 {@code R U R'}。
 * 这样输出贴近高手习惯：右侧槽（FR/BR）用 R/U 流，左侧槽（FL/BL）用 L/U 流，
 * 4 组几乎只用到 R、U、L 三个面。</p>
 *
 * <p>每个槽分两阶段：
 * <ol>
 *   <li><b>调整到标态</b>：把当前 角+中棱 搜到一个「再补一个基本触发公式即可入槽」的标态
 *       （优先只用 R/U/L 浅搜，不行再放宽到非 D 全面）；</li>
 *   <li><b>标准入槽</b>：套用与该标态匹配的同侧手基本触发公式。</li>
 * </ol>
 * 只有当纯 R/U/L 在给定深度内确实凑不出标态时，才放宽到含 F/B 触发兜底——尽量减少 F/B 出现。</p>
 *
 * <p>启发：对所有“应当保持/还原”的块取单块 god 距离最大值，并叠加当前 pair 的联合距离（标态阶段
 * 再扣掉一个触发公式的长度），一旦破坏已完成部分则 f 升高被剪枝。每个槽的结果都会在副本上验证
 * （入槽且不破坏已完成块）才采用，验证不过则回退到一次性整段搜索，故两阶段绝不会解错。</p>
 */
final class F2LPhase {

    /** 右侧槽（FR/BR）的标准入槽触发：只用 R、U 面。 */
    private static final int[][] R_TRIGS = MoveSeq.parseAll(
            "R U R'", "R U' R'", "R U2 R'",
            "R' U R", "R' U' R", "R' U2 R");
    /** 左侧槽（FL/BL）的标准入槽触发：只用 L、U 面（右手触发的镜像）。 */
    private static final int[][] L_TRIGS = MoveSeq.parseAll(
            "L' U' L", "L' U L", "L' U2 L",
            "L U L'", "L U' L'", "L U2 L'");
    /** 兜底：极少数情形才用到的含 F/B 触发。 */
    private static final int[][] FB_TRIGS = MoveSeq.parseAll(
            "F' U' F", "F' U F", "F' U2 F",
            "B U B'", "B U' B'", "B U2 B'");

    /** 触发公式的最大长度，用于标态阶段启发函数的可容纳修正。 */
    private static final int MAX_TRIG = 3;

    /** 当前正在插入的 pair（用于联合启发）；-1 表示无。 */
    private int activePair = -1;

    int[] solve(FaceCube fc) {
        List<Integer> all = new ArrayList<>();
        List<Integer> reqEdges = new ArrayList<>();
        List<Integer> reqCorners = new ArrayList<>();
        for (int e : Slots.DOWN_EDGES) reqEdges.add(e); // 黄十字 4 棱必须保持

        for (int p = 0; p < 4; p++) {
            int cs = Slots.PAIRS[p][0];
            reqCorners.add(cs);
            reqEdges.add(Slots.PAIRS[p][1]);
            if (Slots.allSolved(fc, reqCorners, reqEdges)) continue;

            int[] moves = solveSlot(fc, p, cs, reqCorners, reqEdges);
            for (int m : moves) all.add(m); // 就地求解，无转体，直接拼接真实朝向手序
        }
        return MoveSeq.simplify(MoveSeq.toIntArray(all));
    }

    /**
     * 求解单个槽（就地、不转体）：右侧槽用 R/U 触发、左侧槽用 L/U 触发；先只用 R/U/L 面 setup，
     * 不行再放宽到非 D 全面，仍只用同侧手触发；再不行才加 F/B 触发兜底；两阶段彻底失败回退整段搜索。
     * 返回真实朝向（fixed frame）的 token 序列，并已施加到 fc。
     */
    private int[] solveSlot(FaceCube fc, int p, int cs,
                            List<Integer> reqCorners, List<Integer> reqEdges) {
        activePair = p;
        boolean rightSide = FaceCube.CORNER_POS[cs][0] > 0; // x>0 → 右侧槽用 R 流，否则左侧用 L 流
        int[][] goodTrigs = rightSide ? R_TRIGS : L_TRIGS;
        boolean[] rul = rulMask();
        boolean[] full = SolverConfig.noDown();

        // 0 档：只用 R/U/L 面 setup + 同侧手标准触发，输出纯 R/U/L、零转体。
        int[] result = twoStage(fc, reqCorners, reqEdges, rul,
                SolverConfig.F2L_SLOT_RESTRICTED_DEPTH, goodTrigs);
        // 1 档：放宽 setup 到非 D 全面，仍只用同侧手标准触发。
        if (result == null) {
            result = twoStage(fc, reqCorners, reqEdges, full,
                    SolverConfig.F2L_SETUP_FULL_DEPTH, goodTrigs);
        }
        // 2 档：极少数情形才放宽到含 F/B 触发兜底。
        if (result == null) {
            int[][] allTrigs = concatTrigs(goodTrigs, FB_TRIGS);
            result = twoStage(fc, reqCorners, reqEdges, full,
                    SolverConfig.F2L_SETUP_FULL_DEPTH, allTrigs);
        }
        // 3 档：两阶段彻底失败，回退一次性整段搜索（保证不会解错/解不出）。
        if (result == null) {
            result = directSolve(fc, cs, reqCorners, reqEdges);
        }
        activePair = -1;
        return result == null ? new int[0] : result;
    }

    /** F2L setup 阶段优先允许的面：只放行 R、U、L（面顺序 U,R,F,D,L,B）。 */
    private static boolean[] rulMask() {
        return new boolean[]{true, true, false, false, true, false};
    }

    /**
     * 两阶段：先 idaGoal 搜到「标态」（再补一个给定触发即可入槽且不破坏已完成块），
     * 再挑最短的可入槽触发拼接，副本验证通过后施加到 fc。触发已是真实朝向，无需再做坐标变换。
     */
    private int[] twoStage(FaceCube fc,
                           List<Integer> reqCorners, List<Integer> reqEdges,
                           boolean[] allowed, int maxDepth, int[][] trigs) {
        Search.Heuristic h = st -> {
            int max = 0;
            for (int e : reqEdges) max = Math.max(max, st.edgeDistance(e));
            for (int c : reqCorners) max = Math.max(max, st.cornerDistance(c));
            if (activePair >= 0) {
                max = Math.max(max, Math.max(0, Slots.jointPairDistance(st, activePair) - MAX_TRIG));
            }
            return max;
        };
        Search.Goal goal = st -> completingTrigger(st, trigs, reqCorners, reqEdges) != null;

        FaceCube probe = fc.copy();
        int[] setup = Search.idaGoal(probe, goal, h, maxDepth, allowed);
        if (setup == null) return null;

        int[] trig = completingTrigger(probe, trigs, reqCorners, reqEdges);
        if (trig == null) return null; // 理论不可达

        int[] seq = concat(setup, trig);
        FaceCube verify = fc.copy();
        verify.apply(seq);
        if (!Slots.allSolved(verify, reqCorners, reqEdges)) return null;
        fc.apply(seq);
        return seq;
    }

    /** 在 st 上找一个施加后即可入槽且不破坏已完成块的最短触发；找不到返回 null。 */
    private static int[] completingTrigger(FaceCube st, int[][] trigs,
                                           List<Integer> reqCorners, List<Integer> reqEdges) {
        int[] best = null;
        for (int[] t : trigs) {
            FaceCube c = st.copy();
            c.apply(t);
            if (Slots.allSolved(c, reqCorners, reqEdges) && (best == null || t.length < best.length)) {
                best = t;
            }
        }
        return best;
    }

    /** 一次性整段搜索（旧策略）作为兜底：直接搜到该槽完全入槽。成功时已施加到 fc。 */
    private int[] directSolve(FaceCube fc, int cs, List<Integer> reqCorners, List<Integer> reqEdges) {
        Search.Heuristic h = st -> {
            int max = 0;
            for (int e : reqEdges) max = Math.max(max, st.edgeDistance(e));
            for (int c : reqCorners) max = Math.max(max, st.cornerDistance(c));
            if (activePair >= 0) max = Math.max(max, Slots.jointPairDistance(st, activePair));
            return max;
        };
        int[] goal = Slots.goalFacelets(reqCorners, reqEdges);
        int[] moves = Search.ida(fc, goal, h, SolverConfig.F2L_SLOT_RESTRICTED_DEPTH, Slots.slotMask(cs));
        if (moves == null) moves = Search.ida(fc, goal, h, SolverConfig.F2L_SLOT_FULL_DEPTH, SolverConfig.noDown());
        return moves;
    }

    private static int[][] concatTrigs(int[][] a, int[][] b) {
        int[][] r = new int[a.length + b.length][];
        System.arraycopy(a, 0, r, 0, a.length);
        System.arraycopy(b, 0, r, a.length, b.length);
        return r;
    }

    private static int[] concat(int[] a, int[] b) {
        int[] r = new int[a.length + b.length];
        System.arraycopy(a, 0, r, 0, a.length);
        System.arraycopy(b, 0, r, a.length, b.length);
        return r;
    }
}
