package com.temp.cube.solver;

import com.temp.cube.model.Cube;
import com.temp.cube.solver.engine.CfopEngine;
import com.temp.cube.solver.engine.FaceCube;
import com.temp.cube.solver.engine.MoveCodec;
import com.temp.cube.turn.SideTurnAction;

import java.util.List;

/**
 * CFOP 第三步：OLL（定向最后一层）。此处只需把 up 面全部翻成白色。
 * 经过 F2L 后仅剩顶层 4 个角的朝向问题，使用 {U, Sune, Antisune} 宏操作迭代加深求解。
 */
public class OLLSolver {

    private final CubeStateChecker checker = new CubeStateChecker();
    private final CfopEngine engine = new CfopEngine();

    /** 返回 OLL（顶面全白）所需步骤；不改动传入的 cube。 */
    public List<SideTurnAction> solve(Cube cube) {
        int[] moves = engine.solveOLL(FaceCube.fromCube(cube));
        return MoveCodec.toActions(moves);
    }

    public boolean isOLLSolved(Cube cube) {
        return checker.isOLLSolved(cube);
    }
}
