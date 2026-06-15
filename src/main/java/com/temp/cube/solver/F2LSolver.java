package com.temp.cube.solver;

import com.temp.cube.enums.Direction;
import com.temp.cube.enums.SideTurnEnum;
import com.temp.cube.model.Cube;
import com.temp.cube.turn.SideTurnAction;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * F2L 求解器（基于预生成的"角棱对"距离表 + IDA*）。
 *
 * <p>核心思路：对每个槽位，把"目标角块 + 目标棱块"两块的摆放抽象成一个状态码
 * （角块位置×朝向、棱块位置×朝向），用 BFS 从还原态出发，在全 6 面转动下穷举出
 * 每个状态码到还原所需的最少步数，形成一张距离表（模式数据库 / pattern database）。
 *
 * <p>这张表只关心这一对块、忽略其它块，因此它给出的步数是真实代价的下界，
 * 是一个可容纳（admissible）启发函数。用它指引 IDA* 搜索，能在保持十字与已完成槽位
 * 的前提下快速找到当前槽的还原步骤。
 *
 * <p>坐标系（由实测转动确认）：
 * <pre>
 *   U: row0=back, row2=front, col0=left, col2=right
 *   D: row0=front, row2=back,  col0=left, col2=right
 *   侧面: row0=顶(贴U), row2=底(贴D); F.col2/R.col0 贴在一起, 依此类推
 * </pre>
 */
public class F2LSolver {

    /** 面索引：0=U 1=D 2=F 3=B 4=R 5=L */
    private static final SideTurnEnum[] ALL_SIDES = {
        SideTurnEnum.U, SideTurnEnum.R, SideTurnEnum.L,
        SideTurnEnum.D, SideTurnEnum.F, SideTurnEnum.B
    };

    /** 8 个角块位置的三贴纸坐标 {面,行,列}，顺序: UFR UFL UBR UBL DFR DFL DBR DBL */
    private static final int[][][] CORNER = {
        {{0,2,2},{2,0,2},{4,0,0}}, // UFR
        {{0,2,0},{2,0,0},{5,0,2}}, // UFL
        {{0,0,2},{3,0,0},{4,0,2}}, // UBR
        {{0,0,0},{3,0,2},{5,0,0}}, // UBL
        {{1,0,2},{2,2,2},{4,2,0}}, // DFR
        {{1,0,0},{2,2,0},{5,2,2}}, // DFL
        {{1,2,2},{3,2,0},{4,2,2}}, // DBR
        {{1,2,0},{3,2,2},{5,2,0}}, // DBL
    };

    /** 12 个棱块位置的双贴纸坐标，顺序: UF UB UR UL DF DB DR DL FR FL BR BL */
    private static final int[][][] EDGE = {
        {{0,2,1},{2,0,1}}, // UF
        {{0,0,1},{3,0,1}}, // UB
        {{0,1,2},{4,0,1}}, // UR
        {{0,1,0},{5,0,1}}, // UL
        {{1,0,1},{2,2,1}}, // DF
        {{1,2,1},{3,2,1}}, // DB
        {{1,1,2},{4,2,1}}, // DR
        {{1,1,0},{5,2,1}}, // DL
        {{2,1,2},{4,1,0}}, // FR
        {{2,1,0},{5,1,2}}, // FL
        {{3,1,0},{4,1,2}}, // BR
        {{3,1,2},{5,1,0}}, // BL
    };

    private static final String Y = "YELLOW", G = "GREEN", R = "RED", B = "BLUE", O = "ORANGE";

    /** 每个槽位目标角块的三色 */
    private static final String[][] CORNER_COLORS = {
        {Y, G, R}, {Y, R, B}, {Y, B, O}, {Y, O, G}
    };
    /** 每个槽位目标棱块的两色 */
    private static final String[][] EDGE_COLORS = {
        {G, R}, {R, B}, {B, O}, {O, G}
    };
    /** 棱块朝向参考色（用于区分两种朝向） */
    private static final String[] EDGE_PRIMARY = {G, R, B, O};

