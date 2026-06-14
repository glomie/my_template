package com.temp.cube;

import com.temp.cube.model.Cube;
import com.temp.cube.solver.CFOPSolver;
import com.temp.cube.solver.CubeStateChecker;
import com.temp.cube.solver.ScrambleGenerator;
import com.temp.cube.solver.Solution;
import com.temp.cube.turn.Move;
import com.temp.cube.turn.SideTurnAction;

/**
 * CFOP 解法演示入口：随机打乱一个魔方，用 CFOP 四阶段求解，并输出每一步与最终验证结果。
 */
public class Main {

    public static void main(String[] args) {
        ScrambleGenerator generator = new ScrambleGenerator();
        CFOPSolver solver = new CFOPSolver();
        CubeStateChecker checker = new CubeStateChecker();

        // 1) 生成随机打乱
        String scramble = generator.generate(20);

        // 2) 求解
        Solution solution = solver.solve(scramble);

        // 3) 输出解法
        System.out.println(solver.getFormatter().formatCFOP(solution));

        // 4) 在新魔方上重放：打乱 + 解法，验证是否真的还原
        Cube cube = Cube.init();
        for (SideTurnAction m : generator.parseMoves(scramble)) cube.turn(m.getSideTurnEnum(), m.getDirection());
        for (Move m : solution.getCrossMoves()) m.apply(cube);
        for (Move m : solution.getF2lMoves()) m.apply(cube);
        for (Move m : solution.getOllMoves()) m.apply(cube);
        for (Move m : solution.getPllMoves()) m.apply(cube);

        System.out.println();
        System.out.println("验证：执行 [打乱 + 解法] 后魔方是否还原 = " + checker.isSolved(cube));
    }
}
