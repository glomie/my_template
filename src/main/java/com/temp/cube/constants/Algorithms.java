package com.temp.cube.constants;

/**
 * CFOP 求解用的公式/触发器数据（标准记法字符串，纯整面转 + 转体，不含 wide/M）。
 *
 * <p>这里只放“数据”，解析与搜索逻辑在 {@code solver.engine} 中。把领域数据集中于此，
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
     * OLL 整体公式集：7 个 OCLL（顶棱已定向时定向四角）+ 若干非十字常见整体 OLL + 部分点形。
     * 标准 57 OLL 里大量使用 wide 转，本工程模型不支持，故只收录纯整面转可写的部分，
     * 其余复杂形状由 2-look（翻棱 + OCLL）兜底；求解时在副本上验证，错误公式只会被跳过。
     */
    public static final String[] OLL = {
        // 7 个 OCLL（顶棱已定向）
        "R U R' U R U2 R'",                  // Sune
        "R U2 R' U' R U' R'",                // Antisune
        "R U2 R2 U' R2 U' R2 U2 R",          // Pi
        "R U2 R' U' R U R' U' R U' R'",      // H
        "R2 D R' U2 R D' R' U2 R'",          // U (headlights)
        "R U R' U' R' F R F'",               // T
        "F' R U R' U' R' F R",               // L
        // 非十字的常见整体 OLL
        "F R U R' U' F'",                    // 45 (T 形)
        "F U R U' R' F'",                    // 44 (P 形)
        "F' U' L' U L F",                    // 43 (P 形镜像)
        "R U R' U' R' F R F'",               // 33 (T)
        "F R' F' R U R U' R'",               // 37 (鱼形)
        "R' U' F U R U' R' F' R",            // 31
        "F' L' U' L U L' U' L U F",          // 47
        "F R U R' U' R U R' U' F'",          // 48
        "R U R' U R U' R' U R U2 R'",        // 角定向变体
        "R U2 R' U' R U' R' U2 F R U R' U' F'", // 复合形状
        // 点形 / 其他
        "R U2 R2 F R F' U2 R' F R F'",       // 1 (点)
        "F R U R' U' F' U2 F R U R' U' F'",  // 2 (点)
        "R U R' U' R' F R F' U F R U R' U' F'", // 点变体
        "R U R' U R U' R' U' R' F R F'",     // W 形
        "L' U' L U' L' U L U L F' L' F",     // W 形镜像
        "F R U R' U' F' U F R U R' U' F'",   // P 复合
        "R U R' U' R U R' F' R U R' U' R' F R"  // 复合
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
        "R' U R' U' R D' R' D R' U D' R2 U' R2 D R2",                // V
        "R U R' U R U R' F' R U R' U' R' F R2 U' R' U2 R U' R'",     // Na
        "R' U R U' R' F' U' F R U R' F R' F' R U' R",                // Nb
        "R2 U R' U R' U' R U' R2 U' D R' U R D'",                    // Ga
        "R' U' R U D' R2 U R' U R U' R U' R2 D",                     // Gb
        "R2 U' R U' R U R' U R2 U D' R U' R' D",                     // Gc
        "R U R' U' D R2 U' R U' R' U R' U R2 D'",                    // Gd
        "R B' R' F R B R' F' R B R' F R B' R' F'"                    // E
    };
}
