package com.temp.cube.solver.engine;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 在 {@link FaceCube} 上实现 CFOP 四个阶段的求解，返回内部 move 编号序列。
 *
 * <ul>
 *   <li>Cross：IDA*，启发=4个白棱单块god距离的最大值。</li>
 *   <li>F2L：先逐个插入4个底棱（黄十字），再逐个插入4个 角+中棱 对（IDA*，启发=该对的联合god距离）。
 *       每一步都把已还原的贴纸锁定为目标，保证不破坏。</li>
 *   <li>OLL：对 {U, Sune, Antisune} 三个宏操作做迭代加深，直到顶面全白。</li>
 *   <li>PLL：对 {U, U', A-perm, U-perm} 宏操作做迭代加深，直到完全还原。</li>
 * </ul>
 */
public final class CfopEngine {

    // ---- 槽位分类（静态推导） ----
    private static final int[] CROSS_EDGES;   // up 层 4 白棱
    private static final int[] DOWN_EDGES;    // down 层 4 棱
    private static final int[][] PAIRS;       // 4 组 {角槽, 中棱槽}

    // ---- 每个 pair（角+中棱）的联合 god 距离表，作强启发 ----
    @SuppressWarnings("unchecked")
    private static final Map<Long, Integer>[] PAIR_DIST = new Map[4];

    // ---- 末层宏操作（内部 move 序列） ----
    private static final int[] SUNE      = parse("R U R' U R U2 R'");
    private static final int[] ANTISUNE  = parse("R U2 R' U' R U' R'");
    private static final int[] A_PERM    = parse("R' F R' B2 R F' R' B2 R2"); // 纯角块3循环
    private static final int[] U_PERM    = parse("R U' R U R U R U' R' U' R2"); // 纯棱块3循环
    private static final int[] EO_CROSS  = parse("F R U R' U' F'");            // 翻顶棱(保持下两层)
    private static final int U_MOVE  = FaceCube.U1;
    private static final int U_PRIME = FaceCube.U3;

    static {
        List<Integer> cross = new ArrayList<>();
        List<Integer> down = new ArrayList<>();
        List<Integer> downCorners = new ArrayList<>();
        for (int s = 0; s < 12; s++) {
            int y = FaceCube.EDGE_POS[s][1];
            if (y == 1) cross.add(s);
            else if (y == -1) down.add(s);
        }
        for (int s = 0; s < 8; s++) {
            if (FaceCube.CORNER_POS[s][1] == -1) downCorners.add(s);
        }
        CROSS_EDGES = toIntArray(cross);
        DOWN_EDGES = toIntArray(down);

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
    }

    // ============================ 公开求解入口 ============================

    /** 解白十字，返回 move 序列并施加到 fc。 */
    public int[] solveCross(FaceCube fc) {
        if (fc.isCrossSolved()) return new int[0];
        int[] goal = facelets(CROSS_EDGES, true);
        int[] result = ida(fc, goal, st -> crossHeuristic(st), 8);
        // ida 成功时已把解法施加到 fc，无需再 apply
        return simplify(result == null ? new int[0] : result);
    }

    /**
     * 解 F2L，最终达到 isF2LSolved（下两层全还原 + 白十字）。
     *
     * <p>策略：把魔方当作“黄色作底色”的标准 CFOP——先用 U 面作为自由缓冲层，逐块组好
     * 黄色底层十字、四个 角+中棱 对（下两层）；此过程会破坏白十字（无所谓）。最后再用
     * 保持下两层的宏操作重建 up 面白十字。这样每步搜索都有自由缓冲层、解很短，A* 极快。</p>
     *
     * <p>启发函数对“所有应当还原的块”取单块god距离最大值：一旦破坏已还原块，f 立即升高被剪枝。</p>
     */
    public int[] solveF2L(FaceCube fc) {
        List<Integer> all = new ArrayList<>();
        List<Integer> reqEdges = new ArrayList<>();
        List<Integer> reqCorners = new ArrayList<>();

        // 启发：已放置块的最大单块距离（惩罚破坏） 与 当前正在插入的 pair 联合距离 取最大
        Heuristic h = st -> {
            int max = 0;
            for (int e : reqEdges) max = Math.max(max, st.edgeDistance(e));
            for (int c : reqCorners) max = Math.max(max, st.cornerDistance(c));
            if (activePair >= 0) max = Math.max(max, jointPairDistance(st, activePair));
            return max;
        };

        // 1) 黄色底层十字：4 个底棱一次性求解（U 面自由，与白十字同理 ≤8 步）
        activePair = -1;
        for (int e : DOWN_EDGES) reqEdges.add(e);
        if (!allSolved(fc, reqCorners, reqEdges)) {
            int[] moves = ida(fc, goalFacelets(reqCorners, reqEdges), h, 8);
            if (moves != null) for (int m : moves) all.add(m);
        }

        // 2) 四个 角+中棱 对（下两层完成），U 面自由
        for (int p = 0; p < 4; p++) {
            reqCorners.add(PAIRS[p][0]);
            reqEdges.add(PAIRS[p][1]);
            if (allSolved(fc, reqCorners, reqEdges)) { continue; }
            activePair = p;
            int[] moves = ida(fc, goalFacelets(reqCorners, reqEdges), h, 18);
            activePair = -1;
            if (moves != null) for (int m : moves) all.add(m);
        }

        // 3) 重建白十字（保持下两层），用宏操作：U 调整 / 翻棱 / 棱3循环
        if (!fc.isCrossSolved()) {
            int[][] macros = {{U_MOVE}, {U_PRIME}, EO_CROSS, U_PERM};
            int[] crossMoves = macroSearch(fc, macros, 12, FaceCube::isCrossSolved);
            if (crossMoves != null) for (int m : crossMoves) all.add(m);
        }
        return simplify(toIntArray(all));
    }

    private boolean allSolved(FaceCube fc, List<Integer> corners, List<Integer> edges) {
        for (int e : edges) for (int f : FaceCube.EDGE_SLOTS[e]) if (!fc.correct(f)) return false;
        for (int c : corners) for (int f : FaceCube.CORNER_SLOTS[c]) if (!fc.correct(f)) return false;
        return true;
    }

    private int[] goalFacelets(List<Integer> corners, List<Integer> edges) {
        List<Integer> list = new ArrayList<>();
        for (int e : edges) for (int f : FaceCube.EDGE_SLOTS[e]) list.add(f);
        for (int c : corners) for (int f : FaceCube.CORNER_SLOTS[c]) list.add(f);
        return toIntArray(list);
    }

    /** OLL：顶面全白。 */
    public int[] solveOLL(FaceCube fc) {
        if (fc.isOLLSolved()) return new int[0];
        int[][] macros = {{U_MOVE}, SUNE, ANTISUNE};
        int[] result = macroSearch(fc, macros, 10, st -> st.isOLLSolved());
        return simplify(result == null ? new int[0] : result);
    }

    /** PLL：完全还原。 */
    public int[] solvePLL(FaceCube fc) {
        if (fc.isSolved()) return new int[0];
        int[][] macros = {{U_MOVE}, {U_PRIME}, A_PERM, U_PERM};
        int[] result = macroSearch(fc, macros, 12, st -> st.isSolved());
        return simplify(result == null ? new int[0] : result);
    }

    // ============================ IDA* （cross / F2L） ============================

    private interface Heuristic { int of(FaceCube fc); }
    private interface Goal { boolean ok(FaceCube fc); }

    private static final int[] OPPOSITE = {3, 4, 5, 0, 1, 2}; // U-D,R-L,F-B

    /** 当前正在插入的 pair（用于联合启发）；-1 表示无。 */
    private int activePair = -1;

    /**
     * IDA*（深度优先迭代加深，内存占用 O(depth)，不会 OOM）。
     * 启发函数可容纳；成功时把解法保留在 fc 上并返回 move 序列。
     */
    private static final int TT_CAP = 2_000_000;

    private int[] ida(FaceCube fc, int[] goalFacelets, Heuristic h, int maxDepth) {
        if (goalReached(fc, goalFacelets)) return new int[0];
        for (int limit = h.of(fc); limit <= maxDepth; limit++) {
            List<Integer> path = new ArrayList<>();
            // 置换表：state -> 已失败时的最大剩余预算（剪掉重复且不更深的再探索）
            java.util.HashMap<String, Integer> seen = new java.util.HashMap<>();
            if (dfs(fc, goalFacelets, h, limit, 0, -1, path, seen)) {
                return toIntArray(path);
            }
        }
        return null;
    }

    private boolean dfs(FaceCube fc, int[] goal, Heuristic h, int limit, int g,
                        int lastFace, List<Integer> path, java.util.HashMap<String, Integer> seen) {
        if (goalReached(fc, goal)) return true;
        int remaining = limit - g;
        if (h.of(fc) > remaining) return false;

        String k = stateKey(fc.s);
        Integer prev = seen.get(k);
        if (prev != null && prev >= remaining) return false; // 已用≥预算探索过且失败
        if (seen.size() < TT_CAP) seen.put(k, remaining);

        for (int face = 0; face < 6; face++) {
            if (face == lastFace) continue;                              // 不连续转同一面
            if (lastFace == OPPOSITE[face] && face < lastFace) continue; // 相对面只按固定顺序
            for (int a = 0; a < 3; a++) {
                int m = face * 3 + a;
                fc.apply(m);
                path.add(m);
                if (dfs(fc, goal, h, limit, g + 1, face, path, seen)) return true;
                path.remove(path.size() - 1);
                fc.apply(inverse(m));
            }
        }
        return false;
    }

    private static String stateKey(byte[] s) {
        char[] c = new char[54];
        for (int i = 0; i < 54; i++) c[i] = (char) ('0' + s[i]);
        return new String(c);
    }

    private boolean goalReached(FaceCube fc, int[] goalFacelets) {
        for (int f : goalFacelets) if (!fc.correct(f)) return false;
        return true;
    }

    private int crossHeuristic(FaceCube fc) {
        int max = 0;
        for (int e : CROSS_EDGES) max = Math.max(max, fc.edgeDistance(e));
        return max;
    }

    // ============================ 宏操作迭代加深（OLL / PLL） ============================

    /**
     * 宏操作 BFS（带 visited，状态空间小，秒级内可达）。每个宏都保持下两层，
     * 故可达状态只是末层的有限排列；BFS 找到最短宏序列后展开成 move 序列并施加到 fc。
     */
    private int[] macroSearch(FaceCube fc, int[][] macros, int maxMacroDepth, Goal goal) {
        if (goal.ok(fc)) return new int[0];

        FaceCube tmp = new FaceCube();
        ArrayDeque<byte[]> queue = new ArrayDeque<>();
        java.util.HashMap<String, String> parent = new java.util.HashMap<>();
        java.util.HashMap<String, Integer> viaMacro = new java.util.HashMap<>();
        java.util.HashMap<String, Integer> depth = new java.util.HashMap<>();

        byte[] start = fc.s.clone();
        String startKey = stateKey(start);
        queue.add(start);
        parent.put(startKey, null);
        depth.put(startKey, 0);

        while (!queue.isEmpty()) {
            byte[] cur = queue.poll();
            String curKey = stateKey(cur);
            int d = depth.get(curKey);
            if (d >= maxMacroDepth) continue;
            for (int mi = 0; mi < macros.length; mi++) {
                tmp.load(cur);
                tmp.apply(macros[mi]);
                String nk = stateKey(tmp.s);
                if (parent.containsKey(nk)) continue;
                parent.put(nk, curKey);
                viaMacro.put(nk, mi);
                depth.put(nk, d + 1);
                if (goal.ok(tmp)) {
                    int[] flat = reconstructMacros(nk, parent, viaMacro, macros);
                    fc.apply(flat);
                    return flat;
                }
                queue.add(tmp.s.clone());
            }
        }
        return null;
    }

    private int[] reconstructMacros(String goalKey, java.util.HashMap<String, String> parent,
                                    java.util.HashMap<String, Integer> viaMacro, int[][] macros) {
        List<Integer> macroSeq = new ArrayList<>();
        for (String k = goalKey; parent.get(k) != null; k = parent.get(k)) {
            macroSeq.add(viaMacro.get(k));
        }
        List<Integer> flat = new ArrayList<>();
        for (int i = macroSeq.size() - 1; i >= 0; i--) {
            for (int m : macros[macroSeq.get(i)]) flat.add(m);
        }
        return toIntArray(flat);
    }

    // ============================ 工具 ============================

    private static int inverse(int m) {
        int face = m / 3, a = m % 3;
        int ia = (a == 0) ? 2 : (a == 2 ? 0 : 1);
        return face * 3 + ia;
    }

    private int[] facelets(int[] edgeSlots, boolean edge) {
        List<Integer> list = new ArrayList<>();
        for (int s : edgeSlots) for (int f : FaceCube.EDGE_SLOTS[s]) list.add(f);
        return toIntArray(list);
    }

    /** 解析公式字符串为内部 move 序列。 */
    static int[] parse(String alg) {
        List<Integer> moves = new ArrayList<>();
        for (String tok : alg.trim().split("\\s+")) {
            if (tok.isEmpty()) continue;
            int face;
            switch (tok.charAt(0)) {
                case 'U': face = 0; break;
                case 'R': face = 1; break;
                case 'F': face = 2; break;
                case 'D': face = 3; break;
                case 'L': face = 4; break;
                case 'B': face = 5; break;
                default: continue;
            }
            int a = 0; // 90 CW
            if (tok.contains("2")) a = 1;
            else if (tok.contains("'")) a = 2;
            moves.add(face * 3 + a);
        }
        return toIntArray(moves);
    }

    private int jointPairDistance(FaceCube fc, int pair) {
        int[] cpos = fc.currentCornerPositions(PAIRS[pair][0]);
        int[] epos = fc.currentEdgePositions(PAIRS[pair][1]);
        long key = 0;
        for (int p : cpos) key = key * 54 + p;
        for (int p : epos) key = key * 54 + p;
        Integer d = PAIR_DIST[pair].get(key);
        return d == null ? 0 : d;
    }

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

    /**
     * 简化手序：合并相邻同面转动（按 1/4 圈相加 mod 4），消除多余步骤。
     * 例如 U U U → U'、R R R R → 无、R' R → 无。不改变整体效果。
     */
    static int[] simplify(int[] moves) {
        ArrayList<int[]> stack = new ArrayList<>(); // 每项 {face, quarter(1..3)}
        for (int m : moves) {
            int face = m / 3, q = m % 3 + 1;
            if (!stack.isEmpty() && stack.get(stack.size() - 1)[0] == face) {
                int nq = (stack.get(stack.size() - 1)[1] + q) % 4;
                stack.remove(stack.size() - 1);
                if (nq != 0) stack.add(new int[]{face, nq});
            } else {
                stack.add(new int[]{face, q});
            }
        }
        int[] out = new int[stack.size()];
        for (int i = 0; i < out.length; i++) out[i] = stack.get(i)[0] * 3 + (stack.get(i)[1] - 1);
        return out;
    }

    private static int[] toIntArray(List<Integer> list) {
        int[] a = new int[list.size()];
        for (int i = 0; i < a.length; i++) a[i] = list.get(i);
        return a;
    }
}
