package com.temp.cube.solver.engine;

import com.temp.cube.constants.SolverConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * CFOP 第二步：F2L。把 4 个 角+中棱 对插入下两层槽位，达到 isF2LSolved（下两层全还原）。
 *
 * <p>黄十字已在底面、顶层(白)是空闲的最后一层，故全程不转 D。求解贴近高手习惯，核心思想：
 * <b>转体是“有目的”的工具，而非每组必做</b>——每个槽优先在<i>当前朝向</i>就地求解，
 * 只有当当前朝向凑不出纯 R/U/L 解时，才插入一次转体把该槽转到顺手的朝向再做。</p>
 *
 * <p>每个槽分两阶段：
 * <ol>
 *   <li><b>调整到标态</b>：用「该朝向下会打印成 R/U/L 的那 3 个真实面」浅搜，搜到一个
 *       「再补一个基本触发即可入槽且不破坏已完成块」的标态；</li>
 *   <li><b>标准入槽</b>：套用与该标态匹配的旋转坐标系下的基本触发（R U R' / R' U' R / L U L' / L' U L 流）。</li>
 * </ol>
 * 由于 setup 与触发都落在「会打印成 R/U/L」的面上，输出永远是纯 R/U/L（外加少量有目的的 y 转体）。
 * 关键技巧：在 odd 朝向下，真实 F/B 面恰好会打印成 R/L——于是“转一下再用 F/B 做 setup”
 * 在记法上就是“顺手的 R/U/L”，这正是用转体换掉 F/B 的原理。</p>
 *
 * <p>朝向在多个槽之间累积：只在「为这个槽选定的朝向」与「当前朝向」不同时才插入差值转体，
 * 整个 F2L 结束后再补一次把朝向归零，使后续 OLL/PLL 的面记法保持正确。
 * 候选朝向按「转体代价」排序（当前朝向 0 代价优先，其次该槽的标准 FR 朝向），故转体数量被最小化。</p>
 *
 * <p>每个槽的结果都会在副本上验证（入槽且不破坏已完成块）才采用，验证不过则回退一次性整段搜索，
 * 故绝不会解错。</p>
 */
final class F2LPhase {

    /**
     * 旋转坐标系下的基本入槽触发，涵盖 4 个旋转位（FR/BR/BL/FL），全部只用 R/U/L：
     * completingTrigger 会挑出真正能把该槽入槽的那一个。
     */
    private static final int[][] ROT_TRIGS = MoveSeq.parseAll(
            "R U R'", "R U' R'", "R U2 R'",   // 旋转后 FR
            "R' U R", "R' U' R", "R' U2 R",   // 旋转后 BR
            "L U L'", "L U' L'", "L U2 L'",   // 旋转后 BL
            "L' U L", "L' U' L", "L' U2 L");  // 旋转后 FL

    /** 触发公式的最大长度，用于标态阶段启发函数的可容纳修正。 */
    private static final int MAX_TRIG = 3;

    /** 当前正在插入的 pair（用于联合启发）；-1 表示无。 */
    private int activePair = -1;
    /** solveSlot 选定并使用的朝向，供 appendSlot relabel。 */
    private int lastP = 0;

    int[] solve(FaceCube fc) {
        List<Integer> all = new ArrayList<>();
        List<Integer> reqEdges = new ArrayList<>();
        List<Integer> reqCorners = new ArrayList<>();
        for (int e : Slots.DOWN_EDGES) reqEdges.add(e); // 黄十字 4 棱必须保持

        int curRot = 0; // 当前累积朝向（0..3），跨槽只插入差值转体
        for (int p = 0; p < 4; p++) {
            int cs = Slots.PAIRS[p][0];
            reqCorners.add(cs);
            reqEdges.add(Slots.PAIRS[p][1]);
            if (Slots.allSolved(fc, reqCorners, reqEdges)) continue;

            int[] moves = solveSlot(fc, p, cs, curRot, reqCorners, reqEdges);
            if (moves.length > 0) curRot = appendSlot(all, moves, lastP, curRot);
        }
        if (curRot != 0) all.add(MoveCodec.ROT_Y_BASE + ((4 - curRot) % 4 - 1)); // 归正
        return MoveSeq.simplify(MoveSeq.toIntArray(all));
    }

    /**
     * 求解单个槽：在候选朝向里挑一个能凑出纯 R/U/L 解的（优先当前朝向以省转体）；
     * 都不行才放宽到含 F/B 的整段搜索兜底。返回真实朝向(fixed frame) token 序列并已施加到 fc，
     * 选定的朝向记在 {@link #lastP}。
     */
    private int[] solveSlot(FaceCube fc, int p, int cs, int curRot,
                            List<Integer> reqCorners, List<Integer> reqEdges) {
        activePair = p;
        int slotRot = Slots.slotRotation(FaceCube.CORNER_POS[cs][0], FaceCube.CORNER_POS[cs][2]);
        int[] cand = candidateOrientations(curRot, slotRot);

        // A 档：每个候选朝向只用「会打印成 R/U/L 的 3 个真实面」+ 旋转触发浅搜，命中即纯 R/U/L。
        for (int q : cand) {
            int[] r = twoStage(fc, q, reqCorners, reqEdges, goodRealMask(q),
                    SolverConfig.F2L_SETUP_CLEAN_DEPTH, ROT_TRIGS);
            if (r != null) { lastP = q; activePair = -1; return r; }
        }
        // C 档：极少数情形回退一次性整段搜索（可能含 F/B），保证一定解得出、不解错。
        lastP = 0; // directSolve 直接输出真实朝向手序，不再 relabel
        int[] r = directSolve(fc, cs, reqCorners, reqEdges);
        activePair = -1;
        return r == null ? new int[0] : r;
    }

