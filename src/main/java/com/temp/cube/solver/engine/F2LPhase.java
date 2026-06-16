package com.temp.cube.solver.engine;

import com.temp.cube.constants.SolverConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * CFOP 第二步：F2L。把 4 个 角+中棱 对插入下两层槽位，达到 isF2LSolved（下两层全还原）。
 *
 * <p>黄十字已在底面、顶层(白)是空闲的最后一层，故全程不转 D。求解贴近人类习惯，分两阶段：
 * <ol>
 *   <li><b>调整到标态</b>：把当前 角+中棱 搜到一个「再补一个基本触发公式即可入槽」的标态
 *       （仅用本槽两侧面 + U 浅搜，不行再放宽到非 D）；</li>
 *   <li><b>标准入槽</b>：套用与该标态匹配的基本触发公式（{@code R U R'} / {@code R U' R'} /
 *       {@code F' U' F} 等）。</li>
 * </ol>
 * 输出对每个槽包装成「y 转体到前右 + RUF 手法 + y 转体回正」，使解法符合常规 CFOP 转体习惯，
 * 末尾天然呈现「若干步调整 + 一个标准触发」的人类记忆块。</p>
 *
 * <p>启发：对所有“应当保持/还原”的块取单块 god 距离最大值，并叠加当前 pair 的联合距离（标态阶段
 * 再扣掉一个触发公式的长度），一旦破坏已完成部分则 f 升高被剪枝。每个槽的结果都会在副本上验证
 * （入槽且不破坏已完成块）才采用，验证不过则回退到一次性整段搜索，故两阶段绝不会解错。</p>
 */
final class F2LPhase {

    /** 前右(FR)槽的标准“入槽触发”公式（右手 RUR' 流 + 左手 F'U'F 流）。 */
    private static final int[][] INSERTS = MoveSeq.parseAll(
            "R U R'", "R U' R'", "R U2 R'",
            "F' U' F", "F' U F", "F' U2 F"
    );
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

            int rot = Slots.slotRotation(FaceCube.CORNER_POS[cs][0], FaceCube.CORNER_POS[cs][2]);
            int[] moves = solveSlot(fc, p, cs, rot, reqCorners, reqEdges);
            if (moves.length > 0) appendSlot(all, moves, cs);
        }
        return MoveSeq.simplify(MoveSeq.toIntArray(all));
    }

    /**
     * 求解单个槽：优先两阶段（调整到标态 + 标准触发），失败再回退一次性整段搜索。
     * 返回固定朝向（fixed frame）的 token 序列，并已施加到 fc。
     */
    private int[] solveSlot(FaceCube fc, int p, int cs, int rot,
                            List<Integer> reqCorners, List<Integer> reqEdges) {
        activePair = p;
        int[] result = twoStage(fc, p, rot, reqCorners, reqEdges,
                Slots.slotMask(cs), SolverConfig.F2L_SLOT_RESTRICTED_DEPTH);
        if (result == null) {
            result = twoStage(fc, p, rot, reqCorners, reqEdges,
                    SolverConfig.noDown(), SolverConfig.F2L_SETUP_FULL_DEPTH);
        }
        if (result == null) {
            result = directSolve(fc, cs, reqCorners, reqEdges);
        }
        activePair = -1;
        return result == null ? new int[0] : result;
    }

    /**
     * 两阶段：先 idaGoal 搜到「标态」（再补一个基本触发即可入槽且不破坏已完成块），
     * 再挑最短的可入槽触发拼接，副本验证通过后施加到 fc。
     */
    private int[] twoStage(FaceCube fc, int p, int rot,
                           List<Integer> reqCorners, List<Integer> reqEdges,
                           boolean[] allowed, int maxDepth) {
        int[][] fixedTrigs = new int[INSERTS.length][];
        for (int i = 0; i < INSERTS.length; i++) fixedTrigs[i] = toFixedFrame(INSERTS[i], rot);

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
     * 把 FR(前右)朝向的触发公式 token 转成实际槽位所在的固定朝向 token：
     * {@link #appendSlot} 输出时会用 FACE_IMG_Y 施加 rot 次把固定朝向还原成 FR 记法，
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
     * 把某槽的解法（固定朝向 face token）包装成「y^p + RUF 手法 + y^(4-p)」加入 all：
     * 用 y^p 把该槽转到前右(FR)，并把面 relabel 成 R/F，使输出符合 RUL 流并显式带转体。
     */
    private void appendSlot(List<Integer> all, int[] moves, int cornerSlot) {
        int x = FaceCube.CORNER_POS[cornerSlot][0], z = FaceCube.CORNER_POS[cornerSlot][2];
        int p = Slots.slotRotation(x, z);
        if (p == 0) {
            for (int m : moves) all.add(m);
            return;
        }
        all.add(MoveCodec.ROT_Y_BASE + (p - 1));      // y^p
        for (int m : moves) {
            int face = m / 3, amt = m % 3;
            for (int i = 0; i < p; i++) face = Slots.FACE_IMG_Y[face];
            all.add(face * 3 + amt);                   // relabel 后的面手法
        }
        all.add(MoveCodec.ROT_Y_BASE + ((4 - p) % 4 - 1)); // y^(4-p) 回正
    }
}
