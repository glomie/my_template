package com.temp.cube.constants;

/**
 * 求解器的可调参数（搜索深度上限、置换表容量、各阶段允许转动的面掩码）。
 * 这些是“配置数据”，与求解算法分离，便于统一调整。
 */
public final class SolverConfig {

    private SolverConfig() {}

    /** Cross：黄十字搜索深度上限（白/黄十字最优 ≤ 8）。 */
    public static final int CROSS_MAX_DEPTH = 8;
    /** F2L：单槽“仅本槽两侧面 + U”的受限浅搜深度。 */
    public static final int F2L_SLOT_RESTRICTED_DEPTH = 8;
    /** F2L：单槽放宽到“非 D 全面”的兜底深度。 */
    public static final int F2L_SLOT_FULL_DEPTH = 18;
    /** F2L：两阶段「调整到标态」搜索放宽到非 D 全面时的深度上限（标态比完全入槽浅，故略小）。 */
    public static final int F2L_SETUP_FULL_DEPTH = 14;
    /** OLL：翻棱宏 BFS 深度上限。 */
    public static final int EO_MACRO_DEPTH = 8;
    /** OLL：定向四角宏兜底深度上限。 */
    public static final int OLL_MACRO_DEPTH = 12;
    /** PLL：排列宏兜底深度上限。 */
    public static final int PLL_MACRO_DEPTH = 12;
    /** IDA* 置换表最大条目数（限制内存）。 */
    public static final int TT_CAP = 2_000_000;

    /** 面顺序 U,R,F,D,L,B。所有面均可转动（Cross 用）。 */
    public static boolean[] allFaces() {
        return new boolean[]{true, true, true, true, true, true};
    }

    /** 禁止转动 D（F2L 用，保持底层黄十字）。 */
    public static boolean[] noDown() {
        return new boolean[]{true, true, true, false, true, true};
    }
}
