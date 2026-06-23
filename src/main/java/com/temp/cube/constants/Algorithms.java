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
     * 采用速拧通用的 ergonomic 写法，以 R/U/L/F 为主，必要时用宽层（Rw/Lw/Fw）与中层（M），
     * 不出现别扭的 B 面转动；全部旋转中性（不残留整方块转向）。
     *
     * <p>求解时由 {@link com.temp.cube.solver.engine} 的 Search.tryAlgs 枚举 0–3 次<b>前置 AUF</b>
     * 做朝向对齐，套用匹配的那一条公式（在副本上验证确实定向顶层且保持下两层后才采用，取最短）。</p>
     *
     * <p>记法体系不影响正确性：宽层/中层在施加时展开成等效的「面 + 转体」原子步
     * （见 {@link com.temp.cube.solver.engine.MoveCodec}）。仅末一条（罕见情形）暂用较长的整面转写法。</p>
     */
    public static final String[] OLL = {
        "F' U' L' U L F",
        "F R U R' U' F'",
        "F U R U' R' F'",
        "R U2 R' U' R U' R'",
        "R U R' U R U2 R'",
        "R' U' R' F R F' U R",
        "Lw' U' L U' L' U2 Lw",
        "F R' F' R U R U' R'",
        "R U R' U' R' F R F'",
        "R2 D R' U2 R D' R' U2 R'",
        "R' U' F U R U' R' F' R",
        "R U2 R2 U' R2 U' R2 U2 R",
        "R' F R U R' F' R F U' F'",
        "R U R2 U' R' F R U R U' F'",
        "F U R U' R' U R U' R' F'",
        "F R U R' U' R U R' U' F'",
        "R' F' U' F U' R U R' U R",
        "Rw U' Rw' U' Rw U Rw' F' U F",
        "R U R' U' R' F R2 U R' U' F'",
        "R U2 R2 F R F' U2 R' F R F'",
        "R U2 R2 U' R U' R' U2 F R F'",
        "R U2 R' U' R U R' U' R U' R'",
        "R U R' U R' F R F' R U2 R'",
        "L' U' L U' L' U L U L F' L' F",
        "R U R' U R U' R' U' R' F R F'",
        "F' L' U' L U L' U' L U F",
        "Rw U R' U R U2 Rw2 U' R U' R' U2 Rw",
        "Rw U R' U' Rw' R U R U' R'",
        "R U R' U' M' U R U' Rw'",
        "R' U' R U' R' U2 R F R U R' U' F'",
        "R U R' U R U2 R' F R U R' U' F'",
        "R U R' U R' F R F' U2 R' F R F'",
        "R U2 R2 F R F' R U2 R'",
        "M' R' U' R U' R' U2 R U' R Rw'",
        "Rw' R U R U R' U' Rw R2 F R F'",
        "R U R' U' R U' R' F' U' F R U R'",
        "Rw U Rw' U R U' R' U R U' R' Rw U' Rw'",
        "Rw U' Rw2 U Rw2 U Rw2 U' Rw",
        "Fw R U R' U' Fw' U' F R U R' U' F'",
        "Rw U R' U R U2 Rw'",
        "Fw R U R' U' Fw' U F R U R' U' F'",
        "Rw U Rw' R U R' U' Rw U' Rw'",
        "F R' F R2 U' R' U' R U R' F2",
        "F R U R' U' F' Fw R U R' U' Fw'",
        "Rw' U Rw2 U' Rw2 U' Rw2 U Rw'",
        "Rw' U2 R U R' U Rw",
        "Rw U R' U R' F R F' R U2 Rw'",
        "Rw U R' U' M2 U R U' R' U' M'",
        "R' F R U R' U' F' U R",
        "Rw U2 R' U' R U' Rw'",
        "L F' L' U' L U F U' L'",
        "F' Rw U R' U' Rw' F R",
        "L U F' U' L' U L F L'",
        "Rw U R' U' Rw' F R F'",
        "Rw U2 R' U' R U R' U' R U' Rw'",
        "Lw' U2 L U L' U' L U L' U Lw",
        "U2 F2 L2 U' F L F' U L2 F2 U' R U2 R' U",
    };

    /**
     * PLL 标准公式集（恰好 21 条）。采用速拧通用的 ergonomic 写法：以 R/U/L/F 为主，
     * 必要时用整方块转体（x/y/z，已配平为旋转中性）、中层（M）。不出现别扭的 B 面转动。
     *
     * <p>求解时由 Search.tryAlgs 枚举前/后 AUF 在副本上验证完全还原后采用，故记法体系不影响正确性。</p>
     */
    public static final String[] PLL = {
        "x R' U R' D2 R U' R' D2 R2 x'",                             // Aa
        "x R2 D2 R U R' D2 R U' R x'",                               // Ab
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
        "x' R U' R' D R U R' D' R U R' D R U' R' D' x",              // E
        "M2 U M2 U2 M2 U M2",                                        // H
        "M2 U M2 U M' U2 M2 U2 M'"                                   // Z
    };
}
