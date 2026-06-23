package com.temp.cube;

import com.temp.cube.generator.ScrambleGenerator;
import com.temp.cube.result.Solution;
import com.temp.cube.solver.CFOPSolver;
import com.temp.cube.turn.Move;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

public class OllPllShowTest {

    private final CFOPSolver solver = new CFOPSolver();

    @Test
    public void showOllPllExamples() {
        long[] seeds = {1L, 7L, 42L, 100L, 200L, 555L, 1234L, 9999L};
        System.out.println("========== OLL / PLL 标准公式示例 ==========\n");
        for (long seed : seeds) {
            String scramble = new ScrambleGenerator(seed).generate(20);
            Solution sol = solver.solve(scramble);
            assertTrue("seed=" + seed + " 应完全还原", sol.isSolved());

            String oll = fmt(sol.getOllMoves());
            String pll = fmt(sol.getPllMoves());
            System.out.printf("Seed %-5d | 打乱: %s%n", seed, scramble);
            System.out.printf("          | OLL: %s%n", oll.isEmpty() ? "(已定向)" : oll);
            System.out.printf("          | PLL: %s%n%n", pll.isEmpty() ? "(已还原)" : pll);
        }
    }

    private static String fmt(List<Move> moves) {
        return moves.stream().map(Move::notation).collect(Collectors.joining(" "));
    }
}
