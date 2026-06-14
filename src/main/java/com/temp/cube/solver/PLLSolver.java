package com.temp.cube.solver;

import com.temp.cube.model.Cube;
import com.temp.cube.solver.engine.CfopEngine;
import com.temp.cube.solver.engine.FaceCube;
import com.temp.cube.solver.engine.MoveCodec;
import com.temp.cube.turn.SideTurnAction;

import java.util.List;

/**
 * CFOP 第四步：PLL（排列最后一层）。把顶层各块归位，完成整个魔方。
 * 使用 {U, U', A-perm(纯角3循环), U-perm(纯棱3循环)} 宏操作迭代加深求解。
 */
public class PLLSolver {

    private final CubeStateChecker checker = new CubeStateChecker();
    private final CfopEngine engine = new CfopEngine();

    /** 返回 PLL（完全还原）所需步骤；不改动传入的 cube。 */
    public List<SideTurnAction> solve(Cube cube) {
        int[] moves = engine.solvePLL(FaceCube.fromCube(cube));
        return MoveCodec.toActions(moves);
    }

    public boolean isPLLSolved(Cube cube) {
        return checker.isPLLSolved(cube);
    }
}
