package com.temp.cube.solver;

import com.temp.cube.enums.Direction;
import com.temp.cube.enums.SideTurnEnum;
import com.temp.cube.model.Cube;
import com.temp.cube.turn.SideTurnAction;

import java.util.ArrayList;
import java.util.List;

public class CFOPSolver {

    private ScrambleGenerator scrambleGenerator;
    private SolutionFormatter formatter;
    private CubeStateChecker stateChecker;

    public CFOPSolver() {
        this.scrambleGenerator = new ScrambleGenerator();
        this.formatter = new SolutionFormatter();
        this.stateChecker = new CubeStateChecker();
    }

    public Solution solve(Cube cube) {
        Solution solution = new Solution();
        List<SideTurnAction> allMoves = new ArrayList<>();

        List<SideTurnAction> crossMoves = solveCross(cube);
        allMoves.addAll(crossMoves);
        solution.setCrossMoves(crossMoves);

        List<SideTurnAction> f2lMoves = solveF2L(cube);
        allMoves.addAll(f2lMoves);
        solution.setF2lMoves(f2lMoves);

        List<SideTurnAction> ollMoves = solveOLL(cube);
        allMoves.addAll(ollMoves);
        solution.setOllMoves(ollMoves);

        List<SideTurnAction> pllMoves = solvePLL(cube);
        allMoves.addAll(pllMoves);
        solution.setPllMoves(pllMoves);

        solution.calculateTotalMoves();
        
        boolean isSolved = stateChecker.isSolved(cube);
        solution.setSolved(isSolved);

        return solution;
    }

    private List<SideTurnAction> solveCross(Cube cube) {
        List<SideTurnAction> moves = new ArrayList<>();
        
        String[] crossColors = {"GREEN", "RED", "BLUE", "ORANGE"};
        
        for (int i = 0; i < 4; i++) {
            String targetColor = crossColors[i];
            
            for (int j = 0; j < 30; j++) {
                if (isCrossEdgeInPlace(cube, targetColor)) {
                    break;
                }
                
                int bestMove = findBestCrossMove(cube, targetColor);
                if (bestMove >= 0) {
                    SideTurnEnum side = getSideForColor(targetColor);
                    moves.add(new SideTurnAction(side, Direction.CLOCKWISE));
                    cube.turn(side, Direction.CLOCKWISE);
                } else {
                    moves.add(new SideTurnAction(SideTurnEnum.U, Direction.CLOCKWISE));
                    cube.turn(SideTurnEnum.U, Direction.CLOCKWISE);
                }
            }
        }
        
        return moves;
    }

    private SideTurnEnum getSideForColor(String color) {
        switch (color) {
            case "GREEN": return SideTurnEnum.F;
            case "RED": return SideTurnEnum.R;
            case "BLUE": return SideTurnEnum.B;
            case "ORANGE": return SideTurnEnum.L;
            default: return SideTurnEnum.U;
        }
    }

    private boolean isCrossEdgeInPlace(Cube cube, String color) {
        String[][] up = cube.getUpSide().getOutputArray();
        
        switch (color) {
            case "GREEN": return up[0][1].equals("WHITE");
            case "RED": return up[1][2].equals("WHITE");
            case "BLUE": return up[2][1].equals("WHITE");
            case "ORANGE": return up[1][0].equals("WHITE");
        }
        return false;
    }

    private int findBestCrossMove(Cube cube, String color) {
        return 0;
    }

    private List<SideTurnAction> solveF2L(Cube cube) {
        List<SideTurnAction> moves = new ArrayList<>();
        
        for (int i = 0; i < 40; i++) {
            if (stateChecker.isF2LSolved(cube)) break;
            
            moves.add(new SideTurnAction(SideTurnEnum.U, Direction.CLOCKWISE));
            cube.turn(SideTurnEnum.U, Direction.CLOCKWISE);
        }
        
        return moves;
    }

    private List<SideTurnAction> solveOLL(Cube cube) {
        List<SideTurnAction> moves = new ArrayList<>();
        
        String[][] up = cube.getUpSide().getOutputArray();
        int whiteCount = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (up[i][j].equals("WHITE")) whiteCount++;
            }
        }
        
        if (whiteCount < 9) {
            if (whiteCount <= 1) {
                applyAlg(cube, moves, "F R U R' U' F'");
            }
            
            applyAlg(cube, moves, "R U R' U R U2 R'");
            
            for (int i = 0; i < 3; i++) {
                if (stateChecker.isOLLSolved(cube)) break;
                applyAlg(cube, moves, "U");
            }
        }
        
        return moves;
    }

    private List<SideTurnAction> solvePLL(Cube cube) {
        List<SideTurnAction> moves = new ArrayList<>();
        
        if (!stateChecker.isPLLSolved(cube)) {
            applyAlg(cube, moves, "U");
            applyAlg(cube, moves, "R U R' U' R' F R2 U' R' U' R U R' F'");
        }
        
        return moves;
    }

    private void applyAlg(Cube cube, List<SideTurnAction> moves, String alg) {
        String[] parts = alg.split(" ");
        for (String part : parts) {
            if (part.isEmpty()) continue;
            
            char base = part.charAt(0);
            Direction dir = Direction.CLOCKWISE;
            if (part.contains("'")) dir = Direction.COUNTERCLOCKWISE;
            int count = part.contains("2") ? 2 : 1;
            
            SideTurnEnum side = null;
            if (base == 'R') side = SideTurnEnum.R;
            else if (base == 'L') side = SideTurnEnum.L;
            else if (base == 'U') side = SideTurnEnum.U;
            else if (base == 'D') side = SideTurnEnum.D;
            else if (base == 'F') side = SideTurnEnum.F;
            else if (base == 'B') side = SideTurnEnum.B;
            
            if (side != null) {
                for (int i = 0; i < count; i++) {
                    moves.add(new SideTurnAction(side, dir));
                    cube.turn(side, dir);
                }
            }
        }
    }

    public Solution solveWithScramble() {
        String scramble = scrambleGenerator.generate(20);
        return solve(scramble);
    }

    public Solution solve(String scramble) {
        Solution solution = new Solution();
        solution.setScramble(scramble);

        ScrambleGenerator parser = new ScrambleGenerator();
        List<SideTurnAction> scrambleMoves = parser.parseMoves(scramble);

        Cube cube = Cube.init();
        for (SideTurnAction move : scrambleMoves) {
            cube.turn(move.getSideTurnEnum(), move.getDirection());
        }

        return solve(cube);
    }

    public Solution solve(long seed) {
        ScrambleGenerator generator = new ScrambleGenerator(seed);
        String scramble = generator.generate(20);
        return solve(scramble);
    }

    public String solveAndFormat(Cube cube) {
        Solution solution = solve(cube);
        return formatter.formatCFOP(solution);
    }

    public String solveWithScrambleAndFormat() {
        Solution solution = solveWithScramble();
        return formatter.formatCFOP(solution);
    }

    public String solveAndFormat(String scramble) {
        Solution solution = solve(scramble);
        return formatter.formatCFOP(solution);
    }

    public ScrambleGenerator getScrambleGenerator() {
        return scrambleGenerator;
    }

    public SolutionFormatter getFormatter() {
        return formatter;
    }
}
