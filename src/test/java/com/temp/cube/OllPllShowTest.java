package com.temp.cube;

import com.temp.cube.generator.ScrambleGenerator;
import com.temp.cube.result.Solution;
import com.temp.cube.solver.CFOPSolver;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class OllPllShowTest {

    private final CFOPSolver solver = new CFOPSolver();

    @Test
    public void showFullSolutions() {
        long[] seeds = {7L, 42L, 200L};
        for (long seed : seeds) {
            String scramble = new ScrambleGenerator(seed).generate(20);
            Solution sol = solver.solve(scramble);
            assertTrue("seed=" + seed + " 应完全还原", sol.isSolved());
            System.out.println(solver.getFormatter().formatCFOP(sol));
            System.out.println("\n#############################################\n");
        }
    }
}
