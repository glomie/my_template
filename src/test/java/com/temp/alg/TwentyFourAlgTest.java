package com.temp.alg;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;

public class TwentyFourAlgTest {

    @Test
    public void solvesBasicCombination() {
        List<String> solutions = TwentyFourAlg.solve(1, 2, 3, 4);

        assertFalse(solutions.isEmpty());
    }

    @Test
    public void solvesCombinationRequiringFractions() {
        assertTrue(TwentyFourAlg.canSolve(3, 3, 8, 8));
    }

    @Test
    public void reportsCombinationWithoutSolution() {
        assertFalse(TwentyFourAlg.canSolve(1, 1, 1, 1));
    }

    @Test
    public void requiresExactlyFourNumbers() {
        try {
            TwentyFourAlg.solve(1, 2, 3);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            assertTrue(expected.getMessage().contains("four"));
        }
    }
}
