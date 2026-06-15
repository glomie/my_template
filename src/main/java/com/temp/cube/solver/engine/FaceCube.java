package com.temp.cube.solver.engine;

import com.temp.cube.model.Cube;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

/**
 * 内部高速魔方表示（54贴纸facelet模型），用于CFOP求解。
 *
 * <p>面顺序固定为 U,R,F,D,L,B（编号0..5），每个面的贴纸编号 index = face*9 + row*3 + col，
 * 与 {@link Cube#getOutputArray} 的 [row][col] 完全对应。贴纸的颜色用其在还原态所属的面编号表示
 * （0=白U,1=红R,2=绿F,3=黄D,4=橙L,5=蓝B），因此 index 在还原态的颜色恒为 index/9。</p>
 *
 * <p>所有转动排列都由三维坐标推导，physical 语义与 {@link Cube} 的 turn 完全一致
 * （已通过单步对照验证：CLOCKWISE = 从该面外侧看顺时针）。</p>
 */
public final class FaceCube {

    /** 转动编号：move = face*3 + (amount-1)，face 顺序 U,R,F,D,L,B，amount 1=90°,2=180°,3=270°。 */
    public static final int U1 = 0, U2 = 1, U3 = 2;
    public static final int R1 = 3, R2 = 4, R3 = 5;
    public static final int F1 = 6, F2 = 7, F3 = 8;
    public static final int D1 = 9, D2 = 10, D3 = 11;
    public static final int L1 = 12, L2 = 13, L3 = 14;
    public static final int B1 = 15, B2 = 16, B3 = 17;

    /** 18个转动作用在54贴纸上的位置排列：applyMove 后 new[i] = old[perm[i]]。 */
    static final int[][] MOVE = new int[18][54];

    /** 每个贴纸的三维坐标与法向（用于构造）。 */
    private static final int[][] POS = new int[54][3];
    private static final int[][] NORMAL = new int[54][3];

    /** 角块/棱块槽位（home贴纸下标）以及它们的三维位置。 */
    static final int[][] CORNER_SLOTS = new int[8][];
    static final int[][] EDGE_SLOTS = new int[12][];
    static final int[][] CORNER_POS = new int[8][];
    static final int[][] EDGE_POS = new int[12][];

    /** 单块到归位的god距离表，key = 贴纸当前位置打包。 */
    @SuppressWarnings("unchecked")
    static final Map<Integer, Integer>[] CORNER_DIST = new Map[8];
    @SuppressWarnings("unchecked")
    static final Map<Integer, Integer>[] EDGE_DIST = new Map[12];

    /** y 转体（整方块绕竖轴顺时针 90°）的贴纸排列：new[i] = old[ROT_Y[i]]。 */
    static final int[] ROT_Y = new int[54];

    static {
        buildCoordinates();
        buildMoves();
        buildRotations();
        buildSlots();
        buildDistanceTables();
    }

    /** 当前状态：54个贴纸颜色（0..5）。 */
    public final byte[] s = new byte[54];
    private final byte[] scratch = new byte[54];

    public FaceCube() {
        for (int i = 0; i < 54; i++) s[i] = (byte) (i / 9);
    }

    private FaceCube(byte[] state) {
        System.arraycopy(state, 0, s, 0, 54);
    }

    public FaceCube copy() {
        return new FaceCube(s);
    }

    /** 用给定贴纸数组覆盖当前状态。 */
    public void load(byte[] state) {
        System.arraycopy(state, 0, s, 0, 54);
    }

    /** 颜色名 → 面编号。 */
    private static int colorIndex(String name) {
        switch (name) {
            case "WHITE":  return 0; // U
            case "RED":    return 1; // R
            case "GREEN":  return 2; // F
            case "YELLOW": return 3; // D
            case "ORANGE": return 4; // L
            case "BLUE":   return 5; // B
            default: throw new IllegalArgumentException("unknown color: " + name);
        }
    }