    /** 候选朝向按转体代价排序：当前朝向(0 代价)、该槽标准 FR 朝向、其余，去重。 */
    private static int[] candidateOrientations(int curRot, int slotRot) {
        int[] order = {curRot, slotRot, (slotRot + 1) % 4, (slotRot + 2) % 4, (slotRot + 3) % 4};
        int[] out = new int[4];
        int n = 0;
        for (int o : order) {
            boolean seen = false;
            for (int i = 0; i < n; i++) if (out[i] == o) { seen = true; break; }
            if (!seen) out[n++] = o;
        }
        int[] res = new int[n];
        System.arraycopy(out, 0, res, 0, n);
        return res;
    }

    /**
     * 该朝向下「会打印成 R/U/L」的 3 个真实面集合：U 恒含；rot 偶数时真实 R、L 会打印成 R/L，
     * rot 奇数时真实 F、B 会打印成 R/L（已用 FACE_IMG_Y relabel 实测确认）。面顺序 U,R,F,D,L,B。
     */
    private static boolean[] goodRealMask(int rot) {
        boolean[] m = new boolean[6];
        m[0] = true; // U
        if (rot % 2 == 0) { m[1] = true; m[4] = true; }   // R, L
        else              { m[2] = true; m[5] = true; }   // F, B
        return m;
    }

    /**
     * 两阶段：先 idaGoal 搜到「标态」（再补一个给定触发即可入槽且不破坏已完成块），
     * 再挑最短的可入槽触发拼接，副本验证通过后施加到 fc。触发以「该朝向(rot)的旋转坐标系」给出，
     * 先 toFixedFrame 转成真实朝向再用；setup 在真实朝向用 allowed 面集合搜索。
     */
    private int[] twoStage(FaceCube fc, int rot,
                           List<Integer> reqCorners, List<Integer> reqEdges,
                           boolean[] allowed, int maxDepth, int[][] trigs) {
        int[][] fixedTrigs = new int[trigs.length][];
        for (int i = 0; i < trigs.length; i++) fixedTrigs[i] = toFixedFrame(trigs[i], rot);

        Search.Heuristic h = st -> {
            int max = 0;
            for (int e : reqEdges) max = Math.max(max, st.edgeDistance(e));
            for (int c : reqCorners) max = Math.max(max, st.cornerDistance(c));
            if (activePair >= 0) {
                max = Math.max(max, Math.max(0, Slots.jointPairDistance(st, activePair) - MAX_TRIG));
            }
            return max;
        };
        Search.Goal goal = st -> completingTrigger(st, fixedTrigs, reqCorners, reqEdges) != null;

        FaceCube probe = fc.copy();
        int[] setup = Search.idaGoal(probe, goal, h, maxDepth, allowed);
        if (setup == null) return null;

        int[] trig = completingTrigger(probe, fixedTrigs, reqCorners, reqEdges);
        if (trig == null) return null; // 理论不可达

        int[] seq = concat(setup, trig);
        FaceCube verify = fc.copy();
        verify.apply(seq);
        if (!Slots.allSolved(verify, reqCorners, reqEdges)) return null;
        fc.apply(seq);
        return seq;
    }

    /** 在 st 上找一个施加后即可入槽且不破坏已完成块的最短触发；找不到返回 null。 */
    private static int[] completingTrigger(FaceCube st, int[][] fixedTrigs,
                                           List<Integer> reqCorners, List<Integer> reqEdges) {
        int[] best = null;
        for (int[] t : fixedTrigs) {
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

    /**
     * 把某朝向(rot)旋转坐标系下的触发 token 转成真实朝向 token：
     * {@link #appendSlot} 输出时会用 FACE_IMG_Y 施加 rot 次把真实朝向还原成该旋转坐标系记法，
     * 故此处取其逆——施加 (4-rot)%4 次 FACE_IMG_Y。转动圈数(amt)不变。
     */
    private static int[] toFixedFrame(int[] frMoves, int rot) {
        int times = (4 - (rot % 4)) % 4;
        int[] out = new int[frMoves.length];
        for (int i = 0; i < frMoves.length; i++) {
            int face = frMoves[i] / 3, amt = frMoves[i] % 3;
            for (int k = 0; k < times; k++) face = Slots.FACE_IMG_Y[face];
            out[i] = face * 3 + amt;
        }
        return out;
    }

    private static int[] concat(int[] a, int[] b) {
        int[] r = new int[a.length + b.length];
        System.arraycopy(a, 0, r, 0, a.length);
        System.arraycopy(b, 0, r, a.length, b.length);
        return r;
    }

    /**
     * 把某槽的解法（真实朝向 face token）加入 all：只插入「当前朝向 curRot → 该槽选定朝向 p」的差值转体，
     * 并把面 relabel 成该朝向旋转坐标系下的 R/U/L 记法。返回新的当前朝向 p。
     */
    private int appendSlot(List<Integer> all, int[] moves, int p, int curRot) {
        int delta = (p - curRot + 4) % 4;
        if (delta != 0) all.add(MoveCodec.ROT_Y_BASE + (delta - 1));
        for (int m : moves) {
            int face = m / 3, amt = m % 3;
            for (int i = 0; i < p; i++) face = Slots.FACE_IMG_Y[face];
            all.add(face * 3 + amt);
        }
        return p;
    }
}
