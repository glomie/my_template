package com.temp.rubik;

import java.util.ArrayList;
import java.util.List;

public class Solver {

    private static final Move[] ALL_MOVES = Move.values();
    private static final Move[] FR_MOVES = {Move.R, Move.R_PRIME, Move.R2, Move.U, Move.U_PRIME, Move.U2, Move.F, Move.F_PRIME, Move.F2};
    private static final Move[] FL_MOVES = {Move.L, Move.L_PRIME, Move.L2, Move.U, Move.U_PRIME, Move.U2, Move.F, Move.F_PRIME, Move.F2};
    private static final Move[] BR_MOVES = {Move.R, Move.R_PRIME, Move.R2, Move.U, Move.U_PRIME, Move.U2, Move.B, Move.B_PRIME, Move.B2};
    private static final Move[] BL_MOVES = {Move.L, Move.L_PRIME, Move.L2, Move.U, Move.U_PRIME, Move.U2, Move.B, Move.B_PRIME, Move.B2};

    private final Cube cube;
    private final List<Move> solution = new ArrayList<>();

    public Solver(Cube cube) {
        this.cube = new Cube(cube);
    }

    public List<Move> solve() {
        if (cube.isSolved()) return solution;
        solveCross();
        solveF2L();
        solveOLL();
        solvePLL();
        return new ArrayList<>(solution);
    }

    private void exec(Move m) { cube.apply(m); solution.add(m); }
    private void exec(String seq) { for (String t : seq.trim().split("\\s+")) exec(Move.parse(t)); }
    private void execAll(List<Move> moves) { for (Move m : moves) exec(m); }

    // ==================== IDA* Search ====================

    @FunctionalInterface
    interface Goal { boolean test(); }

    private List<Move> search(Goal goal, Move[] moves, int maxDepth) {
        if (goal.test()) return List.of();
        for (int d = 1; d <= maxDepth; d++) {
            List<Move> path = new ArrayList<>();
            if (dfs(goal, moves, d, -1, path)) return path;
        }
        return null;
    }

    private boolean dfs(Goal goal, Move[] moves, int depth, int lastFace, List<Move> path) {
        if (depth == 0) return goal.test();
        for (Move m : moves) {
            int face = m.ordinal() / 3;
            if (face == lastFace) continue;
            cube.apply(m);
            path.add(m);
            if (dfs(goal, moves, depth - 1, face, path)) return true;
            path.removeLast();
            cube.apply(m.inverse());
        }
        return false;
    }

    // ==================== Cross (D face, one edge at a time) ====================

    private boolean crossEdgeSolved(int idx) {
        return switch (idx) {
            case 0 -> cube.get(Cube.D, 1) == Cube.D && cube.get(Cube.F, 7) == Cube.F;
            case 1 -> cube.get(Cube.D, 5) == Cube.D && cube.get(Cube.R, 7) == Cube.R;
            case 2 -> cube.get(Cube.D, 7) == Cube.D && cube.get(Cube.B, 7) == Cube.B;
            case 3 -> cube.get(Cube.D, 3) == Cube.D && cube.get(Cube.L, 7) == Cube.L;
            default -> false;
        };
    }

    private boolean crossSolvedUpTo(int n) {
        for (int i = 0; i <= n; i++) if (!crossEdgeSolved(i)) return false;
        return true;
    }

    private boolean crossSolved() { return crossSolvedUpTo(3); }

    private void solveCross() {
        for (int i = 0; i < 4; i++) {
            final int idx = i;
            List<Move> result = search(() -> crossSolvedUpTo(idx), ALL_MOVES, 8);
            if (result != null) execAll(result);
        }
    }

    // ==================== F2L (one pair at a time) ====================

    private boolean f2lPairSolved(int idx) {
        return switch (idx) {
            case 0 -> cube.get(Cube.D, 2) == Cube.D && cube.get(Cube.F, 8) == Cube.F && cube.get(Cube.R, 6) == Cube.R
                    && cube.get(Cube.F, 5) == Cube.F && cube.get(Cube.R, 3) == Cube.R;
            case 1 -> cube.get(Cube.D, 0) == Cube.D && cube.get(Cube.F, 6) == Cube.F && cube.get(Cube.L, 8) == Cube.L
                    && cube.get(Cube.F, 3) == Cube.F && cube.get(Cube.L, 5) == Cube.L;
            case 2 -> cube.get(Cube.D, 8) == Cube.D && cube.get(Cube.B, 6) == Cube.B && cube.get(Cube.R, 8) == Cube.R
                    && cube.get(Cube.B, 3) == Cube.B && cube.get(Cube.R, 5) == Cube.R;
            case 3 -> cube.get(Cube.D, 6) == Cube.D && cube.get(Cube.B, 8) == Cube.B && cube.get(Cube.L, 6) == Cube.L
                    && cube.get(Cube.B, 5) == Cube.B && cube.get(Cube.L, 3) == Cube.L;
            default -> false;
        };
    }

