package com.temp.cube.solver;

import com.temp.cube.model.Cube;
import com.temp.cube.solver.engine.CfopEngine;
import com.temp.cube.solver.engine.FaceCube;
import com.temp.cube.solver.engine.MoveCodec;
import com.temp.cube.turn.SideTurnAction;

import java.util.List;

/**
 * CFOP 第二步：F2L。把下两层（黄色底面 + 中层）全部还原，同时保持已完成的白十字。
 * 先逐个插入 4 个底棱，再逐个插入 4 个 角+中棱 对；每步用 IDA* 并锁定已还原贴纸不被破坏。
 */
public class F2LSolver {

    private final CubeStateChecker checker = new CubeStateChecker();
    private final CfopEngine engine = new CfopEngine();

    /** 返回还原 F2L 所需步骤；不改动传入的 cube。 */
    public List<SideTurnAction> solve(Cube cube) {
        int[] moves = engine.solveF2L(FaceCube.fromCube(cube));
        return MoveCodec.toActions(moves);
    }

    public boolean isF2LSolved(Cube cube) {
        return checker.isF2LSolved(cube);
    }
}
