package com.temp.rubik;
public class TestAlgs2 {
    public static void main(String[] args) {
        // Test T-perm (should be order 2)
        Cube c = new Cube();
        c.applySequence("R U R' U' R' F R2 U' R' U' R U R' F'");
        c.applySequence("R U R' U' R' F R2 U' R' U' R U R' F'");
        System.out.println("T-perm*2 solved: " + c.isSolved());

        // What does T-perm do?
        c = new Cube();
        c.applySequence("R U R' U' R' F R2 U' R' U' R U R' F'");
        System.out.println("\nAfter T-perm:");
        System.out.println("Corners: F[0]=" + c.get(Cube.F,0) + " F[2]=" + c.get(Cube.F,2)
            + " R[0]=" + c.get(Cube.R,0) + " R[2]=" + c.get(Cube.R,2)
            + " B[0]=" + c.get(Cube.B,0) + " B[2]=" + c.get(Cube.B,2)
            + " L[0]=" + c.get(Cube.L,0) + " L[2]=" + c.get(Cube.L,2));
        System.out.println("Edges: F[1]=" + c.get(Cube.F,1) + " R[1]=" + c.get(Cube.R,1)
            + " B[1]=" + c.get(Cube.B,1) + " L[1]=" + c.get(Cube.L,1));

        // Test: does Sune orient UFR when U-color is on R?
        c = new Cube();
        // Set up: twist UFR so U-color is on R face
        c.applySequence("R U R' U R U2 R'"); // Anti-Sune twists UFR
        System.out.println("\nAfter Anti-Sune, UFR corner:");
        System.out.println("  U[8]=" + c.get(Cube.U,8) + " F[2]=" + c.get(Cube.F,2) + " R[0]=" + c.get(Cube.R,0));
        // Now apply Sune
        c.applySequence("R U2 R' U' R U' R'");
        System.out.println("After Sune on top:");
        System.out.println("  U[8]=" + c.get(Cube.U,8) + " F[2]=" + c.get(Cube.F,2) + " R[0]=" + c.get(Cube.R,0));
        System.out.println("  U face oriented: " + allU(c));

        // Test simpler OLL approach
        c = new Cube();
        c.applySequence("R U2 R' U' R U' R'"); // Sune scrambles corners
        System.out.println("\nAfter Sune (scrambled corners):");
        System.out.println("  U[0]=" + c.get(Cube.U,0) + " U[2]=" + c.get(Cube.U,2)
            + " U[6]=" + c.get(Cube.U,6) + " U[8]=" + c.get(Cube.U,8));
    }

    static boolean allU(Cube c) {
        for (int i = 0; i < 9; i++) if (c.get(Cube.U, i) != Cube.U) return false;
        return true;
    }
}
