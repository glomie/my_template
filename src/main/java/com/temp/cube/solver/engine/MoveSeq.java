package com.temp.cube.solver.engine;

import java.util.ArrayList;
import java.util.List;

/**
 * 内部 move token 序列的工具：公式解析、逆操作、相邻合并简化等。
 *
 * <p>token 约定见 {@link MoveCodec}：面转动 0..17，y 转体从 {@link MoveCodec#ROT_Y_BASE} 起。</p>
 */
final class MoveSeq {

    private MoveSeq() {}

    /** 相对面（U-D, R-L, F-B），用于搜索去重剪枝。 */
    static final int[] OPPOSITE = {3, 4, 5, 0, 1, 2};

    /** 面转动 token 的逆。 */
    static int inverse(int m) {
        int face = m / 3, a = m % 3;
        int ia = (a == 0) ? 2 : (a == 2 ? 0 : 1);
        return face * 3 + ia;
    }

    /**
     * 解析标准记法公式为内部 token 序列。支持：
     * <ul>
     *   <li>面转动 U R F D L B（kind 0..5）；</li>
     *   <li>整方块转体 y x z（kind 6/7/8）；</li>
     *   <li>宽层：小写 r l u d f b，或大写 + w（Rw Lw Uw Dw Fw Bw）（kind 9..14）；</li>
     *   <li>中层 M E S（kind 15/16/17）。</li>
     * </ul>
     * 后缀 ' 表示逆（amount 3），2 表示 180°（amount 2），无后缀为 90° CW（amount 1）。
     * token = kind*3 + (amount-1)。无法识别的记号会跳过。
     */
    static int[] parse(String alg) {
        List<Integer> moves = new ArrayList<>();
        for (String tok : alg.trim().split("\\s+")) {
            if (tok.isEmpty()) continue;
            int kind = kindOf(tok);
            if (kind < 0) continue;
            int a = 0; // 90 CW
            if (tok.contains("2")) a = 1;
            else if (tok.contains("'")) a = 2;
            moves.add(kind * 3 + a);
        }
        return toIntArray(moves);
    }

    /** 记号 → kind（0..17），无法识别返回 -1。 */
    private static int kindOf(String tok) {
        char c0 = tok.charAt(0);
        boolean wide = tok.length() > 1 && tok.charAt(1) == 'w';
        switch (c0) {
            case 'U': return wide ? 11 : 0;
            case 'R': return wide ? 9  : 1;
            case 'F': return wide ? 13 : 2;
            case 'D': return wide ? 12 : 3;
            case 'L': return wide ? 10 : 4;
            case 'B': return wide ? 14 : 5;
            case 'y': return 6;
            case 'x': return 7;
            case 'z': return 8;
            case 'r': return 9;   // 小写 = 宽层
            case 'l': return 10;
            case 'u': return 11;
            case 'd': return 12;
            case 'f': return 13;
            case 'b': return 14;
            case 'M': return 15;
            case 'E': return 16;
            case 'S': return 17;
            default:  return -1;
        }
    }

    static int[][] parseAll(String... algs) {
        int[][] out = new int[algs.length][];
        for (int i = 0; i < algs.length; i++) out[i] = parse(algs[i]);
        return out;
    }

    /**
     * 简化手序：合并相邻同“通道”转动（通道 = kind = token/3，每种转动一条通道），
     * 按 1/4 圈相加 mod 4。例如 U U U → U'、R R R R → 无、y2 y → y'、Rw Rw → Rw2。
     * 仅合并相邻同种转动，不改变整体效果。
     */
    static int[] simplify(int[] moves) {
        ArrayList<int[]> stack = new ArrayList<>(); // 每项 {channel, quarter(1..3)}
        for (int m : moves) {
            int channel = m / 3, q = m % 3 + 1;
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
            out[i] = stack.get(i)[0] * 3 + (stack.get(i)[1] - 1);
        }
        return out;
    }

    /** 逆转整段序列：reverse 顺序并对每步取逆。 */
    static int[] invert(int[] seq) {
        int[] inv = new int[seq.length];
        for (int i = 0; i < seq.length; i++) inv[i] = inverse(seq[seq.length - 1 - i]);
        return inv;
    }

    static int[] toIntArray(List<Integer> list) {
        int[] a = new int[list.size()];
        for (int i = 0; i < a.length; i++) a[i] = list.get(i);
        return a;
    }
}
