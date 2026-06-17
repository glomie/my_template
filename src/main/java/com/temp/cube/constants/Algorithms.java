package com.temp.cube.constants;

/**
 * CFOP 求解用的公式/触发器数据（标准记法字符串，纯整面转 + 转体，不含 wide/M）。
 *
 * <p>这里只放"数据"，解析与搜索逻辑在 {@code solver.engine} 中。把领域数据集中于此，
 * 便于查阅、增删公式而不触碰求解算法。</p>
 */
public final class Algorithms {

    private Algorithms() {}

    // ---- 末层常用触发器 / 宏 ----
    /** 角块定向 Sune。 */
    public static final String SUNE = "R U R' U R U2 R'";
    /** 角块定向 Antisune。 */
    public static final String ANTISUNE = "R U2 R' U' R U' R'";
    /** 纯角块 3 循环（A-perm，用作 PLL 宏兜底）。 */
    public static final String A_PERM = "R' F R' B2 R F' R' B2 R2";
    /** 纯棱块 3 循环（U-perm，用作 PLL 宏兜底）。 */
    public static final String U_PERM = "R U' R U R U R U' R' U' R2";
    /** 翻顶棱、做出顶面十字（保持下两层）。 */
    public static final String EO_CROSS = "F R U R' U' F'";

    /**
     * OLL 完整公式集：恰好 57 条，一一对应 57 种 OLL 情形，覆盖全部 215 个顶层定向状态。
     * 全部使用纯整面转（U/R/F/L/D/B），无 wide/M/S/E。
     *
     * <p>求解时由 {@link com.temp.cube.solver.engine} 的 Search.tryAlgs 枚举 0–3 次<b>前置 AUF</b>
     * 做朝向对齐，套用匹配的那一条公式（在副本上验证确实定向顶层且保持下两层后才采用，取最短）。</p>
     *
     * <p>表中较长（13–15 步）的几条，是少数标准上需用 wide/M 的情形——本解析器只支持整面转，
     * 故改用等效的纯整面转单步公式（由双向 BFS 搜得的 ≤15 步最优解，已逐一验证）。</p>
     */
    public static final String[] OLL = {
        // —— OCLL / 角棱常见短公式 ——
        "F' U' L' U L F",
        "F R U R' U' F'",
        "F U R U' R' F'",
        "R U2 R' U' R U' R'",
        "R U R' U R U2 R'",
        "R' U' R' F R F' U R",
        "R U2 R' U2 R' F R F'",
        "F R' F' R U R U' R'",
        "R U R' U' R' F R F'",
        "R2 D R' U2 R D' R' U2 R'",
        "R' U' F U R U' R' F' R",
        "R U2 R2 U' R2 U' R2 U2 R",
        "R' F R U R' F' R F U' F'",
        "R U R' U' B' R' F R F' B",
        "F U R U' R' U R U' R' F'",
        "F R U R' U' R U R' U' F'",
        "R' F' U' F U' R U R' U R",
        "L F' L' U' L F L' F' U F",
        "R U R' U' R' F R2 U R' U' F'",
        "R U2 R2 F R F' U2 R' F R F'",
        "R U2 R2 U' R U' R' U2 F R F'",
        "R U2 R' U' R U R' U' R U' R'",
        "R U R' U R' F R F' R U2 R'",
        "L' U' L U' L' U L U L F' L' F",
        "R U R' U R U' R' U' R' F R F'",
        "R' U' R' F R F' R' F R F' U R",
        "U R' F R F2 U' F R U2 R' F' U2 F",
        "F R U R' U' F' U2 F R U R' U' F'",
        "R' U' F' U F R U2 R' U' F' U F R",
        "R' U' R U' R' U2 R F R U R' U' F'",
        "R U R' U R U2 R' F R U R' U' F'",
        "R U R' U R' F R F' U2 R' F R F'",
        "U2 F U2 F2 U2 R' F' R U2 L F L' U2",
        "F R U R' U' F' U F R U R' U' F'",
        "U2 F' U' L' U L2 F U F' U' L' U F",
        "F U R U' R' U2 F2 L F L' F U2 F'",
        "R U2 L' U R' U' L F2 L F L' F U2",
        "L U2 L' U' L U' F2 R' F2 L' F R F'",
        "L U F U' R F' L' F U R' U' F' U'",
        "R U R' U R U2 R' F U R U' R' F'",
        "U2 L F R U2 R2 F R F2 L' U L U' L'",
        "F U2 F' U' F2 U R' U' F' U R U2 F' U'",
        "U' R2 F2 R U' F' U F L F L2 U2 L R",
        "F U F' U L' U2 L F U2 R' F R F2 U'",
        "U R' F2 L F L' F R F U R U' R' F'",
        "R U R' U R U2 R' U2 F R U R' U' F'",
        "L2 U' L2 F R' F' L' F R2 U R' F' U' L",
        "R' F R U F' U F U2 F2 U' L' U L U F",
        "U R' F' R U2 R' L F' L' F' U' F2 U F' R",
        "R2 F L2 F' R F L2 F2 U F U F' U' F R",
        "R U R' U R U2 R2 U' F U R U' R' F' R",
        "R U R' U R U2 R' U2 R U2 R' U' R U' R'",
        "U L U2 R2 F L F' R F R' F L' F' L' R2",
        "R U R' U R U2 R' U' R U2 R' U' R U' R'",
        "U2 L U2 R' F' L F' L' U F2 U' F2 L' U' R",
        "U F R U F R2 U' R2 U R2 U F' U2 R' F'",
        "U2 F2 L2 U' F L F' U L2 F2 U' R U2 R' U",
    };

    /** PLL 标准公式集（纯整面转，含 D 但不含 M/wide/旋转）。 */
    public static final String[] PLL = {
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
        "R' U R' U' R D' R' D R' U D' R2 U' R2 D R2",                    // V
        "R U R' U R U R' F' R U R' U' R' F R2 U' R' U2 R U' R'",     // Na
        "R' U R U' R' F' U' F R U R' F R' F' R U' R",                // Nb
        "R2 U R' U R' U' R U' R2 U' D R' U R D'",                    // Ga
        "R' U' R U D' R2 U R' U R U' R U' R2 D",                     // Gb
        "R2 U' R U' R U R' U R2 U D' R U' R' D",                     // Gc
        "R U R' U' D R2 U' R U' R' U R' U R2 D'",                    // Gd
        "R B' R' F R B R' F' R B R' F R B' R' F'",                   // E
        "R2 U R U R' U' R' U' R' U R' U R2 U R U R' U' R' U' R' U R' U'", // H
        "R U' R U R U R U' R' U' R2 U R U' R U R U R U' R' U' R2 U'" // Z
    };
}
