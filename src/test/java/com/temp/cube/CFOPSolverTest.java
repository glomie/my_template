package com.temp.cube;

import com.temp.cube.model.Cube;
import com.temp.cube.solver.*;
import com.temp.cube.turn.Move;
import com.temp.cube.turn.SideTurnAction;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class CFOPSolverTest {

    private final CFOPSolver solver = new CFOPSolver();
    private final CubeStateChecker checker = new CubeStateChecker();
    private final ScrambleGenerator generator = new ScrambleGenerator();

    /** 已还原的魔方，解法应为空且已解决 */
    @Test
    public void testSolvedCubeNeedNoMoves() {
        Cube cube = Cube.init();
        Solution solution = solver.solve(cube);
        assertTrue("solved cube should be solved after empty solution", solution.isSolved());
        assertEquals(0, solution.getTotalMoves());
    }

    /** 单步打乱后能还原 */
    @Test
    public void testSingleMove() {
        String[] singles = {"R", "R'", "L", "L'", "U", "U'", "D", "D'", "F", "F'", "B", "B'"};
        for (String move : singles) {
            Solution solution = solver.solve(move);
            assertTrue("should solve single move: " + move, solution.isSolved());
        }
    }

    /** 固定seed打乱后能还原 */
    @Test
    public void testFixedSeedScramble() {
        long[] seeds = {42L, 12345L, 99999L};
        for (long seed : seeds) {
            Solution solution = solver.solve(seed);
            assertTrue("should solve seed=" + seed + ", scramble=" + solution.getScramble(),
                       solution.isSolved());
        }
    }

    /** 随机生成20步打乱后能还原（运行5次） */
    @Test
    public void testRandomScrambles() {
        for (int i = 0; i < 5; i++) {
            String scramble = generator.generate(20);
            Solution solution = solver.solve(scramble);
            assertTrue("should solve scramble: " + scramble, solution.isSolved());
        }
    }

    /** 验证Solution的解法步骤真的能把打乱的魔方还原 */
    @Test
    public void testSolutionMovesActuallyWork() {
        String scramble = new ScrambleGenerator(7777L).generate(15);

        // 打乱魔方
        List<SideTurnAction> scrambleMoves = generator.parseMoves(scramble);
        Cube cube = Cube.init();
        for (SideTurnAction m : scrambleMoves) cube.turn(m.getSideTurnEnum(), m.getDirection());
        assertFalse("cube should be scrambled", checker.isSolved(cube));

        // 求解，拿到步骤列表
        Solution solution = solver.solve(scramble);
        List<Move> allMoves = new java.util.ArrayList<>(solution.getCrossMoves());
        allMoves.addAll(solution.getF2lMoves());
        allMoves.addAll(solution.getOllMoves());
        allMoves.addAll(solution.getPllMoves());

        // 在新的打乱魔方上重新执行解法步骤
        Cube cube2 = Cube.init();
        for (SideTurnAction m : scrambleMoves) cube2.turn(m.getSideTurnEnum(), m.getDirection());
        for (Move m : allMoves) m.apply(cube2);

        assertTrue("solution moves should fully restore cube", checker.isSolved(cube2));
    }

    /** 验证各CFOP阶段状态正确 */
    @Test
    public void testCFOPStages() {
        String scramble = new ScrambleGenerator(2024L).generate(20);
        List<SideTurnAction> scrambleMoves = generator.parseMoves(scramble);

        Cube cube = Cube.init();
        for (SideTurnAction m : scrambleMoves) cube.turn(m.getSideTurnEnum(), m.getDirection());

        // Cross
        CrossSolver crossSolver = new CrossSolver();
        List<Move> cross = crossSolver.solve(cube);
        for (Move m : cross) m.apply(cube);
        assertTrue("cross should be solved after CrossSolver", checker.isCrossSolved(cube));

        // F2L
        F2LSolver f2lSolver = new F2LSolver();
        List<Move> f2l = f2lSolver.solve(cube);
        for (Move m : f2l) m.apply(cube);
        assertTrue("F2L should be solved after F2LSolver", checker.isF2LSolved(cube));

        // OLL
        OLLSolver ollSolver = new OLLSolver();
        List<Move> oll = ollSolver.solve(cube);
        for (Move m : oll) m.apply(cube);
        assertTrue("OLL should be solved after OLLSolver", checker.isOLLSolved(cube));

        // PLL
        PLLSolver pllSolver = new PLLSolver();
        List<Move> pll = pllSolver.solve(cube);
        for (Move m : pll) m.apply(cube);
        assertTrue("PLL should be solved after PLLSolver", checker.isPLLSolved(cube));
    }

    /** 打印一次完整解法供肉眼验证 */
    @Test
    public void testPrintSolution() {
        String scramble = new ScrambleGenerator(1234L).generate(20);
        Solution solution = solver.solve(scramble);
        String formatted = solver.getFormatter().formatCFOP(solution);
        System.out.println(formatted);
        assertTrue("should be solved", solution.isSolved());
    }
}