    private static final int MAX_BOUND = 40;

    /**
     * 启发权重（加权 IDA*）。本求解器不追求最优步数，对启发函数加权能大幅减少
     * 搜索量、更快找到一个可行解（代价不超过最优的约 WEIGHT 倍）。
     */
    private static final int WEIGHT = 3;

    private final CubeStateChecker checker = new CubeStateChecker();

    /** 每个槽位的距离表：状态码 -> 还原所需最少步数 */
    private final Map<Integer, Integer>[] tables = buildAllTables();

    @SuppressWarnings("unchecked")
    private Map<Integer, Integer>[] buildAllTables() {
        Map<Integer, Integer>[] t = new HashMap[4];
        for (int slot = 0; slot < 4; slot++) {
            t[slot] = buildTable(slot);
        }
        return t;
    }

    public List<SideTurnAction> solve(Cube cube) {
        List<SideTurnAction> all = new ArrayList<>();
        if (checker.isF2LSolved(cube)) return all;

        for (int slot = 0; slot < 4; slot++) {
            if (isSlotSolved(cube, slot)) continue;
            int start = heuristic(getArrays(cube), slot);
            for (int bound = start; bound <= MAX_BOUND; bound++) {
                List<SideTurnAction> result = new ArrayList<>();
                if (dfs(cube, slot, 0, bound, result, null, null)) {
                    all.addAll(result);
                    break;
                }
            }
        }
        return all;
    }

    /** IDA*：在保持十字与 [0,slot) 已完成槽位的前提下还原当前槽。 */
    private boolean dfs(Cube cube, int slot, int g, int bound, List<SideTurnAction> moves,
                        SideTurnEnum lastSide, SideTurnEnum secondLastSide) {
        String[][][] f = getArrays(cube);
        int h = heuristic(f, slot);
        if (g + WEIGHT * h > bound) return false;
        if (h == 0 && isAccept(f, slot)) return true;

        for (SideTurnEnum side : ALL_SIDES) {
            if (side == lastSide) continue;                              // 同面连转冗余
            if (side == secondLastSide && isOpposite(side, lastSide)) continue; // X opp X 冗余
            for (Direction dir : Direction.values()) {
                cube.turn(side, dir);
                moves.add(new SideTurnAction(side, dir));

                if (dfs(cube, slot, g + 1, bound, moves, side, lastSide)) return true;

                moves.remove(moves.size() - 1);
                cube.turn(side, dir == Direction.CLOCKWISE
                        ? Direction.COUNTERCLOCKWISE : Direction.CLOCKWISE);
            }
        }
        return false;
    }

    private boolean isOpposite(SideTurnEnum a, SideTurnEnum b) {
        return (a == SideTurnEnum.U && b == SideTurnEnum.D) || (a == SideTurnEnum.D && b == SideTurnEnum.U)
            || (a == SideTurnEnum.R && b == SideTurnEnum.L) || (a == SideTurnEnum.L && b == SideTurnEnum.R)
            || (a == SideTurnEnum.F && b == SideTurnEnum.B) || (a == SideTurnEnum.B && b == SideTurnEnum.F);
    }

    /**
     * 可容纳启发：取下列各项下界的最大值（每项都是剩余步数的合法下界）：
     *   - 当前槽角棱对的还原距离（查表）；
     *   - 每个被破坏的前序槽，其角棱对的还原距离（查表）；
     *   - 被破坏的十字棱数。
     * 用 max 保证可容纳，同时在"前序槽被打散"时能给出强下界，避免搜索爆炸。
     * 性能：前序槽多数时刻是完好的，先用便宜的 5 贴纸判定，破坏了才查表。
     */
    private int heuristic(String[][][] f, int slot) {
        int h = tables[slot].getOrDefault(code(f, slot), MAX_BOUND);
        for (int e = 0; e < slot; e++) {
            if (isSlotSolved(f, e)) continue;
            int d = tables[e].getOrDefault(code(f, e), MAX_BOUND);
            if (d > h) h = d;
        }
        int crossBroken = crossUnsolvedEdges(f);
        return Math.max(h, crossBroken);
    }

