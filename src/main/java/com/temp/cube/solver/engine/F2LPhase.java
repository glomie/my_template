package com.temp.cube.solver.engine;

import com.temp.cube.constants.SolverConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * CFOP 第二步：F2L。把 4 个 角+中棱 对插入下两层槽位，达到 isF2LSolved（下两层全还原）。
 *
 * <p>黄十字已在底面、顶层(白)是空闲的最后一层，故全程不转 D，插入手法天然是 R U R' 这类“RUL 流”。
 * 每个槽先“仅本槽两侧面 + U”浅搜（relabel 后纯 RUF），不行再放宽到非 D；输出对每个槽包装成
 * 「y 转体到前右 + RUF 手法 + y 转体回正」，使解法符合常规 CFOP 的转体习惯。</p>
 *
 * <p>启发：对所有“应当保持/还原”的块取单块 god 距离最大值，并叠加当前 pair 的联合距离，
 * 一旦破坏已完成部分则 f 升高被剪枝，搜索快且不破坏。</p>
 */
final class F2LPhase {

    /** 当前正在插入的 pair（用于联合启发）；-1 表示无。 */
    private int activePair = -1;

    int[] solve(FaceCube fc) {
        List<Integer> all = new ArrayList<>();
        List<Integer> reqEdges = new ArrayList<>();
        List<Integer> reqCorners = new ArrayList<>();
        for (int e : Slots.DOWN_EDGES) reqEdges.add(e); // 黄十字 4 棱必须保持

        Search.Heuristic h = st -> {
            int max = 0;
            for (int e : reqEdges) max = Math.max(max, st.edgeDistance(e));
            for (int c : reqCorners) max = Math.max(max, st.cornerDistance(c));
            if (activePair >= 0) max = Math.max(max, Slots.jointPairDistance(st, activePair));
            return max;
        };

        for (int p = 0; p < 4; p++) {
            int cs = Slots.PAIRS[p][0];
            reqCorners.add(cs);
            reqEdges.add(Slots.PAIRS[p][1]);
            if (Slots.allSolved(fc, reqCorners, reqEdges)) continue;
            activePair = p;
            int[] goal = Slots.goalFacelets(reqCorners, reqEdges);
            int[] moves = Search.ida(fc, goal, h, SolverConfig.F2L_SLOT_RESTRICTED_DEPTH, Slots.slotMask(cs));
            if (moves == null) moves = Search.ida(fc, goal, h, SolverConfig.F2L_SLOT_FULL_DEPTH, SolverConfig.noDown());
            activePair = -1;
            if (moves != null) appendSlot(all, moves, cs);
        }
        return MoveSeq.simplify(MoveSeq.toIntArray(all));
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
