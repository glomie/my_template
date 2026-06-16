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
     * OLL 完整公式集：覆盖全部 57 种 OLL 情形，全部使用纯整面转（R/L/U/D/F/B），无 wide/M/S/E。
     * 求解时由 Search.tryAlgs 枚举 0–3 次前置 AUF 做模式对齐，命中即一条公式还原顶层定向。
     * 公式在副本上验证后才施加，多余/错误的条目只会被跳过，不影响正确性。
     */
    public static final String[] OLL = {
        // === OCLL（顶棱全部定向，只需定向四角）7 种 ===
        "R U R' U R U2 R'",                              // OLL 27 Sune
        "R U2 R' U' R U' R'",                            // OLL 26 Antisune
        "R U2 R2 U' R2 U' R2 U2 R",                      // OLL 22 Pi
        "R U2 R' U' R U R' U' R U' R'",                  // OLL 21 H / Double Antisune
        "R2 D R' U2 R D' R' U2 R'",                      // OLL 25 Headlights
        "R U R' U' R' F R F'",                            // OLL 24 T-shape
        "F' R U R' U' R' F R",                            // OLL 23 L-shape

        // === 顶面十字已完成，部分角未定向（Cross done）===
        // Fish (鱼形) — 2 个角定向
        "R U R' U' R' F R F'",                            // OLL 33 (Fish / Frying Pan)
        "F R' F' R U R U' R'",                            // OLL 37 (Fish)
        // Square (方块)
        "R U2 R' U' R U' R' F U' F' R U R'",             // OLL 20 (squares)
        // Knight (马形 / L)
        "R' F R U R' F' R F U' F'",                       // OLL 36
        "L F' L' U' L F L' F' U F",                       // OLL 38
        // Small L
        "R U R' U' R' F R F' R U2 R'",                   // OLL 35
        "R' U' R U' R' U2 R F R U R' U' F'",             // OLL 34
        // P-shapes
        "F U R U' R' F'",                                 // OLL 44
        "F' U' L' U L F",                                 // OLL 43
        "R' U' R' F R F' R' F R F' U R",                 // OLL 32 (P)
        "R U R' F' R U R' U' R' F R2 U' R'",             // OLL 31 (P)
        // C-shapes
        "R U R' U' B' R' F R F' B",                       // OLL 46
        "R' U' R' F R F' U R",                            // OLL 45 (T/C)
        // W-shapes
        "R U R' U R U' R' U' R' F R F'",                  // OLL 35 (W)
        "L' U' L U' L' U L U L F' L' F",                  // OLL 35 mirror (W)
        // S-shapes
        "F R U R' U' F' U F R U R' U' F'",               // OLL 48 / S-shape
        "F R U R' U' R U R' U' F'",                       // OLL 48 alt
        // Z-shapes
        "R' U' F U R U' R' F' R",                         // OLL 29 (Z)
        "F U R U' R2 F' R U R U' R'",                     // OLL 30 (Z)

        // === 顶面有两棱未定向（T/L/I 棱形）===
        // T-shapes (2 adjacent edges)
        "F R U R' U' F'",                                 // OLL 45 T-front
        "R' F' U' F U R",                                 // OLL 39
        "L F U F' U' L'",                                 // OLL 40
        // I-shape (2 opposite edges)
        "F R U R' U' R U R' U' F'",                       // OLL 49
        "R U R' U R U2 R' F R U R' U' F'",               // OLL 55
        // L-bar
        "F U R U' R' U R U' R' F'",                       // OLL 44 alt
        "R' U' R' F R F' R' F R F' U R",                  // OLL 50
        // Lightning
        "R' F' U' F U' R U R' U R",                       // OLL 11
        "L F U F' U L' U' L U' L'",                       // OLL 12
        "R U R' U R' F R F' R U2 R'",                     // OLL 9
        "R' U' R U' R' U F' U F R",                       // OLL 10
        // Anti-sune + edge
        "R U2 R2 U' R U' R' U2 F R F'",                   // OLL 5
        "L' U2 L2 U L' U L U2 F' L' F",                   // OLL 6
        "R U R' U' R' F R2 U R' U' F'",                   // OLL 53
        "R' U' R U R' F' R U2 R' U2 R F",                 // OLL 54
        "R U2 R' U2 R' F R F'",                           // OLL 18 (knight)
        "R' U2 R U2 L U' R' U L'",                        // OLL 17
        "R U R' U' R U' R' U2 R' F R F'",                 // OLL 51
        "R' U' R U' R' U R U R' F R F' R",               // OLL 52

        // === 点形 Dot（顶面无一白色，棱角全需翻）===
        "R U2 R2 F R F' U2 R' F R F'",                    // OLL 1
        "F R U R' U' F' U2 F R U R' U' F'",              // OLL 2
        "R U R' U R U2 R' U2 R' F R F'",                 // OLL 3
        "R' U' F' U F R U2 R' U' F' U F R",              // OLL 4
        "R' F R2 U R' U R U2 R' U F'",                   // OLL 7
        "R U R2 F' R U2 R' U' F U R",                    // OLL 8
        "R' U' F U R U' R' F' R U2 R' U2 R",             // OLL 13
        "R U R' F' R U R' U' R' F R2 U' R'",             // OLL 14
        "L' U' L U L F U F' U' L' U L F' L F",           // OLL 15
        "R U R' U R' F R F' U2 R' F R F'",               // OLL 16
        "F R U' R' U R U R' F' R U R' U' R' F R F'",    // OLL 19
        "F' R U R' U' R' F R U R U' R' U R U2 R'",      // OLL 20 dot alt

        // === 补充覆盖缺失 OLL 情形的公式（2-look 搜索验证）===
        "R U R' U R U2 R2 U' F U R U' R' F' R",
        "U R U R' U R U2 R' U R U2 R2 F R F' U2 R' F R F'",
        "U R U R' U R U2 R' U2 F R' F' R U R U' R'",
        "R U R' U R U' R' U R U2 R' U R U2 R2 F R F' U2 R' F R F'",
        "R U R' U R U2 R' U R U R' U R U2 R' U R U2 R2 F R F' U2 R' F R F'",
        "U R U R' U R U2 R' U' R U R' U R U2 R' U R U2 R2 F R F' U2 R' F R F'",
        "R U R' U R U2 R' U2 R' F' U' F U' R U R' U R",
        "U2 R U R' U R U2 R' U2 R U R' U R U' R' U' R' F R F'",
        "R U R' U R U2 R' U2 F R U R' U' R U R' U' F'",
        "R U R' U R U2 R' U2 R U2 R2 U' R2 U' R2 U2 R",
        "R U R' U R U2 R' U' R U R' U R' F R F' U2 R' F R F'",
        "R U R' U R U2 R' U' R' F R U R' F' R F U' F'",
        "R U R' U R U2 R' U' R U R' U' R' F R2 U R' U' F'",
        "R U R' U R U2 R' U2 R U2 R' U' R U' R'",
        "U R U R' U R U2 R' U2 R U R' U R' F R F' U2 R' F R F'",
        "R U R' U R U2 R' F U R U' R' F'",
        "R U R' U R U2 R' U R' U' R U' R' U2 R F R U R' U' F'",
        "R U R' U R U2 R' U' R U R' U R U2 R' F R U R' U' F'",
        "R U R' U R U2 R' U2 F R U R' U' F'",
        "U2 R U R' U R U2 R' U' R U R' U R U2 R' U R U2 R2 F R F' U2 R' F R F'",
        "R U R' U R U2 R' U R U R' U' R' F R F'",
        "R U R' U R U2 R' U R' U' F U R U' R' F' R",
        "R U R' U R U2 R' U2 R U R' U' D' R' F R F' D",
        "U R U R' U R U2 R' U2 R U2 R2 U' R2 U' R2 U2 R",
        "U R U R' U R U2 R' U2 R U R' U R U2 R' U2 F R' F' R U R U' R'",
        "R U R' U R U2 R' U' R' F' U' F U' R U R' U R",
        "U2 R U R' U R U2 R' U' R U R' U' R' F R2 U R' U' F'",
        "U2 R U R' U R U2 R' U' R' F R U R' F' R F U' F'",
        "R U R' U R U2 R' U R U2 R' U' R U R' U' R U' R'",
        "R U R' U R U2 R2 U' R U' R' U2 R F R U R' U' F'",
        "R U R' U R U2 R' U2 R U R' U R U2 R' F R U R' U' F'",
        "R U R' U R U2 R L R' U2 R L' R' U2 R'",
        "U R U R' U R U2 R2 F' U' F U' R U R' U R",
        "R U R' U R U2 R' U2 R U R' U R U2 R' U2 F R' F' R U R U' R'",
        "U2 R U R' U R U2 R' U' R U R' U R' F R F' U2 R' F R F'",
        "R U R' U R U2 R' U R U R' U R' F R F' U2 R' F R F'",
        "R U R' U R U2 R' U B' U' B U' B' U B U B F' B' F",
        "R U R' U R U2 R' U2 R U R' U R U' R' U' R' F R F'",
        "R U R' U R U2 R' U2 R U R' U R U2 R2 U' F U R U' R' F' R",
        "U2 R U R' U R U2 R' U2 R U2 R2 U' R2 U' R2 U2 R",
        "U R U2 R' U' R U' R' U' R U R' U R U2 R' U R U2 R2 F R F' U2 R' F R F'",
        "U R U R' U R U2 R' U' B F' B' U' B F B' F' U F",
        "R U R' U R U2 R' U2 R U R' U' R' F R F'",
        "U R U R' U R U2 R' U R U R' U R U2 R2 U' F U R U' R' F' R",
        "U' R U R' U R U2 R' U R U R' U R U2 R2 U' F U R U' R' F' R",
        "R U R' U R U2 R' U2 R U R' U R' F R F' R U2 R'",
        "U R U R' U R U2 R' U' R' F' U' F U' R U R' U R",
        "U2 R U R' U R U2 R' U2 R U R' U R U2 R' U2 F R' F' R U R U' R'",
        "R U R' U R U2 R2 F' U' F U' R U R' U R",
        "R U R' U R U2 R' U2 R' U' R' F R F' R' F R F' U R",
        "R U2 R' U' R U' R' U' R U R' U R U2 R' U R U2 R2 F R F' U2 R' F R F'",
        "U R U R' U R U2 R' U' R U R' U' R' F R2 U R' U' F'",
        "U R U R' U R U2 R' U' R' F R U R' F' R F U' F'",
        "U R U R' U R U2 R' U2 R U2 R' U' R U' R'",
        "R U R' U R U2 R' U' R U R' U R U2 R' U R U2 R2 F R F' U2 R' F R F'",
        "U R U R' U R U2 R' U2 R U R' U R U' R' U' R' F R F'",
        "U R U R' U R U2 R' U2 R U R' U R U2 R2 U' F U R U' R' F' R",
        "R U R' U R U2 R2 F R U R' F' R F U' F'",
        "R U R' U R U2 R' U' R U2 R' U' R U' R'",
        "R U R' U R U2 R' U2 R U R' U R' F R F' U2 R' F R F'",
        "R U R' U R U2 R' U' R U R' U R U2 R2 U' F U R U' R' F' R",
        "R U R' U R U2 R' U2 B' U' B U' B' U B U B F' B' F",
        "R U R' U R U2 R' F R' F' R U R U' R'",
        "U2 R U R' U R U2 R' U R' U' F' U F R U2 R' U' F' U F R",
        "U2 R U R' U R U2 R' U2 R U R' U R' F R F' U2 R' F R F'",
        "R U R' U R U2 R' U R' U' F' U F R U2 R' U' F' U F R",
        "R U R' U R U2 R' U F R U R' U' F' U2 F R U R' U' F'",
        "R U R' U R U2 R' U2 F R U R' U' F' U2 F R U R' U' F'",
        "R U R' U R U2 R2 U' F' U F R U2 R' U' F' U F R",
        "U2 R U2 R' U' R U' R' U' R U R' U R U2 R' U R U2 R2 F R F' U2 R' F R F'",
        "R U R' U R U2 R' U' F' U' B' U B F",
        "R U R' U R U2 R' U R' U' R' F R F' U R",
        "U' R U R' U R U2 R' U2 R U R' U R' F R F' U2 R' F R F'",
        "R U R' U R U2 R' U' R U R' U' R' F R F'",
        "U R U R' U R U2 R' U R' U' F' U F R U2 R' U' F' U F R",
        "R U R' U R U2 R' U' R' U' F U R U' R' F' R",
        "U R U R' U R U2 R' U F R U R' U' F' U2 F R U R' U' F'",
        "U R U R' U R U2 R' U' R U R' U R' F R F' U2 R' F R F'",
        "R U R' U R U2 R' U R U R' U R U2 R2 U' F U R U' R' F' R",
        "R U R' U R U2 R' U R U R' U R U' R' U' R' F R F'",
        "U2 R U R' U R U2 R' U R U R' U R U2 R2 U' F U R U' R' F' R",
        "R U2 R' U' R U' R' U R U R' U R U2 R' U R U2 R2 F R F' U2 R' F R F'",
        "R U R' U R U2 R' U B F' B' U' B F B' F' U F",
        "U R U R' U R U2 R' U R' U' R' F R F' U R",
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