    /** 未还原的十字棱数（贴 D 面那一圈黄棱） */
    private int crossUnsolvedEdges(String[][][] f) {
        int u = 0;
        if (!f[1][0][1].equals(Y) || !f[2][2][1].equals(G)) u++; // DF / front
        if (!f[1][1][2].equals(Y) || !f[4][2][1].equals(R)) u++; // DR / right
        if (!f[1][2][1].equals(Y) || !f[3][2][1].equals(B)) u++; // DB / back
        if (!f[1][1][0].equals(Y) || !f[5][2][1].equals(O)) u++; // DL / left
        return u;
    }

    /** 接受条件：当前槽完成 + 十字完好 + 前序槽都完好 */
    private boolean isAccept(String[][][] f, int slot) {
        if (!isSlotSolved(f, slot)) return false;
        if (crossUnsolvedEdges(f) != 0) return false;
        for (int e = 0; e < slot; e++) {
            if (!isSlotSolved(f, e)) return false;
        }
        return true;
    }

    /** 基于贴纸快照的槽位完成判定（避免重复拷贝面数组）。面索引 1=D 2=F 3=B 4=R 5=L */
    private boolean isSlotSolved(String[][][] f, int slot) {
        switch (slot) {
            case 0: return f[2][1][2].equals(G) && f[2][2][2].equals(G)
                        && f[4][1][0].equals(R) && f[4][2][0].equals(R) && f[1][0][2].equals(Y);
            case 1: return f[4][1][2].equals(R) && f[4][2][2].equals(R)
                        && f[3][1][0].equals(B) && f[3][2][0].equals(B) && f[1][2][2].equals(Y);
            case 2: return f[3][1][2].equals(B) && f[3][2][2].equals(B)
                        && f[5][1][2].equals(O) && f[5][2][2].equals(O) && f[1][2][0].equals(Y);
            case 3: return f[5][1][0].equals(O) && f[5][2][0].equals(O)
                        && f[2][1][0].equals(G) && f[2][2][0].equals(G) && f[1][0][0].equals(Y);
            default: return false;
        }
    }

    // ----------------------------------------------------------------------
    // 距离表构建（BFS over pair-state codes）
    // ----------------------------------------------------------------------

    private Map<Integer, Integer> buildTable(int slot) {
        Map<Integer, Integer> dist = new HashMap<>();
        Queue<List<SideTurnAction>> queue = new ArrayDeque<>();

        List<SideTurnAction> empty = new ArrayList<>();
        dist.put(code(getArrays(Cube.init()), slot), 0);
        queue.add(empty);

        while (!queue.isEmpty()) {
            List<SideTurnAction> path = queue.poll();
            Cube base = replay(path);
            int curDist = dist.get(code(getArrays(base), slot));

            for (SideTurnEnum side : ALL_SIDES) {
                for (Direction dir : Direction.values()) {
                    base.turn(side, dir);
                    int code = code(getArrays(base), slot);
                    if (!dist.containsKey(code)) {
                        dist.put(code, curDist + 1);
                        List<SideTurnAction> np = new ArrayList<>(path);
                        np.add(new SideTurnAction(side, dir));
                        queue.add(np);
                    }
                    base.turn(side, dir == Direction.CLOCKWISE
                            ? Direction.COUNTERCLOCKWISE : Direction.CLOCKWISE);
                }
            }
        }
        return dist;
    }

    private Cube replay(List<SideTurnAction> path) {
        Cube cube = Cube.init();
        for (SideTurnAction m : path) cube.turn(m.getSideTurnEnum(), m.getDirection());
        return cube;
    }

