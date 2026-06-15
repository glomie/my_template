package com.temp.cube.solver.engine;

import com.temp.cube.constants.SolverConfig;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 求解搜索基础设施（与具体阶段无关）：
 * <ul>
 *   <li>{@link #ida} —— IDA*（迭代加深 + 置换表），用于 Cross / F2L。</li>
 *   <li>{@link #macroSearch} —— 宏操作 BFS，用于末层（保持下两层的有限状态空间）。</li>
 *   <li>{@link #tryAlgs} —— 公式枚举 + 副本验证 + 取最短，用于 OLL / PLL。</li>
 * </ul>
 */
final class Search {

    private Search() {}

    /** 启发函数：估计到目标的最少步数（需可容纳）。 */
    interface Heuristic { int of(FaceCube fc); }

    /** 目标判定。 */
    interface Goal { boolean ok(FaceCube fc); }

    private static final int U_MOVE = FaceCube.U1;

    // ============================ IDA* ============================

    /** 成功时把解法保留在 fc 上并返回 token 序列；找不到返回 null。 */
    static int[] ida(FaceCube fc, int[] goalFacelets, Heuristic h, int maxDepth, boolean[] allowed) {
        if (goalReached(fc, goalFacelets)) return new int[0];
        for (int limit = h.of(fc); limit <= maxDepth; limit++) {
            List<Integer> path = new ArrayList<>();
            HashMap<String, Integer> seen = new HashMap<>(); // state -> 失败时的最大剩余预算
            if (dfs(fc, goalFacelets, h, limit, 0, -1, path, seen, allowed)) {
                return MoveSeq.toIntArray(path);
            }
        }
        return null;
    }

    private static boolean dfs(FaceCube fc, int[] goal, Heuristic h, int limit, int g,
                               int lastFace, List<Integer> path, HashMap<String, Integer> seen,
                               boolean[] allowed) {
        if (goalReached(fc, goal)) return true;
        int remaining = limit - g;
        if (h.of(fc) > remaining) return false;

        String k = stateKey(fc.s);
        Integer prev = seen.get(k);
        if (prev != null && prev >= remaining) return false; // 已用≥预算探索过且失败
        if (seen.size() < SolverConfig.TT_CAP) seen.put(k, remaining);

        for (int face = 0; face < 6; face++) {
            if (!allowed[face]) continue;                                       // 该阶段允许的面
            if (face == lastFace) continue;                                     // 不连续转同一面
            if (lastFace == MoveSeq.OPPOSITE[face] && face < lastFace) continue; // 相对面固定顺序
            for (int a = 0; a < 3; a++) {
                int m = face * 3 + a;
                fc.apply(m);
                path.add(m);
                if (dfs(fc, goal, h, limit, g + 1, face, path, seen, allowed)) return true;
                path.remove(path.size() - 1);
                fc.apply(MoveSeq.inverse(m));
            }
        }
        return false;
    }

    static boolean goalReached(FaceCube fc, int[] goalFacelets) {
        for (int f : goalFacelets) if (!fc.correct(f)) return false;
        return true;
    }

    static String stateKey(byte[] s) {
        char[] c = new char[54];
        for (int i = 0; i < 54; i++) c[i] = (char) ('0' + s[i]);
        return new String(c);
    }

    // ============================ 宏操作 BFS ============================

    /** 对一组保持下两层的宏做 BFS，找到最短宏序列展开成 move 并施加到 fc。 */
    static int[] macroSearch(FaceCube fc, int[][] macros, int maxMacroDepth, Goal goal) {
        if (goal.ok(fc)) return new int[0];

        FaceCube tmp = new FaceCube();
        ArrayDeque<byte[]> queue = new ArrayDeque<>();
        HashMap<String, String> parent = new HashMap<>();
        HashMap<String, Integer> viaMacro = new HashMap<>();
        HashMap<String, Integer> depth = new HashMap<>();

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

    private static int[] reconstructMacros(String goalKey, HashMap<String, String> parent,
                                           HashMap<String, Integer> viaMacro, int[][] macros) {
        List<Integer> macroSeq = new ArrayList<>();
        for (String k = goalKey; parent.get(k) != null; k = parent.get(k)) {
            macroSeq.add(viaMacro.get(k));
        }
        List<Integer> flat = new ArrayList<>();
        for (int i = macroSeq.size() - 1; i >= 0; i--) {
            for (int m : macros[macroSeq.get(i)]) flat.add(m);
        }
        return MoveSeq.toIntArray(flat);
    }

    // ============================ 公式枚举（OLL/PLL） ============================

    /**
     * 依次尝试 (前 AUF) × (公式) × (后 AUF)，在副本上验证 goal，取标准记法最短的一组施加到 fc。
     * 只有真正达成目标才采用，所以公式表里写错的条目只会被跳过、不会解错。
     */
    static int[] tryAlgs(FaceCube fc, int[][] algs, Goal goal, boolean postAuf) {
        int posts = postAuf ? 4 : 1;
        int[] best = null;
        for (int pre = 0; pre < 4; pre++) {
            for (int[] alg : algs) {
                for (int post = 0; post < posts; post++) {
                    FaceCube t = fc.copy();
                    for (int i = 0; i < pre; i++) t.apply(U_MOVE);
                    t.apply(alg);
                    for (int i = 0; i < post; i++) t.apply(U_MOVE);
                    if (!goal.ok(t)) continue;
                    List<Integer> seq = new ArrayList<>();
                    for (int i = 0; i < pre; i++) seq.add(U_MOVE);
                    for (int m : alg) seq.add(m);
                    for (int i = 0; i < post; i++) seq.add(U_MOVE);
                    int[] cand = MoveSeq.simplify(MoveSeq.toIntArray(seq));
                    if (best == null || cand.length < best.length) best = cand;
                }
            }
        }
        if (best != null) fc.apply(best);
        return best;
    }
}