    private boolean f2lSolvedUpTo(int n) {
        if (!crossSolved()) return false;
        for (int i = 0; i <= n; i++) if (!f2lPairSolved(i)) return false;
        return true;
    }

    private void solveF2L() {
        Move[][] moveSets = {FR_MOVES, FL_MOVES, BR_MOVES, BL_MOVES};
        for (int i = 0; i < 4; i++) {
            final int idx = i;
            List<Move> result = search(() -> f2lSolvedUpTo(idx), moveSets[i], 10);
            if (result != null) execAll(result);
        }
    }

    // ==================== OLL (2-look) ====================

    private boolean uEdgesOriented() {
        return cube.get(Cube.U, 1) == Cube.U && cube.get(Cube.U, 3) == Cube.U
            && cube.get(Cube.U, 5) == Cube.U && cube.get(Cube.U, 7) == Cube.U;
    }

    private boolean uFaceOriented() {
        for (int i = 0; i < 9; i++) if (cube.get(Cube.U, i) != Cube.U) return false;
        return true;
    }

    private void solveOLL() {
        solveOLLEdges();
        solveOLLCorners();
    }

    private void solveOLLEdges() {
        if (uEdgesOriented()) return;
        int count = 0;
        if (cube.get(Cube.U, 1) == Cube.U) count++;
        if (cube.get(Cube.U, 3) == Cube.U) count++;
        if (cube.get(Cube.U, 5) == Cube.U) count++;
        if (cube.get(Cube.U, 7) == Cube.U) count++;

        if (count == 0) {
            // Dot -> apply twice
            exec("F R U R' U' F'");
            solveOLLEdges();
            return;
        }
        if (count == 2) {
            // Line or L-shape
            if ((cube.get(Cube.U, 3) == Cube.U && cube.get(Cube.U, 5) == Cube.U)
             || (cube.get(Cube.U, 1) == Cube.U && cube.get(Cube.U, 7) == Cube.U)) {
                // Line: orient so it's horizontal (L-R)
                if (cube.get(Cube.U, 1) == Cube.U && cube.get(Cube.U, 7) == Cube.U) exec("U");
                exec("F R U R' U' F'");
            } else {
                // L-shape: orient so the two oriented edges are at back and left
                while (!(cube.get(Cube.U, 1) == Cube.U && cube.get(Cube.U, 3) == Cube.U)) exec("U");
                exec("F U R U' R' F'");
            }
        }
    }

    private void solveOLLCorners() {
        for (int attempt = 0; attempt < 6; attempt++) {
            if (uFaceOriented()) return;
            int oriented = 0;
            if (cube.get(Cube.U, 0) == Cube.U) oriented++;
            if (cube.get(Cube.U, 2) == Cube.U) oriented++;
            if (cube.get(Cube.U, 6) == Cube.U) oriented++;
            if (cube.get(Cube.U, 8) == Cube.U) oriented++;

            if (oriented == 1) {
                // Put oriented corner at UBL (position 0)
                while (cube.get(Cube.U, 0) != Cube.U) exec("U");
                if (cube.get(Cube.R, 0) == Cube.U) {
                    exec("R U2 R' U' R U' R'"); // Sune
                } else {
                    exec("R U R' U R U2 R'"); // Anti-Sune
                }
            } else if (oriented == 0) {
                // Check if R face top-right has U color
                if (cube.get(Cube.R, 2) == Cube.U) {
                    exec("R U2 R' U' R U' R'"); // Sune
                } else if (cube.get(Cube.F, 2) == Cube.U) {
                    exec("U");
                    exec("R U2 R' U' R U' R'");
                } else {
                    exec("R U R' U R U2 R'"); // Anti-Sune
                }
            } else if (oriented == 2) {
                // Two corners oriented - find them and apply Sune
                if (cube.get(Cube.U, 0) == Cube.U && cube.get(Cube.U, 2) == Cube.U) {
                    // Both at back
                    exec("R U2 R' U' R U' R'");
                } else if (cube.get(Cube.U, 6) == Cube.U && cube.get(Cube.U, 8) == Cube.U) {
                    // Both at front
                    exec("U2");
                    exec("R U2 R' U' R U' R'");
                } else if (cube.get(Cube.U, 0) == Cube.U && cube.get(Cube.U, 6) == Cube.U) {
                    // Both on left
                    exec("U'");
                    exec("R U2 R' U' R U' R'");
                } else if (cube.get(Cube.U, 2) == Cube.U && cube.get(Cube.U, 8) == Cube.U) {
                    // Both on right
                    exec("U");
                    exec("R U2 R' U' R U' R'");
                } else {
                    // Diagonal
                    exec("R U2 R' U' R U' R'");
                }
            }
        }
    }

