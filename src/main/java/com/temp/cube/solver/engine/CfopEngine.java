package com.temp.cube.solver.engine;

/**
 * CFOP 求解引擎门面：把四个阶段委托给各自的 Phase，对外提供统一入口。
 * 各阶段在 {@link FaceCube} 上求解并返回内部 token 序列（成功时也会把解法施加到 fc）。
 *
 * <ul>
 *   <li>{@link CrossPhase}：黄十字（底面），IDA*。</li>
 *   <li>{@link F2LPhase}：下两层，IDA* + 转体/relabel 输出 RUL 流。</li>
 *   <li>{@link OllPhase}：定向顶面，整体公式优先、2-look 兜底。</li>
 *   <li>{@link PllPhase}：排列末层，标准公式 + 宏兜底。</li>
 * </ul>
 *
 * 公共搜索基础设施见 {@link Search}，槽位/距离数据见 {@link Slots}，
 * 手序工具见 {@link MoveSeq}，公式数据见 {@link com.temp.cube.constants.Algorithms}。
 */
public final class CfopEngine {

    private final CrossPhase cross = new CrossPhase();
    private final F2LPhase   f2l   = new F2LPhase();
    private final OllPhase   oll   = new OllPhase();
    private final PllPhase   pll   = new PllPhase();

    public int[] solveCross(FaceCube fc) { return cross.solve(fc); }

    public int[] solveF2L(FaceCube fc) { return f2l.solve(fc); }

    public int[] solveOLL(FaceCube fc) { return oll.solve(fc); }

    public int[] solvePLL(FaceCube fc) { return pll.solve(fc); }
}
