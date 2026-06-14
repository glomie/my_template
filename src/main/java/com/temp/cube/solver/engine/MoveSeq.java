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

    /** 解析标准记法公式（U R F D L B + ' / 2）为内部 token 序列。 */
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

    static int[][] parseAll(String... algs) {
        int[][] out = new int[algs.length][];
        for (int i = 0; i < algs.length; i++) out[i] = parse(algs[i]);
        return out;
    }

    /**
     * 简化手序：合并相邻同“通道”转动（面 0..5，或 y 转体=通道 6），按 1/4 圈相加 mod 4。
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

    static int[] toIntArray(List<Integer> list) {
        int[] a = new int[list.size()];
        for (int i = 0; i < a.length; i++) a[i] = list.get(i);
        return a;
    }
}
