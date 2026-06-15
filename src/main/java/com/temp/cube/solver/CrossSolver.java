package com.temp.cube.solver;

import com.temp.cube.model.Cube;
import com.temp.cube.solver.engine.CfopEngine;
import com.temp.cube.solver.engine.FaceCube;
import com.temp.cube.solver.engine.MoveCodec;
import com.temp.cube.turn.Move;

import java.util.List;

/**
 * CFOP 第一步：白色十字（位于 up 面，4 棱与相邻面 center 对齐）。
 * 使用 IDA*（单棱 god 距离作可容纳启发）求解，并把结果施加到传入的 Cube。
 */
public class CrossSolver {

    private final CubeStateChecker checker = new CubeStateChecker();
    private final CfopEngine engine = new CfopEngine();

    /** 返回还原黄十字（底面）所需步骤；不改动传入的 cube。 */
    public List<Move> solve(Cube cube) {
        int[] moves = engine.solveCross(FaceCube.fromCube(cube));
        return MoveCodec.toMoves(moves);
    }

    public boolean isCrossSolved(Cube cube) {
        return checker.isCrossSolved(cube);
    }
}
