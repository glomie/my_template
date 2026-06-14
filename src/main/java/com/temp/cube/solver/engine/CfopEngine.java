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
    private static final int[] DOWN_EDGES;    // down 层 4 棱（黄十字）
    private static final int[][] PAIRS;       // 4 组 {角槽, 中棱槽}
    private static final boolean[] ALL_FACES = {true, true, true, true, true, true};
    private static final boolean[] NO_D = {true, true, true, false, true, true}; // F2L 不转 D

    /** 一次 y-CW 后，面 f 移动到的面（U->U,R->F,F->L,D->D,L->B,B->R）。 */
    private static final int[] FACE_IMG_Y = new int[6];

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

    /** OCLL（顶棱已定向后，定向 4 个顶角）的标准公式集（纯面转）。 */
    private static final int[][] OCLL_ALGS = parseAll(
        "R U R' U R U2 R'",                  // Sune
        "R U2 R' U' R U' R'",                // Antisune
        "R U2 R2 U' R2 U' R2 U2 R",          // Pi
        "R U2 R' U' R U R' U' R U' R'",      // H
        "R2 D R' U2 R D' R' U2 R'",          // U (headlights)
        "R U R' U' R' F R F'",               // T
        "F' R U R' U' R' F R"                // L
    );

    /** PLL 标准公式集（纯面转，含 D 但不含 M/wide/旋转）。验证门控保证错误公式只会被跳过。 */
    private static final int[][] PLL_ALGS = parseAll(
        "R' F R' B2 R F' R' B2 R2",                                  // Aa
        "R B' R F2 R' B R F2 R2",                                    // Ab
        "R U' R U R U R U' R' U' R2",                                // Ua
        "R2 U R U R' U' R' U' R' U R'",                              // Ub
        "R U R' U' R' F R2 U' R' U' R U R' F'",                      // T
        "F R U' R' U' R U R' F' R U R' U' R' F R F'",                // Y
        "R' U L' U2 R U' R' U2 R L",                                 // Ja
        "R U R' F' R U R' U' R' F R2 U' R'",                         // Jb
        "R U R' F' R U2 R' U2 R' F R U R U2 R' U'",                  // Ra
        "R' U2 R U2 R' F R U R' U' R' F' R2",                        // Rb
        "R' U' F' R U R' U' R' F R2 U' R' U' R U R' U R",            // F
        "R' U R' U' R D' R' D R' U D' R2 U' R2 D R2",                // V
        "R U R' U R U R' F' R U R' U' R' F R2 U' R' U2 R U' R'",     // Na
        "R' U R U' R' F' U' F R U R' F R' F' R U' R",                // Nb
        "R2 U R' U R' U' R U' R2 U' D R' U R D'",                    // Ga
        "R' U' R U D' R2 U R' U R U' R U' R2 D",                     // Gb
        "R2 U' R U' R U R' U R2 U D' R U' R' D",                     // Gc
        "R U R' U' D R2 U' R U' R' U R' U R2 D'",                    // Gd
        "R B' R' F R B R' F' R B R' F R B' R' F'"                    // E
    );

    static {
        List<Integer> down = new ArrayList<>();
        List<Integer> downCorners = new ArrayList<>();
        for (int s = 0; s < 12; s++) {
            if (FaceCube.EDGE_POS[s][1] == -1) down.add(s);
        }
        for (int s = 0; s < 8; s++) {
            if (FaceCube.CORNER_POS[s][1] == -1) downCorners.add(s);
        }
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

        // y 转体的面映射：旋转后 center 落到的位置即该面的新位置
        for (int g = 0; g < 6; g++) FACE_IMG_Y[FaceCube.ROT_Y[g * 9 + 4] / 9] = g;
    }

    // ============================ 公开求解入口 ============================

    /** 解黄色十字（底面 down），返回 move 序列并施加到 fc。 */
    public int[] solveCross(FaceCube fc) {
        if (fc.isCrossSolved()) return new int[0];
        int[] goal = facelets(DOWN_EDGES, true);
        int[] result = ida(fc, goal, st -> crossHeuristic(st), 8, ALL_FACES);
        // ida 成功时已把解法施加到 fc，无需再 apply
        return simplify(result == null ? new int[0] : result);
    }

    /**
     * 解 F2L：把 4 个 角+中棱 对插入下两层的槽位，达到 isF2LSolved（下两层全还原）。
     *
     * <p>此时黄十字已在底面、顶层(白)是空闲的最后一层，所以 F2L 全程不需要转动 D 面
     * （禁用 D），插入手法自然是 R U R' / L U L' 这类“RUL 流”。启发函数对所有“应当
     * 还原的块”取单块god距离最大值，并叠加当前 pair 的联合距离，搜索快且不破坏已完成部分。</p>
     */
    public int[] solveF2L(FaceCube fc) {
        List<Integer> all = new ArrayList<>();
        List<Integer> reqEdges = new ArrayList<>();
        List<Integer> reqCorners = new ArrayList<>();
        // 黄十字 4 棱必须保持
        for (int e : DOWN_EDGES) reqEdges.add(e);

        Heuristic h = st -> {
            int max = 0;
            for (int e : reqEdges) max = Math.max(max, st.edgeDistance(e));
            for (int c : reqCorners) max = Math.max(max, st.cornerDistance(c));
            if (activePair >= 0) max = Math.max(max, jointPairDistance(st, activePair));
            return max;
        };

        for (int p = 0; p < 4; p++) {
            int cs = PAIRS[p][0];
            reqCorners.add(cs);
            reqEdges.add(PAIRS[p][1]);
            if (allSolved(fc, reqCorners, reqEdges)) continue;
            activePair = p;
            int[] goal = goalFacelets(reqCorners, reqEdges);
            // 先只用该槽自身的两个侧面 + U 浅搜（relabel 后纯 R U F）；不行再放宽到非 D。
            int[] moves = ida(fc, goal, h, 8, slotMask(cs));
            if (moves == null) moves = ida(fc, goal, h, 18, NO_D);
            activePair = -1;
            if (moves != null) appendSlot(all, moves, cs);
        }
        return simplify(toIntArray(all));
    }

    /** 该角槽自身的两个侧面 + U 的允许掩码。 */
    private static boolean[] slotMask(int cornerSlot) {
        int x = FaceCube.CORNER_POS[cornerSlot][0], z = FaceCube.CORNER_POS[cornerSlot][2];
        int fx = x > 0 ? 1 : 4;  // R / L
        int fz = z > 0 ? 2 : 5;  // F / B
        boolean[] mask = new boolean[6];
        mask[0] = true;          // U
        mask[fx] = true;
        mask[fz] = true;
        return mask;
    }

    /**
     * 把某槽的解法（固定朝向 face token）包装成「转体 + RUF 手法 + 转体回正」加入 all。
     * 通过 y^p 把该槽转到前右(FR)，相应把面 relabel 成 R/F，使输出符合 RUL 流并显式带转体。
     */
    private void appendSlot(List<Integer> all, int[] moves, int cornerSlot) {
        int x = FaceCube.CORNER_POS[cornerSlot][0], z = FaceCube.CORNER_POS[cornerSlot][2];
        int p = slotRotation(x, z); // 把该槽转到 FR 所需的 y-CW 次数
        if (p == 0) {
            for (int m : moves) all.add(m);
            return;
        }
        all.add(MoveCodec.ROT_Y_BASE + (p - 1));          // y^p
        for (int m : moves) {
            int face = m / 3, amt = m % 3;
            for (int i = 0; i < p; i++) face = FACE_IMG_Y[face];
            all.add(face * 3 + amt);                       // relabel 后的面手法
        }
        int back = (4 - p) % 4;
        all.add(MoveCodec.ROT_Y_BASE + (back - 1));        // y^(4-p) 回正
    }

    /** 槽位 (x,z) 转到前右 FR(+1,+1) 所需 y-CW 次数：FR0, BR1, BL2, FL3。 */
    private static int slotRotation(int x, int z) {
        if (x == 1 && z == 1) return 0;    // FR
        if (x == 1 && z == -1) return 1;   // BR
        if (x == -1 && z == -1) return 2;  // BL
        return 3;                          // FL (-1,1)
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

    /**
     * OLL（2-look）：先翻棱做出顶面十字，再用一条 OCLL 标准公式定向四角。
     * OCLL 用「枚举 AUF × 公式、在副本上验证是否顶面全白」的方式选用——录错公式只会被跳过。
     * 若公式集未覆盖（极少），回退到 {U,Sune,Antisune} 宏搜索保证可解。
     */
    public int[] solveOLL(FaceCube fc) {
        if (fc.isOLLSolved()) return new int[0];
        List<Integer> all = new ArrayList<>();

        // 1) 顶棱定向：翻棱宏到顶面十字
        if (!fc.isTopCrossSolved()) {
            int[] eo = macroSearch(fc, new int[][]{{U_MOVE}, EO_CROSS}, 8, FaceCube::isTopCrossSolved);
            if (eo != null) for (int m : eo) all.add(m);
        }
        // 2) 顶角定向：OCLL 公式（验证门控） + 宏兜底
        int[] oc = tryAlgs(fc, OCLL_ALGS, FaceCube::isOLLSolved, false);
        if (oc == null) oc = macroSearch(fc, new int[][]{{U_MOVE}, SUNE, ANTISUNE}, 12, FaceCube::isOLLSolved);
        if (oc != null) for (int m : oc) all.add(m);

        return simplify(toIntArray(all));
    }

    /**
     * PLL：用一条标准公式（枚举前后 AUF，在副本上验证是否完全还原）一次排列末层。
     * 录错或缺失的公式只会被跳过；未命中时回退到纯角/纯棱 3 循环宏搜索保证可解。
     */
    public int[] solvePLL(FaceCube fc) {
        if (fc.isSolved()) return new int[0];
        int[] pll = tryAlgs(fc, PLL_ALGS, FaceCube::isSolved, true);
        if (pll == null) {
            pll = macroSearch(fc, new int[][]{{U_MOVE}, {U_PRIME}, A_PERM, U_PERM}, 12, FaceCube::isSolved);
        }
        return simplify(pll == null ? new int[0] : pll);
    }

    /**
     * 依次尝试 (前 AUF) × (公式) × (后 AUF)，在副本上验证 goal；第一组达成的就施加到 fc 并返回其 token。
     * 因为只有真正达成目标才采用，所以公式表里写错的条目只会被跳过、不会解错。
     */
    private int[] tryAlgs(FaceCube fc, int[][] algs, Goal goal, boolean postAuf) {
        int posts = postAuf ? 4 : 1;
        for (int pre = 0; pre < 4; pre++) {
            for (int[] alg : algs) {
                for (int post = 0; post < posts; post++) {
                    FaceCube t = fc.copy();
                    for (int i = 0; i < pre; i++) t.apply(U_MOVE);
                    t.apply(alg);
                    for (int i = 0; i < post; i++) t.apply(U_MOVE);
                    if (goal.ok(t)) {
                        List<Integer> seq = new ArrayList<>();
                        for (int i = 0; i < pre; i++) seq.add(U_MOVE);
                        for (int m : alg) seq.add(m);
                        for (int i = 0; i < post; i++) seq.add(U_MOVE);
                        int[] arr = toIntArray(seq);
                        fc.apply(arr);
                        return arr;
                    }
                }
            }
        }
        return null;
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

    private int[] ida(FaceCube fc, int[] goalFacelets, Heuristic h, int maxDepth, boolean[] allowed) {
        if (goalReached(fc, goalFacelets)) return new int[0];
        for (int limit = h.of(fc); limit <= maxDepth; limit++) {
            List<Integer> path = new ArrayList<>();
            // 置换表：state -> 已失败时的最大剩余预算（剪掉重复且不更深的再探索）
            java.util.HashMap<String, Integer> seen = new java.util.HashMap<>();
            if (dfs(fc, goalFacelets, h, limit, 0, -1, path, seen, allowed)) {
                return toIntArray(path);
            }
        }
        return null;
    }

    private boolean dfs(FaceCube fc, int[] goal, Heuristic h, int limit, int g,
                        int lastFace, List<Integer> path, java.util.HashMap<String, Integer> seen,
                        boolean[] allowed) {
        if (goalReached(fc, goal)) return true;
        int remaining = limit - g;
        if (h.of(fc) > remaining) return false;

        String k = stateKey(fc.s);
        Integer prev = seen.get(k);
        if (prev != null && prev >= remaining) return false; // 已用≥预算探索过且失败
        if (seen.size() < TT_CAP) seen.put(k, remaining);

        for (int face = 0; face < 6; face++) {
            if (!allowed[face]) continue;                                // 该阶段允许转动的面
            if (face == lastFace) continue;                              // 不连续转同一面
            if (lastFace == OPPOSITE[face] && face < lastFace) continue; // 相对面只按固定顺序
            for (int a = 0; a < 3; a++) {
                int m = face * 3 + a;
                fc.apply(m);
                path.add(m);
                if (dfs(fc, goal, h, limit, g + 1, face, path, seen, allowed)) return true;
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
        for (int e : DOWN_EDGES) max = Math.max(max, fc.edgeDistance(e));
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

    private static int[][] parseAll(String... algs) {
        int[][] out = new int[algs.length][];
        for (int i = 0; i < algs.length; i++) out[i] = parse(algs[i]);
        return out;
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
     * 简化手序：合并相邻同“通道”转动（面 0..5，或 y 转体=通道6），按 1/4 圈相加 mod 4。
     * 例如 U U U → U'、R R R R → 无、y2 y → y'。不改变整体效果。
     */
    static int[] simplify(int[] moves) {
        ArrayList<int[]> stack = new ArrayList<>(); // 每项 {channel, quarter(1..3)}
        for (int m : moves) {
            int channel, q;
            if (m >= MoveCodec.ROT_Y_BASE) { channel = 6; q = m - MoveCodec.ROT_Y_BASE + 1; }
            else { channel = m / 3; q = m % 3 + 1; }
            if (!stack.isEmpty() && stack.get(stack.size() - 1)[0] == channel) {
                int nq = (stack.get(stack.size() - 1)[1] + q) % 4;
                stack.remove(stack.size() - 1);
                if (nq != 0) stack.add(new int[]{channel, nq});
            } else {
                stack.add(new int[]{channel, q});
            }
        }
        int[] out = new int[stack.size()];
        for (int i = 0; i < out.length; i++) {
            int channel = stack.get(i)[0], q = stack.get(i)[1];
            out[i] = channel == 6 ? MoveCodec.ROT_Y_BASE + (q - 1) : channel * 3 + (q - 1);
        }
        return out;
    }

    private static int[] toIntArray(List<Integer> list) {
        int[] a = new int[list.size()];
        for (int i = 0; i < a.length; i++) a[i] = list.get(i);
        return a;
    }
}
