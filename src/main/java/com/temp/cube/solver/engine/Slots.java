package com.temp.cube.solver.engine;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cross / F2L 用到的槽位几何与单块/联合 god 距离数据（由 {@link FaceCube} 的坐标静态推导）。
 *
 * <p>纯数据 + 查询，不含搜索逻辑：
 * <ul>
 *   <li>{@link #DOWN_EDGES}：底层 4 棱（黄十字）。</li>
 *   <li>{@link #PAIRS}：4 个 F2L 槽位 {角槽, 中棱槽}。</li>
 *   <li>{@link #FACE_IMG_Y}：一次 y 转体后各面的去向（relabel 用）。</li>
 *   <li>{@link #PAIR_DIST}：每个 pair 的联合 god 距离表（F2L 强启发）。</li>
 * </ul>
 */
final class Slots {

    private Slots() {}

    static final int[] DOWN_EDGES;
    static final int[][] PAIRS;
    static final int[] FACE_IMG_Y = new int[6];
    @SuppressWarnings("unchecked")
    static final Map<Long, Integer>[] PAIR_DIST = new Map[4];

    static {
        List<Integer> down = new ArrayList<>();
        List<Integer> downCorners = new ArrayList<>();
        for (int s = 0; s < 12; s++) {
            if (FaceCube.EDGE_POS[s][1] == -1) down.add(s);
        }
        for (int s = 0; s < 8; s++) {
            if (FaceCube.CORNER_POS[s][1] == -1) downCorners.add(s);
        }
        DOWN_EDGES = MoveSeq.toIntArray(down);

        PAIRS = new int[4][];
        int pi = 0;
        for (int cs : downCorners) {
            int x = FaceCube.CORNER_POS[cs][0], z = FaceCube.CORNER_POS[cs][2];
            int es = -1;
            for (int s = 0; s < 12; s++) {
                if (FaceCube.EDGE_POS[s][1] == 0
                        && FaceCube.EDGE_POS[s][0] == x && FaceCube.EDGE_POS[s][2] == z) {
                    es = s;
                    break;
                }
            }
            PAIRS[pi++] = new int[]{cs, es};
        }

        for (int p = 0; p < 4; p++) {
            int[] home = concat(FaceCube.CORNER_SLOTS[PAIRS[p][0]], FaceCube.EDGE_SLOTS[PAIRS[p][1]]);
            PAIR_DIST[p] = bfsGroup(home);
        }

        // y 转体的面映射：旋转后 center 落到的位置即该面的新位置
        for (int g = 0; g < 6; g++) FACE_IMG_Y[FaceCube.ROT_Y[g * 9 + 4] / 9] = g;
    }

    /** 某角槽自身的两个侧面 + U 的允许掩码。 */
    static boolean[] slotMask(int cornerSlot) {
        int x = FaceCube.CORNER_POS[cornerSlot][0], z = FaceCube.CORNER_POS[cornerSlot][2];
        int fx = x > 0 ? 1 : 4;  // R / L
        int fz = z > 0 ? 2 : 5;  // F / B
        boolean[] mask = new boolean[6];
        mask[0] = true;          // U
        mask[fx] = true;
        mask[fz] = true;
        return mask;
    }

    /** 槽位 (x,z) 转到前右 FR(+1,+1) 所需 y-CW 次数：FR0, BR1, BL2, FL3。 */
    static int slotRotation(int x, int z) {
        if (x == 1 && z == 1) return 0;    // FR
        if (x == 1 && z == -1) return 1;   // BR
        if (x == -1 && z == -1) return 2;  // BL
        return 3;                          // FL (-1,1)
    }

    /** 归属于该 pair 的 角+中棱 当前到归位的联合 god 距离。 */
    static int jointPairDistance(FaceCube fc, int pair) {
        int[] cpos = fc.currentCornerPositions(PAIRS[pair][0]);
        int[] epos = fc.currentEdgePositions(PAIRS[pair][1]);
        long key = 0;
        for (int p : cpos) key = key * 54 + p;
        for (int p : epos) key = key * 54 + p;
        Integer d = PAIR_DIST[pair].get(key);
        return d == null ? 0 : d;
    }

    /** 一组棱槽对应的全部贴纸下标。 */
    static int[] edgeFacelets(int[] edgeSlots) {
        List<Integer> list = new ArrayList<>();
        for (int s : edgeSlots) for (int f : FaceCube.EDGE_SLOTS[s]) list.add(f);
        return MoveSeq.toIntArray(list);
    }

    /** 给定要保持/还原的角槽与棱槽，返回它们的全部贴纸下标（作为搜索目标）。 */
    static int[] goalFacelets(List<Integer> corners, List<Integer> edges) {
        List<Integer> list = new ArrayList<>();
        for (int e : edges) for (int f : FaceCube.EDGE_SLOTS[e]) list.add(f);
        for (int c : corners) for (int f : FaceCube.CORNER_SLOTS[c]) list.add(f);
        return MoveSeq.toIntArray(list);
    }

    static boolean allSolved(FaceCube fc, List<Integer> corners, List<Integer> edges) {
        for (int e : edges) for (int f : FaceCube.EDGE_SLOTS[e]) if (!fc.correct(f)) return false;
        for (int c : corners) for (int f : FaceCube.CORNER_SLOTS[c]) if (!fc.correct(f)) return false;
        return true;
    }

    // ---- 联合距离表 BFS ----

    private static Map<Long, Integer> bfsGroup(int[] home) {
        Map<Long, Integer> dist = new HashMap<>();
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        dist.put(packL(home), 0);
        queue.add(home.clone());
        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            int d = dist.get(packL(cur));
            for (int m = 0; m < 18; m++) {
                int[] perm = FaceCube.MOVE[m];
                int[] next = new int[cur.length];
                for (int k = 0; k < cur.length; k++) next[k] = invPos(perm, cur[k]);
                long key = packL(next);
                if (!dist.containsKey(key)) {
                    dist.put(key, d + 1);
                    queue.add(next);
                }
            }
        }
        return dist;
    }

    private static int invPos(int[] perm, int p) {
        for (int i = 0; i < 54; i++) if (perm[i] == p) return i;
        return p;
    }

    private static long packL(int[] pos) {
        long key = 0;
        for (int p : pos) key = key * 54 + p;
        return key;
    }

    private static int[] concat(int[] a, int[] b) {
        int[] r = new int[a.length + b.length];
        System.arraycopy(a, 0, r, 0, a.length);
        System.arraycopy(b, 0, r, a.length, b.length);
        return r;
    }
}