    // ----------------------------------------------------------------------
    // 状态编码
    // ----------------------------------------------------------------------

    /** 把目标角棱对的摆放编码为整数：(角位*3+角向)*24 + (棱位*2+棱向) */
    private int code(String[][][] f, int slot) {
        int cornerPos = -1, cornerOri = 0;
        for (int p = 0; p < 8 && cornerPos < 0; p++) {
            String[] cols = {
                f[CORNER[p][0][0]][CORNER[p][0][1]][CORNER[p][0][2]],
                f[CORNER[p][1][0]][CORNER[p][1][1]][CORNER[p][1][2]],
                f[CORNER[p][2][0]][CORNER[p][2][1]][CORNER[p][2][2]],
            };
            if (sameSet3(cols, CORNER_COLORS[slot])) {
                cornerPos = p;
                for (int k = 0; k < 3; k++) if (cols[k].equals(Y)) cornerOri = k;
            }
        }

        int edgePos = -1, edgeOri = 0;
        for (int p = 0; p < 12 && edgePos < 0; p++) {
            String[] cols = {
                f[EDGE[p][0][0]][EDGE[p][0][1]][EDGE[p][0][2]],
                f[EDGE[p][1][0]][EDGE[p][1][1]][EDGE[p][1][2]],
            };
            if (sameSet2(cols, EDGE_COLORS[slot])) {
                edgePos = p;
                for (int k = 0; k < 2; k++) if (cols[k].equals(EDGE_PRIMARY[slot])) edgeOri = k;
            }
        }
        return (cornerPos * 3 + cornerOri) * 24 + (edgePos * 2 + edgeOri);
    }

    private boolean sameSet3(String[] a, String[] set) {
        return contains(set, a[0]) && contains(set, a[1]) && contains(set, a[2]);
    }

    private boolean sameSet2(String[] a, String[] set) {
        return contains(set, a[0]) && contains(set, a[1]);
    }

    private boolean contains(String[] set, String v) {
        for (String s : set) if (s.equals(v)) return true;
        return false;
    }

    /** 读出 6 个面的贴纸：索引 0=U 1=D 2=F 3=B 4=R 5=L */
    private String[][][] getArrays(Cube cube) {
        return new String[][][] {
            cube.getUpSide().getOutputArray(),
            cube.getDownSide().getOutputArray(),
            cube.getFrontSide().getOutputArray(),
            cube.getBackSide().getOutputArray(),
            cube.getRightSide().getOutputArray(),
            cube.getLeftSide().getOutputArray(),
        };
    }

    // ----------------------------------------------------------------------
    // 槽位完成判定
    // ----------------------------------------------------------------------

    boolean isSlotSolved(Cube cube, int slot) {
        String[][] f = cube.getFrontSide().getOutputArray();
        String[][] r = cube.getRightSide().getOutputArray();
        String[][] b = cube.getBackSide().getOutputArray();
        String[][] l = cube.getLeftSide().getOutputArray();
        String[][] d = cube.getDownSide().getOutputArray();
        switch (slot) {
            case 0: // FR
                return f[1][2].equals(G) && f[2][2].equals(G)
                    && r[1][0].equals(R) && r[2][0].equals(R)
                    && d[0][2].equals(Y);
            case 1: // RB
                return r[1][2].equals(R) && r[2][2].equals(R)
                    && b[1][0].equals(B) && b[2][0].equals(B)
                    && d[2][2].equals(Y);
            case 2: // BL
                return b[1][2].equals(B) && b[2][2].equals(B)
                    && l[1][2].equals(O) && l[2][2].equals(O)
                    && d[2][0].equals(Y);
            case 3: // LF
                return l[1][0].equals(O) && l[2][0].equals(O)
                    && f[1][0].equals(G) && f[2][0].equals(G)
                    && d[0][0].equals(Y);
            default: return false;
        }
    }

    public boolean isF2LSolved(Cube cube) {
        return checker.isF2LSolved(cube);
    }
}
