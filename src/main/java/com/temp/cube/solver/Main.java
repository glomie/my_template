package com.temp.cube.solver;

import com.temp.cube.model.Cube;
import com.temp.cube.turn.SideTurnAction;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== CFOP Rubik's Cube Solver ===\n");
        System.out.println("Note: This is a framework implementation.\n");
        System.out.println("The solver algorithms need further development to fully solve the cube.\n");

        CFOPSolver solver = new CFOPSolver();
        ScrambleGenerator generator = new ScrambleGenerator();

        // 生成随机打乱
        String scramble = generator.generate(20);
        System.out.println("Scramble: " + scramble);

        // 解析打乱
        ScrambleGenerator parser = new ScrambleGenerator();
        List<SideTurnAction> scrambleMoves = parser.parseMoves(scramble);

        // 创建魔方并应用打乱
        Cube cube = Cube.init();
        for (SideTurnAction move : scrambleMoves) {
            cube.turn(move.getSideTurnEnum(), move.getDirection());
        }

        // 检查状态
        CubeStateChecker checker = new CubeStateChecker();
        System.out.println("After scramble:");
        System.out.println("  isSolved: " + checker.isSolved(cube));
        System.out.println("  isCrossSolved: " + checker.isCrossSolved(cube));

        // 求解
        Solution solution = solver.solve(scramble);
        solution.setScramble(scramble);

        // 输出
        System.out.println("\n" + solver.getFormatter().formatCFOP(solution));

        System.out.println("\n=== Framework Components ===");
        System.out.println("1. ScrambleGenerator - Generate random scrambles");
        System.out.println("2. CFOPSolver - Main solver (needs algorithm improvement)");
        System.out.println("3. CrossSolver, F2LSolver, OLLSolver, PLLSolver");
        System.out.println("4. CubeStateChecker - Check cube state");
        System.out.println("5. SolutionFormatter - Format output");
    }
}