    // ==================== PLL (2-look) ====================

    private void solvePLL() {
        permutePLLCorners();
        permutePLLEdges();
        alignU();
    }

    private boolean cornersPermuted() {
        return cube.get(Cube.F, 0) == cube.get(Cube.F, 2)
            && cube.get(Cube.R, 0) == cube.get(Cube.R, 2)
            && cube.get(Cube.B, 0) == cube.get(Cube.B, 2)
            && cube.get(Cube.L, 0) == cube.get(Cube.L, 2);
    }

    private void permutePLLCorners() {
        for (int attempt = 0; attempt < 4; attempt++) {
            if (cornersPermuted()) return;
            // Find headlights (two same-colored corners on one face)
            int headlight = -1;
            if (cube.get(Cube.F, 0) == cube.get(Cube.F, 2)) headlight = Cube.F;
            else if (cube.get(Cube.R, 0) == cube.get(Cube.R, 2)) headlight = Cube.R;
            else if (cube.get(Cube.B, 0) == cube.get(Cube.B, 2)) headlight = Cube.B;
            else if (cube.get(Cube.L, 0) == cube.get(Cube.L, 2)) headlight = Cube.L;

            if (headlight >= 0) {
                // Put headlights at back, then apply T-perm
                switch (headlight) {
                    case Cube.F -> exec("U2");
                    case Cube.R -> exec("U");
                    case Cube.L -> exec("U'");
                }
                // Jb-perm (swaps two adjacent corners at front)
                exec("R U R' F' R U R' U' R' F R2 U' R'");
            } else {
                // No headlights -> diagonal swap, apply Y-perm
                exec("F R U' R' U' R U R' F' R U R' U' R' F R F'");
            }
        }
    }

    private void permutePLLEdges() {
        for (int attempt = 0; attempt < 4; attempt++) {
            int solved = 0;
            if (cube.get(Cube.F, 1) == cube.get(Cube.F, 0)) solved++;
            if (cube.get(Cube.R, 1) == cube.get(Cube.R, 0)) solved++;
            if (cube.get(Cube.B, 1) == cube.get(Cube.B, 0)) solved++;
            if (cube.get(Cube.L, 1) == cube.get(Cube.L, 0)) solved++;

            if (solved == 4) return;

            if (solved == 1) {
                // One edge solved - find it and put at back
                if (cube.get(Cube.F, 1) == cube.get(Cube.F, 0)) exec("U2");
                else if (cube.get(Cube.R, 1) == cube.get(Cube.R, 0)) exec("U");
                else if (cube.get(Cube.L, 1) == cube.get(Cube.L, 0)) exec("U'");
                // B edge is solved, determine CW or CCW
                if (cube.get(Cube.R, 1) == cube.get(Cube.F, 0)) {
                    // Ua-perm (CCW cycle: F->L->R->F viewed from top)
                    exec("R U' R U R U R U' R' U' R2");
                } else {
                    // Ub-perm (CW cycle)
                    exec("R2 U R U R' U' R' U' R' U R'");
                }
                return;
            }

            if (solved == 0) {
                // No edge solved - H-perm or Z-perm
                if (cube.get(Cube.F, 1) == cube.get(Cube.B, 0)) {
                    // H-perm (opposite swap)
                    exec("R2 U2 R U2 R2 U2 R2 U2 R U2 R2");
                } else {
                    // Z-perm (adjacent swap)
                    exec("U");
                    exec("R2 U2 R U2 R2 U2 R2 U2 R U2 R2");
                    exec("U");
                }
                return;
            }
        }
    }

    private void alignU() {
        for (int i = 0; i < 4; i++) {
            if (cube.isSolved()) return;
            exec("U");
        }
    }
}
