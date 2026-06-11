package com.temp.cube.solver;

import com.temp.cube.model.Cube;
import com.temp.cube.turn.SideTurnAction;

import java.util.List;

public class CFOPSolver {

    private final CrossSolver crossSolver   = new CrossSolver();
    private final F2LSolver   f2lSolver     = new F2LSolver();
    private final OLLSolver   ollSolver     = new OLLSolver();
    private final PLLSolver   pllSolver     = new PLLSolver();
    private final CubeStateChecker checker  = new CubeStateChecker();
    private final ScrambleGenerator scrambleGen = new ScrambleGenerator();
    private final SolutionFormatter formatter   = new SolutionFormatter();

    /** 直接对已打乱的Cube对象求解（cube状态会被修改为还原状态）。 */
    public Solution solve(Cube cube) {
        Solution solution = new Solution();

        List<SideTurnAction> cross = crossSolver.solve(cube);
        solution.setCrossMoves(cross);

        List<SideTurnAction> f2l = f2lSolver.solve(cube);
        solution.setF2lMoves(f2l);

        List<SideTurnAction> oll = ollSolver.solve(cube);
        solution.setOllMoves(oll);

        List<SideTurnAction> pll = pllSolver.solve(cube);
        solution.setPllMoves(pll);

        solution.calculateTotalMoves();
        solution.setSolved(checker.isSolved(cube));
        return solution;
    }

    /** 给定打乱公式字符串，初始化魔方后求解。 */
    public Solution solve(String scramble) {
        List<SideTurnAction> scrambleMoves = scrambleGen.parseMoves(scramble);
        Cube cube = Cube.init();
        for (SideTurnAction m : scrambleMoves) cube.turn(m.getSideTurnEnum(), m.getDirection());

        Solution solution = solve(cube);
        solution.setScramble(scramble);
        return solution;
    }

    /** 用给定seed生成打乱后求解。 */
    public Solution solve(long seed) {
        ScrambleGenerator gen = new ScrambleGenerator(seed);
        String scramble = gen.generate(20);
        return solve(scramble);
    }

    public Solution solveWithScramble() {
        String scramble = scrambleGen.generate(20);
        return solve(scramble);
    }

    public String solveAndFormat(Cube cube) {
        return formatter.formatCFOP(solve(cube));
    }

    public String solveAndFormat(String scramble) {
        return formatter.formatCFOP(solve(scramble));
    }

    public String solveWithScrambleAndFormat() {
        return formatter.formatCFOP(solveWithScramble());
    }

    public ScrambleGenerator getScrambleGenerator() { return scrambleGen; }
    public SolutionFormatter getFormatter()         { return formatter; }
}