    /** 从 model.Cube 导入当前状态。 */
    public static FaceCube fromCube(Cube cube) {
        FaceCube fc = new FaceCube();
        fill(fc, 0, cube.getUpSide().getOutputArray());
        fill(fc, 1, cube.getRightSide().getOutputArray());
        fill(fc, 2, cube.getFrontSide().getOutputArray());
        fill(fc, 3, cube.getDownSide().getOutputArray());
        fill(fc, 4, cube.getLeftSide().getOutputArray());
        fill(fc, 5, cube.getBackSide().getOutputArray());
        return fc;
    }

    private static void fill(FaceCube fc, int face, String[][] arr) {
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++)
                fc.s[face * 9 + r * 3 + c] = (byte) colorIndex(arr[r][c]);
    }

    /** 原地施加一个转动。 */
    public void apply(int move) {
        int[] perm = MOVE[move];
        for (int i = 0; i < 54; i++) scratch[i] = s[perm[i]];
        System.arraycopy(scratch, 0, s, 0, 54);
    }

    public void apply(int[] moves) {
        for (int m : moves) apply(m);
    }

    /** 施加 y 转体（整方块）顺时针 times 次。 */
    public void applyRotY(int times) {
        for (int t = 0; t < (times % 4 + 4) % 4; t++) {
            for (int i = 0; i < 54; i++) scratch[i] = s[ROT_Y[i]];
            System.arraycopy(scratch, 0, s, 0, 54);
        }
    }

    /** 该贴纸是否处于还原色。 */
    public boolean correct(int index) {
        return s[index] == index / 9;
    }

    // ---------------- CFOP 阶段状态判定（与 CubeStateChecker 完全一致） ----------------

    /** 常规：黄十字在 down 面（底），4 棱与相邻面 center 对齐。 */
    public boolean isCrossSolved() {
        return s[28] == 3 && s[34] == 3 && s[30] == 3 && s[32] == 3  // down 四棱黄
            && s[25] == 2   // front[2][1] green
            && s[16] == 1   // right[2][1] red
            && s[52] == 5   // back[2][1] blue
            && s[43] == 4;  // left[2][1] orange
    }

    /** F2L完成：下两层还原（down 全黄 + 四侧面底两行）。顶层(白)留给 OLL/PLL。 */
    public boolean isF2LSolved() {
        for (int i = 27; i < 36; i++) if (s[i] != 3) return false; // down 全黄
        int[] faces = {2, 1, 5, 4};      // F,R,B,L
        for (int k = 0; k < 4; k++) {
            int base = faces[k] * 9;
            for (int idx = 3; idx < 9; idx++) if (s[base + idx] != faces[k]) return false;
        }
        return true;
    }

    public boolean isOLLSolved() {
        for (int i = 0; i < 9; i++) if (s[i] != 0) return false;
        return true;
    }

    /** 顶棱定向完成：up 面四条棱为白（顶面十字，不要求对齐）。 */
    public boolean isTopCrossSolved() {
        return s[1] == 0 && s[3] == 0 && s[5] == 0 && s[7] == 0;
    }

    public boolean isSolved() {
        for (int i = 0; i < 54; i++) if (s[i] != i / 9) return false;
        return true;
    }

    // ---------------- 距离启发 ----------------

    /** 归属于角槽 slot 的角块当前到归位的下界距离。 */
    int cornerDistance(int slot) {
        Integer d = CORNER_DIST[slot].get(pack(currentCornerPositions(slot)));
        return d == null ? 0 : d;
    }

    int edgeDistance(int slot) {
        Integer d = EDGE_DIST[slot].get(pack(currentEdgePositions(slot)));
        return d == null ? 0 : d;
    }

    /** 归属于角槽 slot 的角块当前所在的 3 个贴纸位置（按 home 颜色顺序）。 */
    public int[] currentCornerPositions(int slot) {
        return currentPositions(CORNER_SLOTS, CORNER_SLOTS[slot]);
    }

    /** 归属于棱槽 slot 的棱块当前所在的 2 个贴纸位置（按 home 颜色顺序）。 */
    public int[] currentEdgePositions(int slot) {
        return currentPositions(EDGE_SLOTS, EDGE_SLOTS[slot]);
    }

    /**
     * 找到“本应归位到 home 槽”的那个块当前所在的位置，按 home 各贴纸颜色顺序返回。
     */
    private int[] currentPositions(int[][] allSlots, int[] home) {
        int n = home.length;
        int[] targetColors = new int[n];
        for (int k = 0; k < n; k++) targetColors[k] = home[k] / 9;

        for (int[] slot : allSlots) {
            if (slot.length != n) continue;
            if (sameColorSet(slot, targetColors)) {
                int[] pos = new int[n];
                for (int k = 0; k < n; k++) pos[k] = facletWithColor(slot, targetColors[k]);
                return pos;
            }
        }
        return home.clone(); // 理论不可达
    }

    private boolean sameColorSet(int[] slot, int[] colors) {
        // 集合相等（贴纸颜色互不相同）
        for (int color : colors) {
            boolean found = false;
            for (int idx : slot) if (s[idx] == color) { found = true; break; }
            if (!found) return false;
        }
        return true;
    }

    private int facletWithColor(int[] slot, int color) {
        for (int idx : slot) if (s[idx] == color) return idx;
        return slot[0];
    }

    private static int pack(int[] pos) {
        int key = 0;
        for (int p : pos) key = key * 54 + p;
        return key;
    }

    // ---------------- 静态构造 ----------------

    private static void buildCoordinates() {
        for (int f = 0; f < 6; f++) {
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    int idx = f * 9 + r * 3 + c;
                    int[] p, n;
                    switch (f) {
                        case 0: p = new int[]{c - 1, 1, r - 1};  n = new int[]{0, 1, 0};  break;  // U
                        case 1: p = new int[]{1, 1 - r, 1 - c};  n = new int[]{1, 0, 0};  break;  // R
                        case 2: p = new int[]{c - 1, 1 - r, 1};  n = new int[]{0, 0, 1};  break;  // F
                        case 3: p = new int[]{c - 1, -1, 1 - r}; n = new int[]{0, -1, 0}; break;  // D
                        case 4: p = new int[]{-1, 1 - r, c - 1}; n = new int[]{-1, 0, 0}; break;  // L
                        default:p = new int[]{1 - c, 1 - r, -1}; n = new int[]{0, 0, -1}; break;  // B
                    }
                    POS[idx] = p;
                    NORMAL[idx] = n;
                }
            }
        }
    }

    private static int encodePN(int[] p, int[] n) {
        int pe = ((p[0] + 1) * 3 + (p[1] + 1)) * 3 + (p[2] + 1);
        int ne = ((n[0] + 1) * 3 + (n[1] + 1)) * 3 + (n[2] + 1);
        return pe * 27 + ne;
    }

    private static int[] rotate(int face, int[] v) {
        int x = v[0], y = v[1], z = v[2];
        switch (face) {
            case 0: return new int[]{-z, y, x};   // U
            case 1: return new int[]{x, z, -y};   // R
            case 2: return new int[]{y, -x, z};   // F
            case 3: return new int[]{z, y, -x};   // D
            case 4: return new int[]{x, -z, y};   // L
            default:return new int[]{-y, x, z};   // B
        }
    }

    /** 该贴纸是否在 face 转动的层内。 */
    private static boolean inLayer(int face, int[] p) {
        switch (face) {
            case 0: return p[1] == 1;   // U: y=1
            case 1: return p[0] == 1;   // R: x=1
            case 2: return p[2] == 1;   // F: z=1
            case 3: return p[1] == -1;  // D: y=-1
            case 4: return p[0] == -1;  // L: x=-1
            default:return p[2] == -1;  // B: z=-1
        }
    }

    private static void buildMoves() {
        Map<Integer, Integer> lookup = new HashMap<>();
        for (int i = 0; i < 54; i++) lookup.put(encodePN(POS[i], NORMAL[i]), i);

        // 每个面的顺时针 90° 排列：cw[target] = source
        int[][] cw = new int[6][54];
        for (int face = 0; face < 6; face++) {
            for (int i = 0; i < 54; i++) {
                if (inLayer(face, POS[i])) {
                    int target = lookup.get(encodePN(rotate(face, POS[i]), rotate(face, NORMAL[i])));
                    cw[face][target] = i;
                } else {
                    cw[face][i] = i;
                }
            }
        }

        for (int face = 0; face < 6; face++) {
            int[] p1 = cw[face];
            int[] p2 = compose(p1, p1);
            int[] p3 = compose(p2, p1);
            MOVE[face * 3] = p1;
            MOVE[face * 3 + 1] = p2;
            MOVE[face * 3 + 2] = p3;
        }
    }

    /** 返回 a∘b：先按 b 取源，再按 a 取源；new[i] = old[result[i]]。 */
    private static int[] compose(int[] a, int[] b) {
        int[] r = new int[54];
        for (int i = 0; i < 54; i++) r[i] = b[a[i]];
        return r;
    }

    /** y 转体：整方块绕竖轴顺时针 90°，与 U 面同向（rotate(0,·)），但作用于全部贴纸。 */
    private static void buildRotations() {
        Map<Integer, Integer> lookup = new HashMap<>();
        for (int i = 0; i < 54; i++) lookup.put(encodePN(POS[i], NORMAL[i]), i);
        for (int i = 0; i < 54; i++) {
            int target = lookup.get(encodePN(rotate(0, POS[i]), rotate(0, NORMAL[i])));
            ROT_Y[target] = i;
        }
    }

    private static void buildSlots() {
        // 按三维位置分组 facelet
        Map<Integer, java.util.List<Integer>> groups = new HashMap<>();
        for (int i = 0; i < 54; i++) {
            int key = (POS[i][0] + 1) * 9 + (POS[i][1] + 1) * 3 + (POS[i][2] + 1);
            groups.computeIfAbsent(key, k -> new java.util.ArrayList<>()).add(i);
        }
        int ci = 0, ei = 0;
        for (Map.Entry<Integer, java.util.List<Integer>> e : groups.entrySet()) {
            java.util.List<Integer> g = e.getValue();
            if (g.size() == 3) {
                CORNER_SLOTS[ci] = toArray(g);
                CORNER_POS[ci] = POS[g.get(0)];
                ci++;
            } else if (g.size() == 2) {
                EDGE_SLOTS[ei] = toArray(g);
                EDGE_POS[ei] = POS[g.get(0)];
                ei++;
            }
        }
    }

    private static int[] toArray(java.util.List<Integer> list) {
        int[] a = new int[list.size()];
        for (int i = 0; i < a.length; i++) a[i] = list.get(i);
        return a;
    }

    private static void buildDistanceTables() {
        for (int s = 0; s < 8; s++) CORNER_DIST[s] = bfsPiece(CORNER_SLOTS[s]);
        for (int s = 0; s < 12; s++) EDGE_DIST[s] = bfsPiece(EDGE_SLOTS[s]);
    }

    /** 跟踪单块（home 各贴纸的位置）做 BFS，得到到归位的最短 HTM 距离。 */
    private static Map<Integer, Integer> bfsPiece(int[] home) {
        Map<Integer, Integer> dist = new HashMap<>();
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        dist.put(pack(home), 0);
        queue.add(home.clone());
        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            int d = dist.get(pack(cur));
            for (int m = 0; m < 18; m++) {
                int[] perm = MOVE[m];
                int[] next = new int[cur.length];
                for (int k = 0; k < cur.length; k++) next[k] = applyToPosition(perm, cur[k]);
                int key = pack(next);
                if (!dist.containsKey(key)) {
                    dist.put(key, d + 1);
                    queue.add(next);
                }
            }
        }
        return dist;
    }

    /** 位置 p 上的贴纸经过此转动后所在的位置（perm[target]=source，需反查）。 */
    private static int applyToPosition(int[] perm, int p) {
        for (int i = 0; i < 54; i++) if (perm[i] == p) return i;
        return p;
    }
}
